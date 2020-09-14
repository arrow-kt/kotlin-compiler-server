package com.compiler.server.base

import com.compiler.server.generator.TestProjectRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BaseExecutorTest {
  @Autowired
  private lateinit var testRunner: TestProjectRunner

  fun run(code: String, contains: String, args: String = "") = testRunner.run(code, contains, args)

  fun run(code: List<String>, contains: String) = testRunner.multiRun(code, contains)

  fun runWithException(code: String, contains: String) = testRunner.runWithException(code, contains)

  fun version() = testRunner.getVersion()
}