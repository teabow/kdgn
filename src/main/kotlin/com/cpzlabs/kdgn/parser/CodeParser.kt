package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.extensions.JAVA_FILE_EXTENSION
import com.cpzlabs.kdgn.extensions.KOTLIN_FILE_EXTENSION
import com.cpzlabs.kdgn.extensions.asString
import com.cpzlabs.kdgn.extensions.selectRegularFiles
import com.cpzlabs.kdgn.models.Type


fun parseCode(packageSource: String): List<Type> {
    val kotlinFilesTypes = selectRegularFiles(packageSource) { it.extension == KOTLIN_FILE_EXTENSION }
        .flatMap { parseFile(it.asString()) }
    val javaFilesTypes = selectRegularFiles(packageSource) { it.extension == JAVA_FILE_EXTENSION }
        .flatMap { parseJavaFile(it.asString()) }
    return kotlinFilesTypes + javaFilesTypes
}
