package com.bilaldodin.cobblemonwarp.util;

import net.minecraft.util.math.BlockPos;

public final class CoordinateInputValidator {
    private CoordinateInputValidator() {
    }

    public static ValidationResult parse(String xText, String yText, String zText) {
        if (isBlank(xText) || isBlank(yText) || isBlank(zText)) {
            return ValidationResult.error("message.cobblemon_warp.error.coordinates_blank");
        }

        try {
            int x = Integer.parseInt(xText.trim());
            int y = Integer.parseInt(yText.trim());
            int z = Integer.parseInt(zText.trim());
            return ValidationResult.success(new BlockPos(x, y, z));
        } catch (NumberFormatException ex) {
            return ValidationResult.error("message.cobblemon_warp.error.coordinates_integer");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public record ValidationResult(boolean success, BlockPos pos, String translationKey) {
        public static ValidationResult success(BlockPos pos) {
            return new ValidationResult(true, pos, null);
        }

        public static ValidationResult error(String translationKey) {
            return new ValidationResult(false, null, translationKey);
        }
    }
}
