package com.compiler.server.utils

fun escapeString(string: String): String? =
  when {
    string.isEmpty() -> string
    else -> {
      var resultString = string
      when {
          resultString.contains("<") -> resultString = resultString.replace("<", "&lt;")
          resultString.contains(">") -> resultString = resultString.replace(">", "&gt;")
          resultString.contains("&") -> resultString = resultString.replace("&", "&amp;")
      }
      resultString
    }
  }