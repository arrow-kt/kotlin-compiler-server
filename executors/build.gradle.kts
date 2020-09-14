val kotlinVersion: String by System.getProperties()

plugins {
  kotlin("jvm")
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<Jar>().getByName("jar") {
  destinationDirectory.set(File("../$kotlinVersion"))
}