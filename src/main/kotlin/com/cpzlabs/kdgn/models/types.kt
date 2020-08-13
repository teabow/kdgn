package com.cpzlabs.kdgn.models

sealed class AbstractType(
    open val name: String,
    open var annotations: Map<String, String> = emptyMap()
)

data class Type(
    override val name: String,
    val packageName: String? = null,
    val implementing: List<Type>? = null,
    val inheriting: List<Type>? = null,
    var members: List<Member>? = null,
    override var annotations: Map<String, String> = emptyMap()
) : AbstractType(name, annotations)

data class Member(
    override val name: String,
    val type: String? = null,
    override var annotations: Map<String, String> = emptyMap()
) : AbstractType(name, annotations)
