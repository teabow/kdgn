package com.cpzlabs.kdgn.parser

import com.cpzlabs.kdgn.extensions.KOTLIN_FILE_EXTENSION
import com.cpzlabs.kdgn.extensions.asString
import com.cpzlabs.kdgn.extensions.selectRegularFiles


fun parseCode(packageSource: String): List<Type> {
    return selectRegularFiles(packageSource) {
        it.extension == KOTLIN_FILE_EXTENSION
    }.flatMap {
        parseFile(it.asString())
    }
}
