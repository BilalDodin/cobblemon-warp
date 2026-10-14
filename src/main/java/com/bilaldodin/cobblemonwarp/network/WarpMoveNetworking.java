package com.bilaldodin.cobblemonwarp.network;

import com.bilaldodin.cobblemonwarp.server.WarpMoveService;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class WarpMoveNetworking {
    private WarpMoveNetworking() {
    }

    public static void registerCommon() {
        PayloadTypeRegistry.playC2S().register(WarpMovePayload.ID, WarpMovePayload.CODEC);
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(WarpMovePayload.ID, (payload, context) ->
            context.server().execute(() -> WarpMoveService.handle(context.player(), payload))
        );
    }

    public static void sendToServer(WarpMovePayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
