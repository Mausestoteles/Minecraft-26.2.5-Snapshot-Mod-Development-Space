package dev.mc26.examplefabric;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExampleFabricMod implements ModInitializer {

    public static final String MOD_ID = "examplefabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[{}] geladen – Common Init", MOD_ID);
        // TODO: Items, Blöcke, Events, Commands registrieren
    }
}

