package com.bilaldodin.cobblemonwarp.network;

import com.bilaldodin.cobblemonwarp.CobblemonWarpMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public record WarpMovePayload(int version, UUID pokemonUuid, BlockPos destination) implements CustomPayload {
    public static final int PROTOCOL_VERSION = 1;
    public static final Id<WarpMovePayload> ID = new Id<>(Identifier.of(CobblemonWarpMod.MOD_ID, "warp_move"));
    public static final PacketCodec<RegistryByteBuf, WarpMovePayload> CODEC = PacketCodec.tuple(
        PacketCodecs.VAR_INT,
        WarpMovePayload::version,
        Uuids.PACKET_CODEC,
        WarpMovePayload::pokemonUuid,
        BlockPos.PACKET_CODEC,
        WarpMovePayload::destination,
        WarpMovePayload::new
    );

    public static WarpMovePayload create(UUID pokemonUuid, BlockPos destination) {
        return new WarpMovePayload(PROTOCOL_VERSION, pokemonUuid, destination);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
