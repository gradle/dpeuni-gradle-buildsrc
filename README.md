# DPE University Training

<p align="left">
<img width="10%" height="10%" src="https://user-images.githubusercontent.com/120980/174325546-8558160b-7f16-42cb-af0f-511849f22ebc.png">
</p>

## Gradle Shared Build Logic with buildSrc Exercise

This is a hands-on exercise to go along with the
[Gradle Shared Build Logic](https://dpeuniversity.gradle.com/app/catalog)
training module. In this exercise you will go over the following:

* Creating structure for buildSrc
* Creating convention plugins
* Using the version catalog in a convention plugin

---
## Prerequisites

* Finished going through the relevant sections in the training course
* JDK 1.8+ and recent version of Gradle build tool installed
    * https://gradle.org/install/
* Gradle Build Tool experience
    * Knowledge of core concepts
    * Authoring build files
    * Kotlin experience a plus but not required
* Basic experience with Java software development

---
## Creating Structure for buildSrc

1. Open the Gradle project in this repository in an editor of your choice
2. In the root directory, create a directory called *buildSrc*
3. Create a *build.gradle.kts* file in there
4. Refer to the [Gradle docs](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html#header)
on what contents should go in here
5. Create the sub-directories *src/main/kotlin* in buildSrc
6. Refresh the Gradle configs in the editor

---
## Creating a Convention Plugin

1. Inspect the build files for *app* and *model*
2. Notice the *jacoco* plugin and jacoco configuration is applied to both
3. Run the *check* task, the jacoco verification task should run for both subprojects
4. Create a file called *test-coverage-conventions.gradle.kts* under `buildSrc/src/main/kotlin`
5. In there, the *jacoco* plugin will be applied, as well as the *java* plugin since jacoco requires that

```kotlin
plugins {
    java
    jacoco
}
```

6. Put the common jacoco configuration in here and remove it from the 2 subproject build files

```kotlin
tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.5".toBigDecimal()
            }
        }
    }
}

tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
}
```

7. Apply the convention plugin in both subprojects

```kotlin
plugins {
    // other plugins in subproject
    id("test-coverage-conventions")
}
```

8. Refresh the Gradle configs in the editor
9. Run the *check* task, the jacoco verification task should still run for both subprojects

---
## Using the Version Catalog

1. Inspect the build files for *app* and *model*
2. Notice there are 3 common dependencies in both subprojects
3. Run `./gradlew :app:dep --configuration compileClasspath` and `./gradlew :model:dep --configuration compileClasspath`,
you will see the 3 common dependencies show up in the compileClasspath
4. In the *buildSrc* directory, create a *settings.gradle.kts* file
5. Refer to the [Gradle docs](https://docs.gradle.org/current/userguide/version_catalogs.html#sec:buildsrc-version-catalog)
on what contents to put in this settings file so it has a reference to the version catalog
6. Refresh the Gradle configs in the editor
7. Create a file called *core-dependencies-conventions.gradle.kts* under `buildSrc/src/main/kotlin`
8. In there put the following contents

```kotlin
plugins {
    java
}

dependencies {
}
```

9. Reference the [Gradle docs](https://docs.gradle.org/current/userguide/version_catalogs.html#sec:buildsrc-version-catalog)
again to get the following line and put it above the `dependencies` block

```kotlin
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
```

10. Reference the same Gradle docs to use the `libs` variable to declare the 3 common dependencies using
`libs.findLibrary`

```kotlin
plugins {
    java
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
dependencies {
  implementation(libs.findLibrary("guava").get())
  // Write the other 2 dependencies
}
```

11. Delete the 3 common dependencies from the subproject build files
12. Apply this convention plugin in the subprojects

```kotlin
plugins {
    // other plugins in subproject
    id("core-dependencies-conventions")
}
```

13. Refresh the Gradle configs in the editor
14. Run `./gradlew :app:dep --configuration compileClasspath` and `./gradlew :model:dep --configuration compileClasspath`,
you will see the 3 common dependencies show up in the compileClasspath