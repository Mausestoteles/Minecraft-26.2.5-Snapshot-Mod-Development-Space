# Example Fabric Mod – Minecraft 26.2-snapshot-5

Klassische **Fabric-Mod-Vorlage** mit [Fabric Loom](https://github.com/FabricMC/fabric-loom),
Yarn-Mappings, Mixin-Setup und gesplitteten Common/Client-Source-Sets.

## Voraussetzungen
- **JDK 25**
- Internet-Zugang für den ersten Build (Loom lädt Mappings & Loader)

## Versionen anpassen
Alle Versionen stehen in [`gradle.properties`](gradle.properties).
Aktuelle Werte holst du dir frisch von **https://fabricmc.net/develop**:

```properties
minecraft_version=26.2-snapshot-5
yarn_mappings=26.2-snapshot-5+build.1
loader_version=0.19.2
fabric_api_version=0.147.1+26.2
loom_version=1.16-SNAPSHOT
java_version=25
```

> 📌 Solange für 26.2-snapshot-5 noch **keine** Yarn-Mappings veröffentlicht
> sind, schlägt der erste Build fehl. Dann entweder warten oder vorübergehend
> auf den letzten verfügbaren Snapshot zurückgehen.

## Bauen
```powershell
cd ModFabric
gradle build
```
Die remappte (production-fähige) Jar landet in:
- `ModFabric/build/libs/examplefabric-0.1.0.jar`
- zusätzlich kopiert nach `Mc_26.2.5snap/Minecraft/compiledjar/`

## Im Dev-Client testen
```powershell
gradle runClient    # startet MC mit deinem Mod
gradle runServer    # dedizierter Server
```
IntelliJ erzeugt nach `gradle ideaSyncTask` (oder einfach Gradle-Refresh)
automatisch Run-Configs **Minecraft Client** und **Minecraft Server**.

## Projektstruktur
```
ModFabric/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── src/
    ├── main/                              ← Common-Code (Server + Client)
    │   ├── java/dev/mc26/examplefabric/ExampleFabricMod.java
    │   └── resources/
    │       ├── fabric.mod.json
    │       └── examplefabric.mixins.json
    └── client/                            ← Client-Only-Code
        ├── java/dev/mc26/examplefabric/client/
        │   ├── ExampleFabricClient.java
        │   └── mixin/TitleScreenMixin.java
        └── resources/
            └── examplefabric.client.mixins.json
```

## Was ist drin?
- ✅ **ModInitializer** + **ClientModInitializer** Beispiele
- ✅ **Mixin-Setup** für Common und Client (separate Configs)
- ✅ **Beispiel-Mixin** auf `TitleScreen.init`
- ✅ **Split source sets** (Common/Client) — saubere Trennung von Server-/Client-Code
- ✅ **`processResources` token replacement** in `fabric.mod.json`
- ✅ Auto-Install in `Mc_26.2.5snap/Minecraft/compiledjar/`

## Verwandt
- [`../Mod/`](../Mod) – loaderfreie Java-Agent-Variante derselben Vorlage

