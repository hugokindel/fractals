plugins {
    id("com.github.johnrengelman.shadow").version("7.1.0")
    java
    application
}

group = "com.ustudents"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val jUnitVersion: String by project
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}

tasks {
    // Force unicode support.
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    // Add JUnit support.
    "test"(Test::class) {
        useJUnitPlatform()
    }

    // Set main class to use after a .jar build.
    jar {
        manifest {
            attributes("Main-Class" to "com.ustudents.fractals.Main")
        }
    }
}

// Set main class to use when launching as an application.
application.mainClass.set("com.ustudents.fractals.Main")

// Changes the standard input (useful because Gradle can hide the input in some cases).
val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}

// List of JVM options to pass.
var jvmOptions = mutableListOf<String>()
// Gradle has an issue supporting unicode within Powershell or cmd.exe,
// you need to use `chcp 65001` to enable unicode characters
// (this is not an issue in distributed builds, only within gradle commands output).
jvmOptions.add("-Dfile.encoding=utf-8")
// Pass an IDE name information to know within the engine's code if we are debugging within an IDE.
if (project.gradle.startParameter.taskNames.contains("run") && System.getProperty("idea.vendor.name") == "JetBrains") {
    jvmOptions.add("-Dide=JetBrains")
}
application.applicationDefaultJvmArgs = jvmOptions

// Set minimal JDK version.
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}