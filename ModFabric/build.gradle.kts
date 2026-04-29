import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("fabric-loom")
    `maven-publish`
}

val mod_id: String by project
val mod_group: String by project
val mod_version: String by project

val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_api_version: String by project
val java_version: String by project

group = mod_group
version = mod_version

base {
    archivesName.set(mod_id)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" } // optional (ModMenu etc.)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("net.fabricmc:yarn:$yarn_mappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loader_version")

    // Fabric API – auskommentieren falls nicht benötigt
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "run"
    }
    splitEnvironmentSourceSets()

    mods {
        create(mod_id) {
            sourceSet("main")
            sourceSet("client")
        }
    }
}

tasks.processResources {
    val tokens = mapOf(
        "version"        to mod_version,
        "mc_version"     to minecraft_version,
        "loader_version" to loader_version
    )
    inputs.properties(tokens)
    filesMatching("fabric.mod.json") {
        expand(tokens)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(java_version.toInt())
}

tasks.jar {
    from("LICENSE") { rename { "${it}_$mod_id" } }
    manifest {
        attributes(
            "Mod-Id"            to mod_id,
            "Mod-Version"       to mod_version,
            "Minecraft-Version" to minecraft_version
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

