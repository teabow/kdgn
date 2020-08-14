package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.models.Type

private const val IMPORT_STATEMENT = "import"
private const val UTIL_IMPORT_PACKAGE = "java.util"
private val UTIL_COLLECTIONS = listOf("List", "ArrayList", "LinkedList", "Map", "HashMap", "TreeMap", "Set")

fun generateImports(content: String, types: List<Type>, lineSuffix: String = "", postFix: String = "\n\n"): String {
    val typesNames = types.map { it.name };
    val regex = mutableListOf(*UTIL_COLLECTIONS.toTypedArray(), *typesNames.toTypedArray())
        .joinToString("|").toRegex()
    val results = regex.findAll(content)

    val importsSet: Set<String> = results.foldIndexed(mutableSetOf()) { index, acc, current ->
        val existingType = types.find { it.name == current.value }
        val importPrefix = existingType?.packageName ?: UTIL_IMPORT_PACKAGE

        acc.add("$IMPORT_STATEMENT $importPrefix.${current.value}$lineSuffix")
        acc
    }

    return importsSet.joinToString("\n") + postFix
}
