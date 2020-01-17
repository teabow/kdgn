package com.cpzlabs.kdgn

import org.junit.Assert.assertEquals
import org.junit.Test
import com.cpzlabs.kdgn.parser.Type
import com.cpzlabs.kdgn.parser.parseCode

class CodeParserTest {

    @Test
    fun parseCodeTest() {
        val types = parseCode("src/test/kotlin/com/cpzlabs/kdgn/mocks")

        val expected = listOf(
            Type(
                name = "UserSpec",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = emptyList()
            ),
            Type(
                name = "AutoPersistable",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = emptyList()
            ),
            Type(
                name = "UserMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "UserSpec"), Type(name = "AutoPersistable")),
                members = listOf(
                    Type(name = "lastname", annotations = mapOf("persistence.defaultValue" to "\"2\"")),
                    Type(name = "firstname", annotations = mapOf("persistence.defaultValue" to "\"3\""))
                    ),
                annotations = mapOf("persistence.defaultValue" to "\"1\"")
            ),
            Type(
                name = "ContactMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(Type(name = "phone"), Type(name = "mail"))
            ),
            Type(
                name = "Academy",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Type(name = "country"))
            ),
            Type(
                name = "University",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = emptyList()
            ),
            Type(
                name = "Project",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Type(name = "projectId"), Type(name = "projectName"))
            )
        )

        assertEquals(expected, types)
    }

}
