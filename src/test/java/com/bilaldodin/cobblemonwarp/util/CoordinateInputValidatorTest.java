package com.bilaldodin.cobblemonwarp.util;

import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateInputValidatorTest {
    @Test
    void parsesValidIntegerCoordinates() {
        CoordinateInputValidator.ValidationResult result = CoordinateInputValidator.parse("10", "64", "-5");
        assertTrue(result.success());
        assertEquals(new BlockPos(10, 64, -5), result.pos());
    }

    @Test
    void rejectsBlankCoordinates() {
        CoordinateInputValidator.ValidationResult result = CoordinateInputValidator.parse("", "64", "-5");
        assertFalse(result.success());
        assertEquals("message.cobblemon_warp.error.coordinates_blank", result.translationKey());
    }

    @Test
    void rejectsNonIntegerCoordinates() {
        CoordinateInputValidator.ValidationResult result = CoordinateInputValidator.parse("3.2", "abc", "-5");
        assertFalse(result.success());
        assertEquals("message.cobblemon_warp.error.coordinates_integer", result.translationKey());
    }
}
