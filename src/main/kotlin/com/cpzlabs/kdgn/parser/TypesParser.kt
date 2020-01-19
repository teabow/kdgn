package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.extensions.getNodeComments
import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.psi.Converter
import kastree.ast.psi.Parser


val PROTECTED_MODIFIER = listOf("PRIVATE", "PROTECTED")

data class Type(
    val name: String,
    val packageName: String? = null,
    val implementing: List<Type>? = null,
    val inheriting: List<Type>? = null,
    var members: List<Type>? = null,
    var annotations: Map<String, String> = emptyMap()
)

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
                val propertiesMembersTypes: List<Type> = visitedNode.members.fold(mutableListOf()) { acc, current ->
                    val prop = current as? Node.Decl.Property
                    if (prop != null && isPropertyVisible(prop)) {
                        mutableListOf(
                            *acc.toTypedArray(),
                            *prop.vars.mapNotNull { v ->
                                if (v != null) Type(
                                    name = v.name,
                                    annotations = prop.getNodeComments(extrasMap)
                                ) else null
                            }.toTypedArray()
                        )
                    } else acc
                }
                // parse primary constructor members
                val constructorMembersTypes: List<Type> =
                    visitedNode.primaryConstructor?.params?.fold(mutableListOf()) { acc, current ->
                        current.readOnly?.let {
                            mutableListOf(
                                *acc.toTypedArray(),
                                Type(name = current.name, annotations = current.getNodeComments(extrasMap))
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
        }
    }

    return types
}
