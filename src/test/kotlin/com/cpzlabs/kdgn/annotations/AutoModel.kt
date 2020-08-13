package com.cpzlabs.kdgn.annotations

import org.jetbrains.kotlin.javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class AutoModel

@Scope
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class AutoModelField(val required: Boolean)
