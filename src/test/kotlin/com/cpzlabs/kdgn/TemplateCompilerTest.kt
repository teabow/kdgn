package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.compiler.compileAllTemplates
import com.cpzlabs.kdgn.extensions.asString
import io.mockk.every
import io.mockk.mockkConstructor
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.util.*

class TemplateCompilerTest {

    @Test
    fun compileTemplatesTest() {
        mockkConstructor(Date::class)
        every { anyConstructed<Date>().toString() } returns "TODAY"

        val templateSource = "src/test/resources/templates"
        val packageSource = "src/test/kotlin/com/cpzlabs/kdgn/mocks"

        val packageDist = "src/test/kotlin/com/cpzlabs/kdgn/generated"

        compileAllTemplates(templateSource, packageSource, packageDist)

        val generatedFile = File("$packageDist/AutoPersistable.kt")

        val expected = """
            // Auto-generated file (TODAY). Do not edit.
            package com.cpzlabs.kdgn.generated
            import com.cpzlabs.kdgn.mocks.UserMock
            import com.cpzlabs.kdgn.mocks.Academy
            import com.cpzlabs.kdgn.mocks.University
            import com.cpzlabs.kdgn.mocks.Project
            fun UserMock.persist() {
                // lastname = "2"
                // firstname = "3"
            }
            fun Academy.persist() {
                // country =
            }
            fun University.persist() {
            }
            fun Project.persist() {
                // projectId =
                // projectName =
            }
        """.trimIndent()

        assert(generatedFile.exists())
        assertEquals(
            expected.trim(),
            generatedFile.asString().replace("((\\s*?)?[\\r\\n])+".toRegex(), "\n").trim()
        )
    }
}
