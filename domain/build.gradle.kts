plugins {
    kotlin("jvm")
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
}