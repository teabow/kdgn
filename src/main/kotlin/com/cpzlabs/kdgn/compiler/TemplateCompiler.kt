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


private fun buildDefaultTemplateConfig(): TemplateConfig {
    val config = TemplateConfig()
    config.register(Filter("implementing") {
        subject.toDynamicList().filter {
            it is Type && it.implementing?.find { implements -> implements.name == args[0].toDynamicString() } != null
        }
    })
    return config
}

private suspend fun compileTemplate(file: File, types: List<Type>, packageDest: String, config: TemplateConfig) {
    val template = Template(file.asString(), config)
    val rendered = """
            |// Auto-generated file (${Date()}). Do not edit.
            |
            |${template(mapOf(TEMPLATE_TYPES_KEY to types)).trimIndent()}
            """.trimMargin()

    File(packageDest).createDir()
    val generatedFilePath =
        "$packageDest/${file.name.toString().replace(".$TEMPLATE_FILE_EXTENSION", "")}.$KOTLIN_FILE_EXTENSION"

    File(generatedFilePath) writeString rendered
}

private fun compileTemplates(files: List<File>, packageSource: String, packageDest: String) {

    val parsedTypes = parseCode(packageSource)

    files.forEach { file ->
        runBlocking {
            compileTemplate(file, parsedTypes, packageDest, buildDefaultTemplateConfig())
        }
    }
}

fun compileAllTemplates(templateSource: String, packageSource: String, packageDest: String) {

    compileTemplates(selectRegularFiles(templateSource), packageSource, packageDest)
}

fun compileTemplatesList(templates: List<File>, packageSource: String, packageDest: String) {

    compileTemplates(templates, packageSource, packageDest)
}
