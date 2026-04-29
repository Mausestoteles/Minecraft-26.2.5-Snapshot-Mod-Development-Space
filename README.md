# MC 26.2-snapshot-5 Dev-Workspace

> **Öffentliche Vorlage** zum Entwickeln von Mods gegen **Minecraft 26.2 Snapshot 5**
> (Java 25 / `java-runtime-epsilon`, Protocol `1073742135`, World-Version `4889`).
>
> Lizenz: siehe [Hinweise zu Minecraft-Inhalten](#-rechtliches).
> Die **Vorlage selbst** (Build-Skripte, Struktur, Beispielcode) ist frei nutzbar – fork & build.

---

## 📦 Was ist das hier?

Ein schlankes Workspace-Setup, das die **dekompilierten / entpackten Minecraft-Klassen**
als reine **Compile-Referenz** verwendet und dir zwei parallele Wege zum Modden bietet:

1. **Loader-frei** (`Mod/`) – reines Java + Gradle, geladen als Java-Agent oder via eigenem Launcher.
2. **Fabric** (`ModFabric/`) – klassisches Loom-Setup mit Yarn-Mappings und Mixins.

Gedacht für:
- 🔬 **Snapshot-Reverse-Engineering** (neue MC-Versionen bevor Loader nachziehen)
- 🧪 **Java-Agent-Mods** (ASM/ByteBuddy-Transformer) — siehe `Mod/`
- 🧶 **Fabric-Mods** mit Loom/Yarn/Mixin — siehe `ModFabric/`
- 🛠️ **Eigene Loader / Launcher / Pack-Tools**
- 📚 **Lernen**, wie der MC-Client intern aufgebaut ist (Blaze3D, Netcode, Worldgen …)

---

## 🗂️ Ordnerstruktur

```
26_2_5dev/
├── README.md                ← du bist hier
├── PackEditor.lnk           ← Verknüpfung zum externen PackEditor-Tool
│
├── Mc_26.2.5snap/           ← 📚 REFERENZ (read-only behandeln!)
│   └── Minecraft/
│       ├── reference/                       # entpackte / dekompilierte Vanilla-Klassen
│       │   ├── 26.2-snapshot-5.jar          #   originale Client-Jar (signiert von Mojang)
│       │   ├── version.json                 #   Snapshot-Metadaten
│       │   ├── pack.png
│       │   ├── flightrecorder-config.jfc    #   JFR-Profilingkonfig
│       │   ├── net/minecraft/...            #   Spielcode
│       │   ├── com/mojang/blaze3d/...       #   Render-Engine (OpenGL/Vulkan/Shader)
│       │   ├── com/mojang/math/...
│       │   └── com/mojang/realmsclient/...
│       └── compiledjar/                     # Output-Slot für deine fertigen Mod-Jars
│
├── Mod/                     ← 🛠️ LOADER-FREIE Java-Agent-Mod
│   ├── build.gradle.kts     #   bindet die Reference-Jar als compileOnly ein
│   ├── settings.gradle.kts
│   ├── gradle.properties    #   mod.id, version, mc.version, java-version
│   ├── README.md            #   Detail-Doku zur Mod-Vorlage (Bauen, Laden, Agent, …)
│   └── src/main/
│       ├── java/dev/mc26/examplemod/ExampleMod.java
│       └── resources/
│           ├── mod.json
│           └── META-INF/MANIFEST.MF         # macht die Jar Java-Agent-fähig
│
├── ModFabric/               ← 🧶 FABRIC-MOD (Loom + Yarn + Mixin)
│   ├── build.gradle.kts     #   fabric-loom Plugin, runClient/runServer Tasks
│   ├── settings.gradle.kts  #   pluginManagement mit Fabric-Maven
│   ├── gradle.properties    #   minecraft_version / yarn_mappings / loader_version / fabric_api_version
│   ├── README.md            #   Anleitung Versionen pflegen, runClient, Mixins
│   └── src/
│       ├── main/                            # Common (Server + Client)
│       │   ├── java/dev/mc26/examplefabric/ExampleFabricMod.java
│       │   └── resources/
│       │       ├── fabric.mod.json
│       │       └── examplefabric.mixins.json
│       └── client/                          # Client-Only Source-Set
│           ├── java/dev/mc26/examplefabric/client/
│           │   ├── ExampleFabricClient.java
│           │   └── mixin/TitleScreenMixin.java
│           └── resources/examplefabric.client.mixins.json
│
└── dev/                     ← 🗒️ Notizen, Scratch, Recherche
    └── dev.txt
```

### Rollen der Top-Ordner

| Ordner            | Zweck                                                       | Editieren? |
|-------------------|--------------------------------------------------------------|------------|
| `Mc_26.2.5snap/`  | **Referenz-Quelle** – Vanilla-MC zum Nachschlagen            | ❌ nein     |
| `Mod/`            | **Loader-freie Mod** (reines Java + Java-Agent)              | ✅ ja       |
| `ModFabric/`      | **Fabric-Mod** (Loom, Yarn-Mappings, Mixins, runClient)      | ✅ ja       |
| `dev/`            | Freier Notiz-/Scratch-Space                                  | ✅ ja       |

> Beide Mod-Projekte (`Mod/` und `ModFabric/`) sind **unabhängige Gradle-Builds**
> und legen ihre fertige Jar jeweils in `Mc_26.2.5snap/Minecraft/compiledjar/` ab.

---

## 🚀 Schnellstart

### Voraussetzungen
- **JDK 25** (passend zu MC's `java-runtime-epsilon`)
- **Gradle 8.10+** (oder Wrapper hinzufügen via `gradle wrapper`)
- IntelliJ IDEA (empfohlen) oder VS Code mit Java-Extension

### Variante A — Loader-freie Mod (`Mod/`)
```powershell
cd Mod
gradle build
java -javaagent:..\Mc_26.2.5snap\Minecraft\compiledjar\examplemod-0.1.0.jar `
     -jar ..\Mc_26.2.5snap\Minecraft\reference\26.2-snapshot-5.jar
```
> Mehr Details (Classpath-Injection, Bytecode-Patch, Bootstrap) → [`Mod/README.md`](Mod/README.md)

### Variante B — Fabric-Mod (`ModFabric/`)
```powershell
cd ModFabric
gradle build           # baut + remappt + kopiert nach compiledjar/
gradle runClient       # startet Dev-Client mit deinem Mod
gradle runServer       # dedizierter Dev-Server
```
Versionen (Yarn / Loader / Fabric API) stehen zentral in `ModFabric/gradle.properties`
und sollten regelmäßig gegen <https://fabricmc.net/develop> abgeglichen werden.
> Mehr Details (Mixins, Split Source Sets) → [`ModFabric/README.md`](ModFabric/README.md)

---

## 🎯 Ziel-Snapshot

Aus `Mc_26.2.5snap/Minecraft/reference/version.json`:

| Feld              | Wert                       |
|-------------------|----------------------------|
| ID                | `26.2-snapshot-5`          |
| Name              | `26.2 Snapshot 5`          |
| World-Version     | `4889`                     |
| Protocol          | `1073742135` (Snapshot-Bit)|
| Resource-Pack     | `86.2`                     |
| Data-Pack         | `104.0`                    |
| Java-Komponente   | `java-runtime-epsilon` (25)|
| Build             | `2026-04-28T12:39:42Z`     |
| Stabil            | nein                       |

---

## 🧭 Wie navigiere ich die Referenz?

In IntelliJ erkennt der `compileOnly(files(...))`-Eintrag in `Mod/build.gradle.kts`
die Reference-Jar automatisch — **Auto-Complete, Go-to-Definition und Refactor-Vorschau**
funktionieren auf allen `net.minecraft.*` und `com.mojang.*` Klassen.
In `ModFabric/` übernimmt das Loom mit Yarn-gemappten Sourcen automatisch.

Empfohlene Einstiegspunkte:
- `net.minecraft.client.main.Main` – Client-Boot
- `net.minecraft.server.Main` – Server-Boot
- `net.minecraft.client.Minecraft` – zentrale Client-Klasse
- `com.mojang.blaze3d.systems.RenderSystem` – Render-Hooks
- `net.minecraft.network.protocol.*` – Netzwerk-Pakete

---

## 🤝 Als Vorlage benutzen

1. Repo forken / kopieren.
2. `Mc_26.2.5snap/` mit deiner eigenen entpackten MC-Version befüllen
   (oder `mc.version` / `minecraft_version` in den jeweiligen `gradle.properties` ändern).
3. Pakete `dev.mc26.examplemod` bzw. `dev.mc26.examplefabric` umbenennen → `mod.id` / `mod_id`,
   `mod.group` / `mod_group` in `gradle.properties` anpassen.
4. Nicht benötigte Variante (`Mod/` oder `ModFabric/`) einfach löschen.
5. Loslegen.

PRs / Issues mit Verbesserungen am Build-Setup sind willkommen.

---

## ⚖️ Rechtliches

- **Diese Vorlage** (Build-Skripte, Struktur, README, Beispielcode in `Mod/src` und `ModFabric/src`)
  ist frei verwendbar — behandle sie wie *Public Domain / MIT*.
- **Minecraft-Inhalte** unter `Mc_26.2.5snap/Minecraft/reference/`
  (Klassen, Assets, `pack.png`, `LICENSE`, Mojang-Signaturen) gehören
  **Mojang Studios / Microsoft** und unterliegen dem
  [Minecraft EULA](https://www.minecraft.net/eula). Sie sind **nicht** Teil
  der Vorlage und werden hier nur als lokale Compile-Referenz vorausgesetzt.
- **Verteile keine Vanilla-MC-Klassen** mit deiner Mod-Jar — `compileOnly` (Variante A)
  bzw. Loom's Remapping (Variante B) sorgen bereits dafür, dass sie nicht mitgepackt werden.
- Mods, die Vanilla-Logik modifizieren, sind im Rahmen des EULA erlaubt,
  solange sie kostenlos verteilt werden und keine geschützten Assets enthalten.

---

## 📌 Status

🚧 **Work in Progress** — Vorlage und Snapshot 26.2-5 sind beide aktiv in Bewegung.
Erwarte Breaking Changes auf beiden Seiten.

