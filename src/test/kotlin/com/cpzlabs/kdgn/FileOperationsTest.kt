package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.extensions.asString
import com.cpzlabs.kdgn.extensions.createDir
import com.cpzlabs.kdgn.extensions.selectRegularFiles
import com.cpzlabs.kdgn.extensions.writeString
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class FileOperationsTest {

    private val filesResPath = "src/test/resources"
    private val filesMocksPath = "src/test/resources/mocks"

    private val testFile = File("$filesResPath/test.txt")
    private val testDir = File("$filesMocksPath/dir_test")

    @Before
    fun setup() {
        testFile.createNewFile()
        if (testDir.exists()) {
            testDir.delete()
        }
    }

    @After
    fun tearDown() {
        testFile.delete()
    }

    @Test
    fun fileAsStringTest() {
        testFile.writeText("test_1", Charsets.UTF_8)
        assertEquals("test_1", testFile.asString())
    }

    @Test
    fun fileWriteStringTest() {
        testFile writeString "test_2"
        assertEquals("test_2", testFile.asString())
    }

    @Test
    fun createDirTest() {
        val dirNotExists = File("$filesMocksPath/dir_test")
        val dirExists = File("$filesMocksPath/mock1")
        assertEquals(dirNotExists.createDir(), true)
        assertEquals(dirExists.createDir(), false)
    }

    @Test
    fun selectRegularFilesTest() {
        val filesNames = selectRegularFiles(filesMocksPath).map { it.name }
        assertEquals(listOf("test1.txt", "test2.txt"), filesNames)
    }
}
