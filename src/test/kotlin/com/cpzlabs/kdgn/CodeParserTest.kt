package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.models.Member
import com.cpzlabs.kdgn.models.Method
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
                members = emptyList(),
                methods = emptyList()
            ),
            Type(
                name = "AutoPersistable",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = emptyList(),
                methods = emptyList()
            ),
            Type(
                name = "UserMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "UserSpec"), Type(name = "AutoPersistable")),
                members = listOf(
                    Member(name = "lastname", type = "String", annotations = mapOf("persistence.defaultValue" to "\"2\"")),
                    Member(name = "firstname", type = "String", annotations = mapOf("persistence.defaultValue" to "\"3\""))
                ),
                methods = emptyList(),
                annotations = mapOf("persistence.defaultValue" to "\"1\"")
            ),
            Type(
                name = "ContactMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(Member(name = "phone", type = "String"), Member(name = "mail", type = "String")),
                methods = emptyList()
            ),
            Type(
                name = "Academy",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Member(name = "country", type = "String")),
                methods = emptyList()
            ),
            Type(
                name = "University",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = emptyList(),
                methods = listOf(Method(name = "register", returnType = "String"))
            ),
            Type(
                name = "Project",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(
                    Member(name = "projectId", type = "Int", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "projectName", type = "String")
                ),
                methods = listOf(Method(name = "startProject"), Method(name = "endProject")),
                annotations = mapOf("AutoMap" to "")
            ),
            Type(
                name = "AddressMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = listOf(Type(name = "Serializable")),
                members = listOf(
                    Member(name = "street", type = "String", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "city", type = "String", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "zipCode", type = "String", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "country", type = "String", annotations = mapOf("AutoModelField" to "required = false"))
                ),
                methods = listOf(
                  Method(name = "getStreet", returnType = "String"),
                  Method(name = "setStreet", returnType = "void"),
                  Method(name = "getCity", returnType = "String"),
                  Method(name = "setCity", returnType = "void"),
                  Method(name = "getZipCode", returnType = "String"),
                  Method(name = "setZipCode", returnType = "void"),
                  Method(name = "getCountry", returnType = "String"),
                  Method(name = "setCountry", returnType = "void")
                ),
                annotations = mapOf("AutoMap" to "", "AutoModel" to "")
            ),
            Type(
                name = "OrderMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(
                    Member(name = "orderId", type = "String", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "customerId", type = "String", annotations = mapOf("AutoModelField" to "required = true")),
                    Member(name = "productsId", type = "List<ProductMock>", annotations = mapOf("AutoModelField" to "required = true"))
                ),
                methods = listOf(
                    Method(name = "getOrderId", returnType = "String"),
                    Method(name = "setOrderId", returnType = "void"),
                    Method(name = "getCustomerId", returnType = "String"),
                    Method(name = "setCustomerId", returnType = "void"),
                    Method(name = "getProductsId", returnType = "List<ProductMock>"),
                    Method(name = "setProductsId", returnType = "void")
                ),
                annotations = mapOf("AutoMap" to "", "AutoModel" to "")
            ),
            Type(
                name = "ProductMock",
                packageName = "com.cpzlabs.kdgn.mocks",
                implementing = emptyList(),
                members = listOf(
                    Member(name = "productId", type = "String")
                ),
                methods = listOf(Method(name = "getProductId", returnType = "String"))
            )
        )

        assertEquals(expected.sortedBy { it.name }, types.sortedBy { it.name })
    }

}
