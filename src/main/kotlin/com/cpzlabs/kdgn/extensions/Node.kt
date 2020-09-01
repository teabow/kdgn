package com.cpzlabs.kdgn.extensions

import com.cpzlabs.kdgn.parser.parseAnnotatedComment
import kastree.ast.ExtrasMap
import kastree.ast.Node


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

private fun Node.TypeRef.parseSimpleTypeRef() = (this as? Node.TypeRef.Simple)?.pieces?.first()?.name

fun Node.Decl.Func.getReturnType() = this.type?.ref?.parseSimpleTypeRef()
fun Node.Decl.Func.Param.getDeclaredType() = this.type?.ref?.parseSimpleTypeRef()
fun Node.Decl.Property.Var.getDeclaredType() = this.type?.ref?.parseSimpleTypeRef()
