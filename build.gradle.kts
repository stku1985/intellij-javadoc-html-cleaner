import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

plugins {
    java
    id("org.jetbrains.intellij") version "0.7.2"
    kotlin("jvm") version "1.4.31"
    idea
}

group = "com.github.gindex"
version = "1.1.4"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val ideaVersion: String by project
val ideaType: String by project
val downloadIdeaSources: String by project


intellij {
    version = ideaVersion
    type = ideaType
    pluginName = "JavaDoc HTML Cleaner"
    setPlugins("java")
    downloadSources = downloadIdeaSources.toBoolean()
    sandboxDirectory = project.rootDir.canonicalPath + "/.sandbox"
}
tasks {
    patchPluginXml {
        sinceBuild("201.*")
        untilBuild("211.*")
    }
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}


tasks.withType<KotlinCompile> {
    doFirst {
        System.out.println("Projekt: " + ideaVersion)
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs.plus("-progressive")
    }
}

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = true

        excludeDirs.add(file(intellij.sandboxDirectory))
        excludeDirs.add(file("testData"))
    }
}

defaultTasks("clean", "buildPlugin")
