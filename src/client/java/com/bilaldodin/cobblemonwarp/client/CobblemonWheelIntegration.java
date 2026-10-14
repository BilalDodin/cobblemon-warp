package com.bilaldodin.cobblemonwarp.client;

import com.bilaldodin.cobblemonwarp.CobblemonWarpMod;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.pokemon.interaction.PokemonInteractionGUICreationEvent;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.interact.wheel.InteractWheelOption;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.Objects;

public final class CobblemonWheelIntegration {
    private static final Identifier WARP_ICON = Identifier.of(CobblemonWarpMod.MOD_ID, "textures/gui/interact/warp_move.png");
    private static final String WARP_TOOLTIP_KEY = "wheel.cobblemon_warp.warp_move";

    private CobblemonWheelIntegration() {
    }

    public static void bootstrap() {
        CobblemonEvents.POKEMON_INTERACTION_GUI_CREATION.subscribe(Priority.NORMAL, CobblemonWheelIntegration::onGuiCreation);
    }

    private static void onGuiCreation(PokemonInteractionGUICreationEvent event) {
        Pokemon selectedPokemon = CobblemonClient.INSTANCE.getStorage().getParty().getSlots().stream()
            .filter(Objects::nonNull)
            .filter(slot -> slot.getEntity() != null && slot.getEntity().getUuid().equals(event.getPokemonID()))
            .findFirst()
            .orElse(null);

        if (selectedPokemon == null) {
            return;
        }

        String pokemonName = selectedPokemon.getSpecies().getName();
        InteractWheelOption warpMoveOption = new InteractWheelOption(
            WARP_ICON,
            null,
            true,
            WARP_TOOLTIP_KEY,
            () -> new Vector3f(1.0F, 1.0F, 1.0F),
            () -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(new WarpChannelingScreen(event.getPokemonID(), pokemonName));
                return Unit.INSTANCE;
            }
        );

        event.addFillingOption(warpMoveOption);
    }
}
