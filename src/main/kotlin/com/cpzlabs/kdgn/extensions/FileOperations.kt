package com.cpzlabs.kdgn.extensions

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

const val KOTLIN_FILE_EXTENSION = "kt"
const val JAVA_FILE_EXTENSION = "java"

fun File.asString() = try {
    this.readText(charset = Charsets.UTF_8)
} catch (e: IOException) {
    ""
}

infix fun File.writeString(content: String) = try {
    this.writeText(content, charset = Charsets.UTF_8)
} catch (e: IOException) {
    ""
}

fun File.createDir(): Boolean {
    return if (!this.exists()) {
        this.mkdirs()
    } else false
}

fun selectRegularFiles(directory: String, filterPredicate: ((File) -> Boolean)? = null): List<File> {
    return Files.walk(Paths.get(directory))
        .filter { Files.isRegularFile(it) && (filterPredicate?.invoke(it.toFile()) ?: true) }
        .map { it.toFile() }
        .toList()
}
