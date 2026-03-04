import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("com.github.gmazzo.buildconfig") version "5.5.0"
}

buildConfig {
    packageName("io.github.b4official.mail")
    buildConfigField("String", "BACKEND_BASE_URL", "\"http://localhost:8080\"")
}

kotlin {
    jvm("desktop")

    repositories {
        google()
        mavenCentral()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.kotlin.logging)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.cio)
            implementation(libs.slf4j.simple)
        }
    }
}


compose.desktop {
    application {
        mainClass = "io.github.b4official.mail.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.b4official.mail"
            packageVersion = "1.0.0"
        }
    }
}
