package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.models.Member
import com.cpzlabs.kdgn.models.Type
import com.cpzlabs.kdgn.parser.parseCode
import org.junit.Assert.assertEquals
import org.junit.Test

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
                    Member(name = "lastname", annotations = mapOf("persistence.defaultValue" to "\"2\"")),
                    Member(name = "firstname", annotations = mapOf("persistence.defaultValue" to "\"3\""))
                ),
                annotations = mapOf("persistence.defaultValue" to "\"1\"")
            ),
            Type(
                name = "ContactMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(Member(name = "phone"), Member(name = "mail"))
            ),
            Type(
                name = "Academy",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Member(name = "country"))
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
                members = listOf(Member(name = "projectId"), Member(name = "projectName"))
            ),
            Type(
                name = "AddressMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "Serializable")),
                members = listOf(
                    Member(name = "street", type = "String"),
                    Member(name = "city", type = "String"),
                    Member(name = "zipCode", type = "String"),
                    Member(name = "country", type = "String", annotations = mapOf("AutoModelField" to "[required = false]"))
                ),
                annotations = mapOf("AutoModel" to "")
            ),
            Type(
                name = "OrderMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(
                    Member(name = "orderId", type = "String"),
                    Member(name = "customerId", type = "String"),
                    Member(name = "productsId", type = "List<String>")
                )
            )
        )

        assertEquals(expected.sortedBy { it.name }, types.sortedBy { it.name })
    }

}
