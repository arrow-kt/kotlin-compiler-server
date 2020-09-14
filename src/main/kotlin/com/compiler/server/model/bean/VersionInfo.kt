package com.compiler.server.model.bean

data class VersionInfo(
  val version: String,
  val stdlibVersion: String,
  val latestStable: Boolean = true,
  val arrowVersion: String
)

data class ArrowVersionInfo(
  val version: String,
  val supportedKotlinVersions: List<String>,
  val latestStable: Boolean = true
)