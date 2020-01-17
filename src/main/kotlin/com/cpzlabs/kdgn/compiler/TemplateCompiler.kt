package com.cpzlabs.kdgn.compiler

import com.soywiz.korte.Filter
import com.soywiz.korte.Template
import com.soywiz.korte.TemplateConfig
import com.cpzlabs.kdgn.extensions.*
import kotlinx.coroutines.runBlocking
import com.cpzlabs.kdgn.parser.Type
import com.cpzlabs.kdgn.parser.parseCode
import java.io.File
import java.util.*


const val TEMPLATE_TYPES_KEY = "types"
const val TEMPLATE_FILE_EXTENSION = "template"

fun compileTemplates(templateSource: String, packageSource: String, packageDest: String) {

    val config = TemplateConfig()
    config.register(Filter("implementing") {
        subject.toDynamicList().filter {
            it is Type && it.implementing?.find { implements -> implements.name == args[0].toDynamicString() } != null
        }
    })

    selectRegularFiles(templateSource).forEach { file ->
        runBlocking {
            val template = Template(file.asString(), config)
            val rendered = """
            |// Auto-generated file (${Date()}). Do not edit.
            |
            |${template(mapOf(TEMPLATE_TYPES_KEY to parseCode(packageSource))).trimIndent()}
            """.trimMargin()

            File(packageDest).createDir()
            val generatedFilePath =
                "$packageDest/${file.name.toString().replace(".$TEMPLATE_FILE_EXTENSION", "")}.$KOTLIN_FILE_EXTENSION"

            File(generatedFilePath) writeString rendered
        }
    }
}
