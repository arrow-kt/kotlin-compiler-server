plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlin.compiler)
    implementation(libs.kotlin.gradle.plugin.idea)
    implementation(libs.kotlin.base.fe10.analysis)
}
