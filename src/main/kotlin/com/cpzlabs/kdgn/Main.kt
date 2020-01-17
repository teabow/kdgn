import com.cpzlabs.kdgn.compiler.compileTemplates
import com.cpzlabs.kdgn.helpers.Logger
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) = runBlocking {
    val argSeparator = "="

    val argsMap = args.fold(mutableMapOf<String, String>()) { acc, current ->
        val (key, value) = current.split(argSeparator)
        acc[key] = value
        acc
    }

    val argsMapValues = listOf(argsMap["templateSource"], argsMap["packageSource"], argsMap["packageDest"])

    if (argsMap.isNotEmpty() && argsMapValues.all { it != null }) {

        compileTemplates(argsMap["templateSource"]!!, argsMap["packageSource"]!!, argsMap["packageDest"]!!)

    } else {
        Logger.logMessage("""
            Please provide args as follow :
            templateSource=path/to/templates packageSource=path/to/src/package packageDest=path/to/src/generated/package
        """.trimIndent(), "Program error")
    }
}
