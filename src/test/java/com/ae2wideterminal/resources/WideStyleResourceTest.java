package com.ae2wideterminal.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WideStyleResourceTest {
    private static final Path STYLE_DIRECTORY = Path.of(
            "src/main/resources/assets/ae2/screens/ae2wideterminal");
    private static final Path TEXTURE_DIRECTORY = Path.of(
            "src/main/resources/assets/ae2/textures/guis/ae2wideterminal");

    @ParameterizedTest
    @CsvSource({
            "wireless_terminal_wide.json,terminal_wide.png,357",
            "ae2_crafting_terminal_wide.json,crafting_wide.png,357",
            "wtlib_crafting_terminal_wide.json,wtlib_extras_wide.png,362",
            "wtlib_pattern_encoding_terminal_wide.json,pattern_wide.png,357"
    })
    void styleUsesEighteenColumnsAndExpectedTextureWidth(
            String styleName, String textureName, int expectedWidth) throws IOException {
        Path stylePath = STYLE_DIRECTORY.resolve(styleName);
        JsonObject style = JsonParser.parseString(Files.readString(stylePath)).getAsJsonObject();
        assertEquals(18, style.getAsJsonObject("terminalStyle")
                .get("slotsPerRow").getAsInt());

        Path texturePath = TEXTURE_DIRECTORY.resolve(textureName);
        BufferedImage texture = ImageIO.read(texturePath.toFile());
        assertEquals(expectedWidth, texture.getWidth());
    }

    @Test
    void wtlibUpperPanelDoesNotStretchEdgePixelsAcrossWideMargins() throws IOException {
        BufferedImage texture = ImageIO.read(
                TEXTURE_DIRECTORY.resolve("wtlib_extras_wide.png").toFile());

        assertEquals(0, texture.getRGB(10, 25) >>> 24);
        assertEquals(0, texture.getRGB(350, 25) >>> 24);
    }
}
