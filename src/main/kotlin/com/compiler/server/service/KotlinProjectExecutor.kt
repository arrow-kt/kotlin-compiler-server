package com.compiler.server.service

import com.compiler.server.compiler.KotlinFile
import com.compiler.server.compiler.components.KotlinCompiler
import com.compiler.server.compiler.components.KotlinEnvironment
import com.compiler.server.model.ExecutionResult
import com.compiler.server.model.Project
import com.compiler.server.model.bean.VersionInfo
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.springframework.stereotype.Component

@Component
class KotlinProjectExecutor(
        private val kotlinCompiler: KotlinCompiler,
        private val version: VersionInfo,
        private val kotlinEnvironment: KotlinEnvironment
) {

  fun run(project: Project): ExecutionResult {
    return kotlinEnvironment.environment { environment ->
      val files = getFilesFrom(project, environment).map { it.kotlinFile }
      kotlinCompiler.run(files, environment, project.args)
    }
  }

  fun getVersion() = version

  private fun getFilesFrom(project: Project, coreEnvironment: KotlinCoreEnvironment) = project.files.map {
    KotlinFile.from(coreEnvironment.project, it.name, it.text)
  }
}