package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.models.Member
import com.cpzlabs.kdgn.models.Type
import kastree.ast.Node
import org.junit.Assert.assertEquals
import org.junit.Test
import com.cpzlabs.kdgn.parser.isPropertyVisible
import com.cpzlabs.kdgn.parser.parseKtFile

class TypesParserTest {

    private fun buildProperty(isVisible: Boolean) = Node.Decl.Property(
        mods = listOf(Node.Modifier.Lit(keyword = if (isVisible) Node.Modifier.Keyword.PUBLIC else Node.Modifier.Keyword.PRIVATE)),
        readOnly = false,
        typeParams = listOf(Node.TypeParam(mods = listOf(Node.Modifier.Lit(keyword = Node.Modifier.Keyword.PRIVATE)), name = "", type = null)),
        receiverType = null,
        vars = emptyList(),
        typeConstraints = emptyList(),
        delegated = false,
        expr = null,
        accessors = null
    )

    @Test
    fun isPropertyVisibleTest() {
        val visibleProp = buildProperty(true)
        val invisibleProp = buildProperty(false)

        assertEquals(true, isPropertyVisible(visibleProp))
        assertEquals(false, isPropertyVisible(invisibleProp))
    }

    @Test
    fun parseFileTest() {
        val code = """
            package test
            
            object Academy: AutoPersistable {
                var country: String = ""
            }

            // kdgn: sourceable = true
            class University(val id: Int, val name: String, region: String): AutoPersistable
        """.trimIndent()

        val types = parseKtFile(code)

        val expected = listOf(
            Type(
                name = "Academy",
                packageName = "test",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Member(name = "country", type = "String")),
                methods = emptyList()
            ),
            Type(
                name = "University",
                packageName = "test",
                implementing = listOf(Type(name = "AutoPersistable")),
                members = listOf(Member(name = "id", type = "Int"), Member(name = "name", type = "String")),
                methods = emptyList(),
                annotations = mapOf("sourceable" to "true")
            )
        )

        assertEquals(expected, types)
    }

}
