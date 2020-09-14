# Kotlin compiler server
[![Kotlin](https://img.shields.io/badge/Kotlin-1.4-orange.svg) ](https://kotlinlang.org/) 
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

A REST server for compiling and executing Kotlin code.
The server provides the API for [Kotlin Playground](https://github.com/arrow-kt/arrow-playground) component:

```html
<script
    src="https://unpkg.com/arrow-playground@1"
    data-selector="[class^='language-kotlin']"
    data-server="http://localhost:8080">
</script>
```

## How to start :checkered_flag:

### Simple Spring Boot application

```shell script
$ ./gradlew bootRun
```

### From Amazon lambda

Based on [aws-serverless-container](https://github.com/awslabs/aws-serverless-java-container).

```shell script
$ ./gradlew buildLambda -DarrowVersion=<version>
```

Getting `.zip` file from `lambdaDistributions/`.

Lambda handler: `com.compiler.server.lambdas.StreamLambdaHandler::handleRequest`.

Publish your Lambda function: you can follow the instructions in [AWS Lambda's documentation](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java-how-to-create-deployment-package.html) on how to package your function for deployment.

## API Documentation :page_with_curl:

### Execute Kotlin code on JVM

```shell script
curl -X POST \
  http://localhost:8080/api/compiler/run \
  -H 'Content-Type: application/json' \
  -d '{
    "args": "1 2 3",
    "files": [
        {
            "name": "File.kt",
            "text": "fun main(args: Array<String>) {\n    println(\"123\")\n}"
        }
    ]
}'
```

### Get the current version

```shell script
curl -X GET http://localhost:8080/versions
curl -X GET http://localhost:8080/kotlinServer?type=getKotlinVersions # same request as before
curl -X GET http://localhost:8080/kotlinServer?type=getArrowVersions # used by arrow-playground component
```

## How to add your dependencies to kotlin compiler :books:

Just put whatever you need as dependencies to [build.gradle.kts](https://github.com/arrow-kt/kotlin-compiler-server/blob/arrow/build.gradle.kts) via a task called `arrowDependency`:

```
 arrowDependency "your dependency"
```

NOTE: If the library you're adding uses reflection, accesses the file system, or performs any other type of security-sensitive operations, don't forget to
configure the [executors.policy](https://github.com/AlexanderPrednota/kotlin-compiler-server/blob/master/executors.policy). [Click here](https://docs.oracle.com/javase/7/docs/technotes/guides/security/PolicyFiles.html) for more information about *Java Security Policy*.

**How to set Java Security Policy in `executors.policy`**

If you want to configure a custom dependency, use the marker `@LIB_DIR@`:

```
grant codeBase "file:%%LIB_DIR%%/junit-4.12.jar"{
  permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
  permission java.lang.RuntimePermission "setIO";
  permission java.io.FilePermission "<<ALL FILES>>", "read";
  permission java.lang.RuntimePermission "accessDeclaredMembers";
};
```

## CORS configuration

Set the environment variables

| ENV                | Default value        |
| -------------------|----------------------|
| ACCESS_CONTROL_ALLOW_ORIGIN_VALUE| *|
| ACCESS_CONTROL_ALLOW_HEADER_VALUE| *|

## Release guide :rocket:

(WIP)
