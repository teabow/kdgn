package com.cpzlabs.kdgn

import org.junit.Assert.assertEquals
import org.junit.Test
import com.cpzlabs.kdgn.parser.parseAnnotatedComment

class AnnotatedCommentParserTest {

    @Test
    fun parseAnnotatedCommentTest() {
        val comment = "// kdgn: defaultValue = \"\""

        assertEquals(mapOf("defaultValue" to "\"\""), parseAnnotatedComment(comment))
    }

    @Test
    fun parseAnnotatedCommentWithNamespaceTest() {
        val comment = "//  kdgn:persistence: defaultValue = \"\", parser = .intValue"

        assertEquals(
            mapOf(
                "persistence.defaultValue" to "\"\"",
                "persistence.parser" to ".intValue"
            ),
            parseAnnotatedComment(comment)
        )
    }

}
