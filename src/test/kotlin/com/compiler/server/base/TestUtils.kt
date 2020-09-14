package com.compiler.server.base

import com.compiler.server.model.*
import org.junit.jupiter.api.Assertions

internal fun ExecutionResult.assertNoErrors() = errors.assertNoErrors()

internal fun Map<String, List<ErrorDescriptor>>.assertNoErrors() {
    Assertions.assertFalse(hasErrors) {
        "No errors expected, but the following errors were found:\n" +
                "\n" +
          renderErrorDescriptors(filterOnlyErrors)
    }
}