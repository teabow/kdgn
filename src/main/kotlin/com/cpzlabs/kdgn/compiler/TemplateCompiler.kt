package com.cpzlabs.kdgn.compiler

import com.cpzlabs.kdgn.extensions.*
import com.cpzlabs.kdgn.models.Type
import com.cpzlabs.kdgn.parser.generateImports
import com.cpzlabs.kdgn.parser.getGeneratedPackage
import com.cpzlabs.kdgn.parser.parseCode
import com.soywiz.korte.Filter
import com.soywiz.korte.Template
import com.soywiz.korte.TemplateConfig
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*


const val TEMPLATE_TYPES_KEY = "types"
const val TEMPLATE_FILE_EXTENSION = "template"
const val SEMI_COLON = ";"

internal val RENDERED_SPLIT_MARKERS = listOf("public class", "public interface", "public enum")

private fun buildDefaultTemplateConfig(): TemplateConfig {
    val config = TemplateConfig()
    config.register(Filter("implementing") {
        subject.toDynamicList().filter {
            it is Type && it.implementing?.find { implements -> implements.name == args[0].toDynamicString() } != null
        }
    })
    config.register(Filter("annotatedWith") {
        subject.toDynamicList().filter {
            it is Type && it.annotations.keys.find { annotated -> annotated == args[0].toDynamicString() } != null
        }
    })
    config.register(Filter("capital") {
        subject.toString().capitalize()
    })
    return config
}

private suspend fun compileTemplate(file: File, types: List<Type>, packageDest: String, config: TemplateConfig) {
    val template = Template(file.asString(), config)
    val lineSuffix = if (file.path.contains(".$JAVA_FILE_EXTENSION")) SEMI_COLON else ""

    File(packageDest).createDir()

    val renderedHeader = """
        // Auto-generated file (${Date()}). Do not edit.
        |${getGeneratedPackage(packageDest, lineSuffix)}
        |
    """.trimIndent()

    val splitChunkRegex = RENDERED_SPLIT_MARKERS.joinToString("|", prefix = "(?=(", postfix = "))").toRegex()

    val rendered = template(mapOf(TEMPLATE_TYPES_KEY to types))
    val renderedChunks = splitChunkRegex.splitWithDelimiter(rendered)

    if (renderedChunks.size == 1) {
        val generatedFilePath =
            "$packageDest${File.separator}${file.name.toString().replace(".$TEMPLATE_FILE_EXTENSION", "")}"

        File(generatedFilePath) writeString """
            |${renderedHeader.trimIndent()}
            |${generateImports(rendered, types, lineSuffix).trimIndent()}
            |${rendered.trimIndent()}
        """.trimMargin()

    } else {
        renderedChunks.forEach { chunk ->
            val matchResult = "(${RENDERED_SPLIT_MARKERS.joinToString("|")})\\s+(.*)\\s*\\{".toRegex().find(chunk)

            matchResult?.destructured?.let {
                val (_, entityName) = it

                val generatedFilePath =
                    "$packageDest${File.separator}${entityName.trim()}.${file.name.replace(
                        ".$TEMPLATE_FILE_EXTENSION",
                        ""
                    ).getFileExtension()}"

                File(generatedFilePath) writeString """
                |${renderedHeader.trimIndent()}
                |${generateImports(chunk, types, lineSuffix).trimIndent()}
                |${chunk.trimIndent()}
            """.trimMargin()
            }
        }
    }
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
