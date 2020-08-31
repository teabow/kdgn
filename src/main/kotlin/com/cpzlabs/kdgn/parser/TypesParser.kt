package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.extensions.getDeclaredType
import com.cpzlabs.kdgn.extensions.getNodeComments
import com.cpzlabs.kdgn.extensions.getReturnType
import com.cpzlabs.kdgn.models.Member
import com.cpzlabs.kdgn.models.Method
import com.cpzlabs.kdgn.models.Type
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.BodyDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.visitor.TreeVisitor
import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.psi.Converter
import kastree.ast.psi.Parser


val PROTECTED_MODIFIER = listOf("PRIVATE", "PROTECTED")

fun isPropertyVisible(property: Node.Decl.Property): Boolean {
    return !property.mods.any {
        val modifier = it as? Node.Modifier.Lit
        PROTECTED_MODIFIER.contains(modifier?.keyword?.name)
    }
}

private fun getKtNodeAnnotations(node: Node.WithAnnotations): Map<String, String> {
    return node.anns.flatMap { it.anns }.fold(mutableMapOf(), { acc, current ->
        val value = if (current.args.count() > 0) current.args.mapNotNull {
            val expr = (it.expr as? Node.Expr.Const)?.value ?: ""
            if (expr.isNotEmpty()) {
                "${it.name} = $expr"
            } else {
                null
            }
        }.joinToString() else ""
        acc[current.names.first()] = value
        acc
    })
}

fun parseKtFile(fileContent: String): List<Type> {
    val types = mutableListOf<Type>()
    var packageName = ""

    val extrasMap = Converter.WithExtras()
    val file = Parser(extrasMap).parseFile(fileContent)

    Visitor.visit(file) { visitedNode, _ ->

        when (visitedNode) {
            is Node.Package -> {
                packageName = visitedNode.names.joinToString(".")
            }

            is Node.Decl.Structured -> {
                val parent = visitedNode.parents.map { it as? Node.Decl.Structured.Parent.Type }

                // parse properties members
                val membersPair = Pair<List<Member>, List<Method>>(mutableListOf(), mutableListOf())
                val membersTypes = visitedNode.members.fold(membersPair) { acc, current ->

                    val (members, methods) = acc

                    when (current) {
                        is Node.Decl.Property -> {
                            if (isPropertyVisible(current)) {
                                val propAnnotations = current.getNodeComments(extrasMap).toMutableMap()
                                propAnnotations.putAll(getKtNodeAnnotations(current))

                                Pair(
                                    mutableListOf(
                                        *members.toTypedArray(),
                                        *current.vars.mapNotNull { v ->
                                            if (v != null) {
                                                Member(
                                                    name = v.name,
                                                    type = v.getDeclaredType(),
                                                    annotations = propAnnotations
                                                )
                                            } else {
                                                null
                                            }
                                        }.toTypedArray()
                                    ), acc.second
                                )
                            } else {
                                acc
                            }
                        }
                        is Node.Decl.Func -> {
                            if (current.name != null) {
                                Pair(
                                    acc.first, mutableListOf(
                                        *methods.toTypedArray(),
                                        Method(current.name!!, returnType = current.getReturnType())
                                    )
                                )
                            } else acc
                        }
                        else -> {
                            // do nothing
                            acc
                        }
                    }
                }
                // parse primary constructor members
                val constructorMembersTypes
                        : List<Member> =
                    visitedNode.primaryConstructor?.params?.fold(mutableListOf()) { acc, current ->
                        val propAnnotations = current.getNodeComments(extrasMap).toMutableMap()
                        propAnnotations.putAll(getKtNodeAnnotations(current))
                        current.readOnly?.let {
                            mutableListOf(
                                *acc.toTypedArray(),
                                Member(name = current.name, type = current.getDeclaredType(), annotations = propAnnotations)
                            )
                        } ?: acc
                    } ?: emptyList()

                val typeAnnotations = visitedNode.getNodeComments(extrasMap).toMutableMap()
                typeAnnotations.putAll(getKtNodeAnnotations(visitedNode))

                types += Type(
                    name = visitedNode.name,
                    packageName = packageName,
                    implementing = parent.flatMap { it?.type?.pieces ?: emptyList() }.map {
                        Type(it.name)
                    },
                    members = listOf(*membersTypes.first.toTypedArray(), *constructorMembersTypes.toTypedArray()),
                    methods = membersTypes.second,
                    annotations = typeAnnotations
                )
            }
            else -> {
                // do nothing
            }
        }
    }

    return types
}

private fun getJavaNodeAnnotations(node: com.github.javaparser.ast.Node): Map<String, String> {
    return if (node is BodyDeclaration<*>) {
        return node.annotations.fold(mutableMapOf(), { acc, current ->
            acc[current.nameAsString] = if (current is NormalAnnotationExpr) current.pairs.joinToString() else ""
            acc
        })
    } else emptyMap()
}

fun parseJavaFile(fileContent: String): List<Type> {
    val types = mutableListOf<Type>()
    var packageName = ""

    val parser = JavaParser()
    val declaration = parser.parse(fileContent)

    val visitor = object : TreeVisitor() {
        override fun process(node: com.github.javaparser.ast.Node?) {
            when (node) {
                is PackageDeclaration -> {
                    packageName = node.name.asString()
                }

                is ClassOrInterfaceDeclaration -> {
                    types += Type(
                        name = node.name.asString(),
                        packageName = packageName,
                        members = node.members.mapNotNull { member ->
                            if (member is FieldDeclaration && member.variables.first.isPresent) {
                                val variable = member.variables.first.get()
                                Member(
                                    name = variable.nameAsString,
                                    type = variable.type.asString(),
                                    annotations = getJavaNodeAnnotations(member)
                                )
                            } else {
                                null
                            }
                        },
                        methods = node.methods.map {
                            Method(
                                it.nameAsString,
                                returnType = it.typeAsString,
                                annotations = getJavaNodeAnnotations(it)
                            )
                        },
                        implementing = node.implementedTypes.map { implemented ->
                            Type(
                                name = implemented.nameAsString
                            )
                        },
                        annotations = getJavaNodeAnnotations(node)
                    )
                }
            }
            if (node?.childNodes?.size ?: 0 > 0) {
                visitDirectChildren(node)
            }
        }
    }

    if (declaration.result.isPresent) {
        visitor.process(declaration.result.get().findRootNode())
    }

    return types
}
