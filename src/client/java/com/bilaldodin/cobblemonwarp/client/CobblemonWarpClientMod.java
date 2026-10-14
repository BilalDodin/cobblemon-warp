package com.bilaldodin.cobblemonwarp.client;

import net.fabricmc.api.ClientModInitializer;

public final class CobblemonWarpClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobblemonWheelIntegration.bootstrap();
    }
}
