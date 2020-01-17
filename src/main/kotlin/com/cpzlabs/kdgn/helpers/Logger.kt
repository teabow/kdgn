package com.cpzlabs.kdgn.helpers

object Logger {
    fun logMessage(message: String, header: String? = null) {
        println("""
        ==========================
        ${header ?: "Log"}
        ------------------
        $message
        ==========================
        
        """.trimIndent())
    }
}
