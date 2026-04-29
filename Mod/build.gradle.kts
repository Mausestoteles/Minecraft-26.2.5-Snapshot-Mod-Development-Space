import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    `java-library`
}

val modId: String by project
val modGroup: String by project
val modVersion: String by project
val mcVersion: String by project
val mcJavaVersion: String by project

group = modGroup
version = modVersion

// ---------------------------------------------------------------------------
// Pfade zu den Referenzen aus Mc_26.2.5snap
// ---------------------------------------------------------------------------
val mcRoot       = rootDir.resolve("../Mc_26.2.5snap/Minecraft").canonicalFile
val mcReference  = mcRoot.resolve("reference/$mcVersion.jar")
val mcOutputDir  = mcRoot.resolve("compiledjar")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(mcJavaVersion.toInt()))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Minecraft als Compile-Only-Referenz (nicht in die finale Jar packen).
    // Falls die Reference-Jar fehlt, wird das beim Build deutlich gemeldet.
    if (mcReference.exists()) {
        compileOnly(files(mcReference))
    }

    // Optional: Annotations / Logging, die in Minecraft sowieso vorhanden sind
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.slf4j:slf4j-api:2.0.13")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(mcJavaVersion.toInt())
}

tasks.named<Jar>("jar") {
    archiveBaseName.set(modId)
    archiveVersion.set(modVersion)
    manifest {
        attributes(
            "Mod-Id"            to modId,
            "Mod-Version"       to modVersion,
            "Minecraft-Version" to mcVersion,
            "Built-By"          to System.getProperty("user.name"),
            "Build-Jdk"         to System.getProperty("java.version")
        )
    }
}

// Sanity-Check vor dem Build
tasks.register("checkReferenceJar") {
    doFirst {
        if (!mcReference.exists()) {
            throw GradleException(
                "Reference jar not found:\n  $mcReference\n" +
                "Lege die Minecraft-Jar dort ab oder passe mc.version in gradle.properties an."
            )
        }
        logger.lifecycle("Using Minecraft reference: $mcReference")
    }
}
tasks.named("compileJava").configure { dependsOn("checkReferenceJar") }

// Komfort-Task: kopiert die fertige Mod-Jar in compiledjar/
tasks.register<Copy>("installToCompiledJar") {
    dependsOn(tasks.named("jar"))
    from(tasks.named<Jar>("jar").flatMap { it.archiveFile })
    into(mcOutputDir)
    doLast {
        logger.lifecycle("Mod-Jar installiert nach: $mcOutputDir")
    }
}

tasks.named("build").configure { finalizedBy("installToCompiledJar") }

