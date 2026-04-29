package dev.mc26.examplemod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Einstiegspunkt der Mod.
 *
 * Da Minecraft 26.2-snapshot-5 (noch) keinen offiziellen Loader hat, wird
 * diese Klasse manuell aufgerufen – z. B. via -javaagent, eigenem Launcher
 * oder Bytecode-Patch in der Main-Klasse von Minecraft.
 *
 * Siehe README.md für Lade-Strategien.
 */
public final class ExampleMod {

    public static final String MOD_ID  = "examplemod";
    public static final String VERSION = "0.1.0";

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private ExampleMod() {}

    /** Wird vom Loader / Agent / Patch aufgerufen, sobald Minecraft bereit ist. */
    public static void init() {
        LOGGER.info("[{}] v{} initialisiert für Minecraft 26.2-snapshot-5", MOD_ID, VERSION);
        // TODO: hier Hooks, Listener, Mixins, etc. registrieren
    }

    /** Optional: Premain-Hook, falls als Java-Agent gestartet. */
    public static void premain(String args, java.lang.instrument.Instrumentation inst) {
        LOGGER.info("[{}] premain – Agent geladen", MOD_ID);
        // Beispiel: ClassFileTransformer registrieren
        // inst.addTransformer(new MyTransformer());
        init();
    }
}

