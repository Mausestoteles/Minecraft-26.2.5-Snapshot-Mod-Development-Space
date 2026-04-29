pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        gradlePluginPortal()
        mavenCentral()
    }
    val loom_version: String by settings
    plugins {
        id("fabric-loom") version loom_version
    }
}

rootProject.name = "mc26-2-5-fabric-mod"

