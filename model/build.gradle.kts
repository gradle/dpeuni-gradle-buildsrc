plugins {
    id("java-library")
    // Jacoco plugin applied to both subprojects.
    jacoco
}

group = "com.gradle.training"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Common dependencies.
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.jodatime)

    // Dependencies unique to this subproject.
    implementation(libs.apache.commons.io)

    // Test dependencies.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Common Jacoco configuration applied to both subprojects.
tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
    }
}

// Common Jacoco configuration applied to both subprojects.
tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.4".toBigDecimal()
            }
        }
    }
}

// Common Jacoco configuration applied to both subprojects.
tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
}