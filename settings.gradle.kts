rootProject.name = "kotlin-compiler-server"
include(":executors")
include(":indexation")
include(":common")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}