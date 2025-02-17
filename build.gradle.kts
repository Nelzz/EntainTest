import java.time.Year


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.hilt) apply false
}


subprojects {

    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/generated/**")
            ktfmt("0.51").googleStyle().configure {
                it.setRemoveUnusedImports(true)
                it.setManageTrailingCommas(false)
            }
            licenseHeader("/* (C) ${Year.now()}. All rights reserved. */")
        }
    }

    detekt {
        toolVersion = "1.23.7"
        config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        ignoreFailures = false // Fail the build if issues are found
        parallel = true // Run detekt in parallel for performance
    }


}
