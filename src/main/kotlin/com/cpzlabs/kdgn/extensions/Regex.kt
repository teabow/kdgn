package com.cpzlabs.kdgn.extensions

private const val withDelimiter = "((?<=%1\$s)|(?=%1\$s))"

fun Regex.splitWithDelimiter(input: CharSequence) =
    Regex(withDelimiter.format(this.pattern)).split(input)
