package com.bilaldodin.cobblemonwarp;

import com.bilaldodin.cobblemonwarp.network.WarpMoveNetworking;
import net.fabricmc.api.ModInitializer;

public final class CobblemonWarpMod implements ModInitializer {
    public static final String MOD_ID = "cobblemon_warp";

    @Override
    public void onInitialize() {
        WarpMoveNetworking.registerCommon();
        WarpMoveNetworking.registerServer();
    }
}
