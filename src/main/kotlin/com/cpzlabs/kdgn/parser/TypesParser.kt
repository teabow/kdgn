package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.extensions.getNodeComments
import com.cpzlabs.kdgn.models.Member
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

fun parseFile(fileContent: String): List<Type> {
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
                val propertiesMembersTypes: List<Member> = visitedNode.members.fold(mutableListOf()) { acc, current ->
                    val prop = current as? Node.Decl.Property
                    if (prop != null && isPropertyVisible(prop)) {
                        mutableListOf(
                            *acc.toTypedArray(),
                            *prop.vars.mapNotNull { v ->
                                if (v != null) {
                                    Member(
                                        name = v.name,
                                        annotations = prop.getNodeComments(extrasMap)
                                    )
                                } else {
                                    null
                                }
                            }.toTypedArray()
                        )
                    } else acc
                }
                // parse primary constructor members
                val constructorMembersTypes: List<Member> =
                    visitedNode.primaryConstructor?.params?.fold(mutableListOf()) { acc, current ->
                        current.readOnly?.let {
                            mutableListOf(
                                *acc.toTypedArray(),
                                Member(name = current.name, annotations = current.getNodeComments(extrasMap))
                            )
                        } ?: acc
                    } ?: emptyList()

                types += Type(
                    name = visitedNode.name,
                    packageName = packageName,
                    implementing = parent.flatMap { it?.type?.pieces ?: emptyList() }.map {
                        Type(it.name)
                    },
                    members = listOf(*propertiesMembersTypes.toTypedArray(), *constructorMembersTypes.toTypedArray()),
                    annotations = visitedNode.getNodeComments(extrasMap)
                )
            }
            else -> {
                // do nothing
            }
        }
    }

    return types
}

private fun getNodeAnnotations(node: com.github.javaparser.ast.Node): Map<String, String> {
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
                                    annotations = getNodeAnnotations(member)
                                )
                            } else {
                                null
                            }
                        },
                        implementing = node.implementedTypes.map { implemented ->
                            Type(
                                name = implemented.nameAsString
                            )
                        },
                        annotations = getNodeAnnotations(node)
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
