package com.cpzlabs.kdgn

import com.cpzlabs.kdgn.parser.getGeneratedPackage
import org.junit.Assert.assertEquals
import org.junit.Test

class PackageParserTest {

    @Test
    fun getGeneratedPackageTest() {
        assertEquals(
            "package com.cpzlabs.kdgn.generated",
            getGeneratedPackage("src/test/kotlin/com/cpzlabs/kdgn/generated")
        )

        assertEquals(
            "package com.cpzlabs.kdgn.generated",
            getGeneratedPackage("test/java/com/cpzlabs/kdgn/generated")
        )

        assertEquals(
            "",
            getGeneratedPackage("test/path/com/cpzlabs/kdgn/generated")
        )
    }

}
