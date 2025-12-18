import org.gradle.kotlin.dsl.annotationProcessor

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.h2database:h2:2.4.240")
    implementation("org.projectlombok:lombok:1.18.42")
    implementation("com.sparkjava:spark-core:2.9.4")

    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("io.rest-assured:rest-assured:6.0.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
    testImplementation("org.wiremock:wiremock-standalone:3.13.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks.test {
    useJUnitPlatform()
}