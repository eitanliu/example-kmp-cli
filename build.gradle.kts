@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

group = "org.example.kmp.cli"
version = "1.0.0"

kotlin {

    jvm {
        binaries {
            executable {
                mainClass.set("MainKt")
            }
            executable(KotlinCompilation.TEST_COMPILATION_NAME) {
                mainClass.set("MainKt")
            }
        }
    }

    val os = OperatingSystem.current()
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val nativeTarget = when {
        os.isLinux -> if (isArm64) linuxArm64() else linuxX64()
        os.isMacOsX -> if (isArm64) macosArm64() else macosArm64()
        os.isWindows -> mingwX64()
        else -> null
    }

    nativeTarget?.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

        all {
            languageSettings.optIn("kotlin.ExperimentalStdlibApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
            // 2.1.2
            languageSettings.optIn("kotlin.concurrent.atomics.ExperimentalAtomicApi")
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")

            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
    }

    compilerOptions {
        // 2.1.0 https://kotlinlang.org/docs/whatsnew21.html#guard-conditions-in-when-with-a-subject
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

repositories {
    mavenCentral()
}
