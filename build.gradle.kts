import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.61"
    `maven-publish`
}

group = "com.cpzlabs"
version = "0.5.0"

object Versions {
    val junit = "4.13"
    val mockk = "1.9"
    val coroutines = "1.3.3"
    val korte = "1.2.1"
    val kastree = "0.4.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://dl.google.com/dl/android/maven2/") }
    jcenter { url = uri("http://jcenter.bintray.com/") }
    maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
    maven {  url = uri("https://dl.bintray.com/hotkeytlt/maven") }
    maven {  url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("junit:junit:${Versions.junit}")
    testImplementation("io.mockk:mockk:${Versions.mockk}")

    implementation("com.soywiz.korlibs.korte:korte-jvm:${Versions.korte}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

    implementation("com.github.cretz.kastree:kastree-ast-jvm:${Versions.kastree}")
    implementation("com.github.cretz.kastree:kastree-ast-psi:${Versions.kastree}")
    implementation("com.github.cretz.kastree:kastree-ast-common:${Versions.kastree}")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/teabow/kdgn")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("kdgn") {
            from(components["java"])
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register<TestReport>("testReport") {
    destinationDir = file("$buildDir/reports/allTests")
    // Include the results from the `test` task in all subprojects
    reportOn(subprojects.map { it.tasks["test"] })
}
