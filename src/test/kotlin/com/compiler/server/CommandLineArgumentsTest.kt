package com.compiler.server

import com.compiler.server.base.BaseExecutorTest
import org.junit.jupiter.api.Test

class CommandLineArgumentsTest : BaseExecutorTest() {

  @Test
  fun `command line arguments jvm test`() {
    run(
      code = "fun main(args: Array<String>) {\n    println(args[0])\n    println(args[1])\n}",
      args = "0 1",
      contains = "0\n1\n"
    )
  }

  @Test
  fun `command line string arguments jvm test`() {
    run(
      code = "fun main(args: Array<String>) {\n    println(args[0])\n    println(args[1])\n}",
      args = "alex1 alex2",
      contains = "alex1\nalex2\n"
    )
  }
}