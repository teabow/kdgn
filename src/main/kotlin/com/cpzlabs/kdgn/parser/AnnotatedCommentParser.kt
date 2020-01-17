package com.cpzlabs.kdgn.parser


const val START_ANNOTATED_COMMENT_PATTERN = """//\s*(kdgn:)"""

const val ANNOTATED_COMMENT_REGEX = """(?:(.*?)\s*:)?\s*(.*?)\s*=\s*(.*)"""

fun buildAnnotatedComment(key: String, namespace: String? = null): String {
    return if (namespace != null) {
        "$namespace.$key"
    } else key
}

fun parseAnnotatedComment(comment: String): Map<String, String> {

    if (!comment.trim().matches("""$START_ANNOTATED_COMMENT_PATTERN(.*)""".toRegex())) {
        return emptyMap()
    }

    val cleanedComment = comment.replace(START_ANNOTATED_COMMENT_PATTERN.toRegex(), "").trim()

    var commentNamespace: String? = null

    return cleanedComment.split(",").fold(mutableMapOf()) { acc, current ->

        val matchResult = ANNOTATED_COMMENT_REGEX.toRegex().find(current)

        matchResult?.destructured?.let {
            val (namespace, name, value) = it

            if (namespace.isNotEmpty()) {
                commentNamespace = namespace
            }

            acc[buildAnnotatedComment(name, commentNamespace)] = value.trim()
        }

        acc
    }
}
