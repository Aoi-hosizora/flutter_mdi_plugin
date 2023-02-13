plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.intellij") version "1.8.0"
}

group = "com.aoihosizora.flutter_mdi_plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set("Flutter MdiIcons Marker")
    version.set("2021.3.3")
    type.set("IC") // Target IDE Platform

    // Plugin Dependencies
    plugins.set(listOf(
        "com.intellij.java",
        "Dart:213.7433", // https://plugins.jetbrains.com/plugin/6351-dart/versions
        "io.flutter:72.0.1", // https://plugins.jetbrains.com/plugin/9212-flutter/versions
    ))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("223.*")
    }

    // signPlugin {
    //     certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    //     privateKey.set(System.getenv("PRIVATE_KEY"))
    //     password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    // }
    //
    // publishPlugin {
    //     token.set(System.getenv("PUBLISH_TOKEN"))
    // }
}
