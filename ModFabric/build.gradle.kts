import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
    `maven-publish`
}

val modId: String by project
val modGroup: String by project
val modVersion: String by project

val mcVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricApiVersion: String by project
val mcJavaVersion: String by project

group = modGroup
version = modVersion

base {
    archivesName.set(modId)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(mcJavaVersion.toInt()))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" } // optional (ModMenu etc.)
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    // Fabric API – auskommentieren falls du sie nicht brauchst
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "run"
    }
    splitEnvironmentSourceSets()

    mods {
        create(modId) {
            sourceSet("main")
            sourceSet("client")
        }
    }
}

tasks.processResources {
    val tokens = mapOf(
        "version"        to modVersion,
        "mc_version"     to mcVersion,
        "loader_version" to loaderVersion
    )
    inputs.properties(tokens)
    filesMatching("fabric.mod.json") {
        expand(tokens)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(mcJavaVersion.toInt())
}

tasks.jar {
    from("LICENSE") { rename { "${it}_${modId}" } }
    manifest {
        attributes(
            "Mod-Id"            to modId,
            "Mod-Version"       to modVersion,
            "Minecraft-Version" to mcVersion
        )
    }
}

// Fertige (gemappte) Jar zusätzlich ins gemeinsame compiledjar/ kopieren
val compiledOut = rootDir.resolve("../Mc_26.2.5snap/Minecraft/compiledjar").canonicalFile
tasks.register<Copy>("installToCompiledJar") {
    dependsOn(tasks.named("remapJar"))
    from(tasks.named("remapJar"))
    into(compiledOut)
    doLast { logger.lifecycle("Fabric-Mod-Jar installiert nach: $compiledOut") }
}
tasks.named("build").configure { finalizedBy("installToCompiledJar") }

