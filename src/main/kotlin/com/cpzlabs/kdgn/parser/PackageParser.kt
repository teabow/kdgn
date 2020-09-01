package com.cpzlabs.kdgn.parser

fun getGeneratedPackage(packageDest: String, suffix: String = ""): String {
    val packagePathRegex = "(?:src/)?(?:.*?)/(?:kotlin|java)/(.*)".toRegex()
    val matchResult = packagePathRegex.find(packageDest)
    return matchResult?.destructured?.let {
        val (packagePath) = it
        val packageName = packagePath.split("/").joinToString(".")
        "package $packageName $suffix".trim()
    } ?: ""
}
