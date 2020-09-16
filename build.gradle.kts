import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by System.getProperties()
val arrowVersion: String by System.getProperties()
val policy: String by System.getProperties()

group = "com.compiler.server"
version = "$kotlinVersion-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val kotlinDependency: Configuration by configurations.creating {
    isTransitive = false
}
val arrowDependency: Configuration by configurations.creating {
    isTransitive = true
}
val libJVMFolder = kotlinVersion
val propertyFile = "application.properties"

val copyDependencies by tasks.creating(Copy::class) {
    from(kotlinDependency)
    into(libJVMFolder)
}

val copyArrowDependencies by tasks.creating(Copy::class) {
    from(arrowDependency)
    into(libJVMFolder)
}

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version System.getProperty("kotlinVersion")
    kotlin("plugin.spring") version System.getProperty("kotlinVersion")
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://cache-redirector.jetbrains.com/kotlin.bintray.com/kotlin-plugin")
    }
    afterEvaluate {
        dependencies {
            implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
        }
    }
}

repositories {
    maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

dependencies {
    kotlinDependency("com.fasterxml.jackson.core:jackson-databind:2.10.0")
    kotlinDependency("com.fasterxml.jackson.core:jackson-core:2.10.0")
    kotlinDependency("com.fasterxml.jackson.core:jackson-annotations:2.10.0")
    // Kotlin libraries
    kotlinDependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    kotlinDependency("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
    kotlinDependency("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    annotationProcessor("org.springframework:spring-context-indexer")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot2:1.5.1")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-compiler:$kotlinVersion")
    implementation(project(":executors", configuration = "default"))

    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.8")

    // Arrow
    arrowDependency("io.arrow-kt:arrow-annotations:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-aql:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-continuations:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-core:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-core-data:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-core-retrofit:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-free:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-free-data:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx-coroutines-kotlinx-coroutines:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx-kotlinx-coroutines:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-fx-mtl:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx-reactor:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-fx-rx2:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-generic:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-integrations-retrofit-adapter:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-kindedj:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-mtl:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-mtl-data:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-optics:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-optics-mtl:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-recursion:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-recursion-data:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-reflect:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-syntax:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-ui:$arrowVersion")
    arrowDependency("io.arrow-kt:arrow-ui-data:$arrowVersion")
    // arrowDependency("io.arrow-kt:arrow-validation:$arrowVersion")

    // For buildLambda
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.1.0")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.2.0")
}

fun buildPropertyFile() {
    rootDir.resolve("src/main/resources/${propertyFile}").apply {
        println("Generate properties into $absolutePath")
        parentFile.mkdirs()
        writeText(generateProperties())
    }
}

fun generateProperties(prefix: String = "") = """
    # this file is autogenerated by build.gradle.kts
    kotlin.version=${kotlinVersion}
    policy.file=${prefix + policy}
    libraries.folder.jvm=${prefix + libJVMFolder}
    arrow.version=${arrowVersion}
""".trimIndent()

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xskip-metadata-version-check")
        jvmTarget = "1.8"
    }
    dependsOn(copyDependencies)
    dependsOn(copyArrowDependencies)
    dependsOn(":executors:jar")
    buildPropertyFile()
}

val buildLambda by tasks.creating(Zip::class) {
    archiveBaseName.set("playground-server")
    archiveVersion.set("$arrowVersion")
    destinationDirectory.set(file("lambdaDistributions"))

    val lambdaWorkDirectoryPath = "/var/task/"
    from(tasks.compileKotlin)
    from(tasks.processResources) {
        eachFile {
            if (name == propertyFile) { file.writeText(generateProperties(lambdaWorkDirectoryPath)) }
        }
    }
    from(policy)
    from(libJVMFolder) { into(libJVMFolder) }
    into("lib") {
        from(configurations.compileClasspath) { exclude("tomcat-embed-*") }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.clean {
    delete(libJVMFolder)
}