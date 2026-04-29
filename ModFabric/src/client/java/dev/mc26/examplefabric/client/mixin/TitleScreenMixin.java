package dev.mc26.examplefabric.client.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.LoggerFactory;

/**
 * Beispiel-Mixin: loggt einmalig, wenn der Titelbildschirm initialisiert wird.
 * Falls das Yarn-Mapping für 26.2-snapshot-5 die Klasse umbenannt hat,
 * passe den Import oben entsprechend an.
 */
@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void examplefabric$onInit(CallbackInfo ci) {
        LoggerFactory.getLogger("examplefabric/mixin")
                .info("TitleScreen.init() aufgerufen – Mixin aktiv ✓");
    }
}

