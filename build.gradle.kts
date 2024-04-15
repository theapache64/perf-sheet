plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("kapt") version "1.9.0" // TODO: Use single version number
    application
}

group = "io.github.theapache64.perfsheet"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    } // Add jitpack
}

dependencies {

    // Cyclone
    implementation("com.github.theapache64:cyclone:1.0.0-alpha02")

    // Dagger : A fast dependency injector for Android and Java.
    val daggerVersion = "2.51.1"
    implementation("com.google.dagger:dagger:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kaptTest("com.google.dagger:dagger-compiler:$daggerVersion")

    implementation(project(":trace-parser"))

    implementation("de.siegmar:fastcsv:3.1.0")

    // log4j - to fix console warning
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")

    // apache poi for excel manipulation
    val poiVersion = "5.2.4"
    implementation("org.apache.poi:poi:$poiVersion")
    implementation("org.apache.poi:poi-ooxml:$poiVersion")

    // Test deps
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.10")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("io.github.theapache64.perfsheet.app.AppKt")
}