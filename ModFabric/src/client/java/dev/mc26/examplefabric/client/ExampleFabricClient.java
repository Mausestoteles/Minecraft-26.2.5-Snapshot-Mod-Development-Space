package dev.mc26.examplefabric.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExampleFabricClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("examplefabric/client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Client-Init für Example Fabric Mod");
        // TODO: KeyBindings, HUD-Renderer, Resource-Reloader registrieren
    }
}

