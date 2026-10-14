package com.bilaldodin.cobblemonwarp.client;

import com.bilaldodin.cobblemonwarp.network.WarpMoveNetworking;
import com.bilaldodin.cobblemonwarp.network.WarpMovePayload;
import com.bilaldodin.cobblemonwarp.util.CoordinateInputValidator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public final class WarpChannelingScreen extends Screen {
    private final UUID pokemonUuid;
    private TextFieldWidget xInput;
    private TextFieldWidget yInput;
    private TextFieldWidget zInput;
    private Text errorMessage = Text.empty();

    public WarpChannelingScreen(UUID pokemonUuid, String pokemonName) {
        super(Text.literal(pokemonName + " - Teleport Channeling"));
        this.pokemonUuid = pokemonUuid;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int inputWidth = 160;

        this.xInput = new TextFieldWidget(this.textRenderer, centerX - inputWidth / 2, centerY - 36, inputWidth, 20, Text.translatable("screen.cobblemon_warp.x"));
        this.yInput = new TextFieldWidget(this.textRenderer, centerX - inputWidth / 2, centerY - 8, inputWidth, 20, Text.translatable("screen.cobblemon_warp.y"));
        this.zInput = new TextFieldWidget(this.textRenderer, centerX - inputWidth / 2, centerY + 20, inputWidth, 20, Text.translatable("screen.cobblemon_warp.z"));
        this.xInput.setMaxLength(11);
        this.yInput.setMaxLength(11);
        this.zInput.setMaxLength(11);

        addDrawableChild(this.xInput);
        addDrawableChild(this.yInput);
        addDrawableChild(this.zInput);

        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.cobblemon_warp.confirm"), button -> trySubmit())
            .dimensions(centerX - 60, centerY + 52, 120, 20)
            .build());

        setInitialFocus(this.xInput);
    }

    private void trySubmit() {
        CoordinateInputValidator.ValidationResult result = CoordinateInputValidator.parse(this.xInput.getText(), this.yInput.getText(), this.zInput.getText());
        if (!result.success()) {
            this.errorMessage = Text.translatable(result.translationKey());
            return;
        }

        BlockPos destination = result.pos();
        WarpMoveNetworking.sendToServer(WarpMovePayload.create(this.pokemonUuid, destination));
        if (this.client != null) {
            this.client.setScreen(null);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, this.height / 2 - 68, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("screen.cobblemon_warp.x"), centerX - 80, this.height / 2 - 46, 0xA0A0A0);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("screen.cobblemon_warp.y"), centerX - 80, this.height / 2 - 18, 0xA0A0A0);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("screen.cobblemon_warp.z"), centerX - 80, this.height / 2 + 10, 0xA0A0A0);
        if (!this.errorMessage.getString().isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, this.errorMessage, centerX, this.height / 2 + 78, 0xFF5555);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
