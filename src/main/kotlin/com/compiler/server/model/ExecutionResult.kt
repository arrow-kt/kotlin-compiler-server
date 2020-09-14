package com.compiler.server.model

open class ExecutionResult(
  open var errors: Map<String, List<ErrorDescriptor>> = emptyMap(),
  open var exception: ExceptionDescriptor? = null
) {
  var text: String = ""
    set(value) {
      field = unEscapeOutput(value)
    }

  fun addWarnings(warnings: Map<String, List<ErrorDescriptor>>) {
    errors = warnings
  }
}

private fun unEscapeOutput(value: String) = value.replace("&amp;lt;".toRegex(), "<")
  .replace("&amp;gt;".toRegex(), ">")
  .replace("\r", "")
