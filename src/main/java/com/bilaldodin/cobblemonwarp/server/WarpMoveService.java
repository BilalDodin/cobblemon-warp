package com.bilaldodin.cobblemonwarp.server;

import com.bilaldodin.cobblemonwarp.network.WarpMovePayload;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public final class WarpMoveService {
    private static final double MAX_DISTANCE_SQUARED = 16D * 16D;

    private WarpMoveService() {
    }

    public static void handle(ServerPlayerEntity player, WarpMovePayload payload) {
        if (payload.version() != WarpMovePayload.PROTOCOL_VERSION) {
            return;
        }

        ServerWorld world = player.getServerWorld();
        Optional<Pokemon> matchedPartyPokemon = findPartyPokemon(player, payload);
        if (matchedPartyPokemon.isEmpty()) {
            return;
        }

        Entity entity = world.getEntity(payload.pokemonUuid());
        if (!(entity instanceof PokemonEntity pokemonEntity)) {
            return;
        }

        if (pokemonEntity.squaredDistanceTo(player) > MAX_DISTANCE_SQUARED) {
            return;
        }

        BlockPos safeDestination = findSafeDestination(world, payload.destination()).orElse(null);
        if (safeDestination == null) {
            return;
        }

        Vec3d playerStart = player.getPos();
        Vec3d pokemonStart = pokemonEntity.getPos();

        player.teleport(world, safeDestination.getX() + 0.5D, safeDestination.getY(), safeDestination.getZ() + 0.5D, player.getYaw(), player.getPitch());
        pokemonEntity.requestTeleport(safeDestination.getX() + 1.5D, safeDestination.getY(), safeDestination.getZ() + 0.5D);

        spawnWarpEffects(world, playerStart, player.getPos());
        spawnWarpEffects(world, pokemonStart, pokemonEntity.getPos());
    }

    private static Optional<Pokemon> findPartyPokemon(ServerPlayerEntity player, WarpMovePayload payload) {
        PlayerPartyStore partyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        for (Pokemon partyPokemon : partyStore) {
            if (partyPokemon == null || partyPokemon.getEntity() == null) {
                continue;
            }

            if (partyPokemon.getEntity().getUuid().equals(payload.pokemonUuid())
                && partyPokemon.getEntity().getWorld().getRegistryKey().equals(player.getWorld().getRegistryKey())) {
                return Optional.of(partyPokemon);
            }
        }

        return Optional.empty();
    }

    private static Optional<BlockPos> findSafeDestination(ServerWorld world, BlockPos requestedPos) {
        if (!isWithinBuildBounds(world, requestedPos)) {
            return Optional.empty();
        }

        for (int offset = 0; offset <= 4; offset++) {
            BlockPos candidate = requestedPos.up(offset);
            if (!isWithinBuildBounds(world, candidate) || !isWithinBuildBounds(world, candidate.up())) {
                continue;
            }
            if (isOpenSpace(world, candidate) && isOpenSpace(world, candidate.up()) && hasSolidFloor(world, candidate.down())) {
                return Optional.of(candidate);
            }
        }

        return Optional.empty();
    }

    private static boolean isWithinBuildBounds(ServerWorld world, BlockPos pos) {
        return world.isInBuildLimit(pos) && !world.isOutOfHeightLimit(pos);
    }

    private static boolean isOpenSpace(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.getCollisionShape(world, pos).isEmpty();
    }

    private static boolean hasSolidFloor(ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos).isSideSolidFullSquare(world, pos, net.minecraft.util.math.Direction.UP);
    }

    private static void spawnWarpEffects(ServerWorld world, Vec3d source, Vec3d destination) {
        spawnAt(world, source);
        spawnAt(world, destination);
    }

    private static void spawnAt(ServerWorld world, Vec3d position) {
        world.spawnParticles(ParticleTypes.PORTAL, position.x, position.y + 1.0D, position.z, 40, 0.45D, 0.8D, 0.45D, 0.12D);
        world.spawnParticles(ParticleTypes.WITCH, position.x, position.y + 1.0D, position.z, 30, 0.45D, 0.8D, 0.45D, 0.01D);
        world.playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.9F, 1.0F);
        world.playSound(null, position.x, position.y, position.z, SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.PLAYERS, 0.7F, 1.1F);
    }
}
