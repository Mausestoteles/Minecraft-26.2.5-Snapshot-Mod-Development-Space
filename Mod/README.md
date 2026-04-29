# Example Mod – Minecraft 26.2-snapshot-5

Reine Java-Mod-Vorlage. Nutzt die entpackten Minecraft-Klassen aus
`../Mc_26.2.5snap/Minecraft/reference/` als **compile-only** Dependency.

## Voraussetzungen
- JDK **25** (passend zu `java-runtime-epsilon`, siehe `version.json`)
- Gradle 8.10+ (oder Wrapper – siehe unten)

## Projektstruktur
```
Mod/
├── build.gradle.kts          # Gradle-Build, bindet die Reference-Jar ein
├── settings.gradle.kts
├── gradle.properties         # mod.id, version, mc.version, java-version
├── src/main/java/
│   └── dev/mc26/examplemod/ExampleMod.java
└── src/main/resources/
    ├── mod.json              # eigenes Mod-Manifest
    └── META-INF/MANIFEST.MF  # macht die Jar Java-Agent-fähig
```

## Bauen
```powershell
cd Mod
gradle build           # baut + kopiert die Jar nach ../Mc_26.2.5snap/Minecraft/compiledjar/
```

Die fertige Mod-Jar landet in:
`Mc_26.2.5snap/Minecraft/compiledjar/examplemod-0.1.0.jar`

## Wie wird die Mod geladen?
Da 26.2-snapshot-5 (noch) keinen Loader wie Fabric/NeoForge hat, drei Optionen:

1. **Java-Agent** (am saubersten, kein Patchen der MC-Jar nötig):
   ```
   java -javaagent:compiledjar/examplemod-0.1.0.jar -jar reference/26.2-snapshot-5.jar
   ```
   `ExampleMod.premain(...)` wird automatisch aufgerufen.

2. **Classpath-Injection** + Aufruf aus eigenem Launcher:
   ```
   java -cp "reference/26.2-snapshot-5.jar;compiledjar/examplemod-0.1.0.jar" \
        dev.mc26.examplemod.Bootstrap
   ```

3. **Bytecode-Patch** der MC-Main-Klasse (z. B. via ASM im Agent), um
   `ExampleMod.init()` direkt nach `Minecraft.<init>` einzuhängen.

## Referenzen erkunden
Alle dekompilierbaren Klassen liegen unter:
```
Mc_26.2.5snap/Minecraft/reference/
├── net/minecraft/...        # Spielcode
├── com/mojang/blaze3d/...   # Render-Engine (OpenGL/Vulkan/Shader)
├── com/mojang/math/...
└── com/mojang/realmsclient/...
```
IntelliJ erkennt sie automatisch über die `compileOnly(files(...))`-Dependency
in `build.gradle.kts` – Auto-Complete & Go-to-Definition funktionieren.

