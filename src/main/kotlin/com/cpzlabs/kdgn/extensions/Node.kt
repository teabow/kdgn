package com.cpzlabs.kdgn.extensions

import kastree.ast.ExtrasMap
import kastree.ast.Node
import com.cpzlabs.kdgn.parser.parseAnnotatedComment


fun Node.getExtrasBefore(extrasMap: ExtrasMap? = null): List<String> {
    return getExtras(extrasMap?.extrasBefore(this))
}

fun Node.getExtras(extras: List<Node.Extra>?): List<String> {
    return extras?.mapNotNull {
        if (it is Node.Extra.Comment) {
            it.text
        } else null
    } ?: emptyList()
}

fun Node.getNodeComments(extrasMap: ExtrasMap? = null): Map<String, String> {
    return parseAnnotatedComment(this.getExtrasBefore(extrasMap).joinToString())
}
