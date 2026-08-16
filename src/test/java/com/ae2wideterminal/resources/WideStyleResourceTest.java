package com.ae2wideterminal.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @ParameterizedTest
    @CsvSource({
            "wireless_terminal_wide.json",
            "ae2_crafting_terminal_wide.json",
            "wtlib_crafting_terminal_wide.json",
            "wtlib_pattern_encoding_terminal_wide.json"
    })
    void wideTextureBlittersDeclareTheirActualReferenceWidth(String styleName) throws IOException {
        JsonObject style = JsonParser.parseString(
                Files.readString(STYLE_DIRECTORY.resolve(styleName))).getAsJsonObject();
        JsonObject terminalStyle = style.getAsJsonObject("terminalStyle");

        for (String element : new String[] {"header", "firstRow", "row", "lastRow", "bottom"}) {
            JsonObject blitter = terminalStyle.getAsJsonObject(element);
            String textureName = Path.of(blitter.get("texture").getAsString()).getFileName().toString();
            BufferedImage texture = ImageIO.read(TEXTURE_DIRECTORY.resolve(textureName).toFile());
            assertTrue(blitter.has("textureWidth"), styleName + " missing textureWidth for " + element);
            assertEquals(texture.getWidth(), blitter.get("textureWidth").getAsInt(),
                    styleName + " textureWidth does not match PNG for " + element);
            assertEquals(texture.getHeight(), blitter.get("textureHeight").getAsInt(),
                    styleName + " textureHeight does not match PNG for " + element);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "wireless_terminal_wide.json,242",
            "ae2_crafting_terminal_wide.json,242",
            "wtlib_crafting_terminal_wide.json,247",
            "wtlib_pattern_encoding_terminal_wide.json,242"
    })
    void wideSearchFieldKeepsItsRightEdgeInsideTheTerminal(String styleName, int expectedLeft) throws IOException {
        JsonObject style = JsonParser.parseString(
                Files.readString(STYLE_DIRECTORY.resolve(styleName))).getAsJsonObject();
        JsonObject search = style.getAsJsonObject("widgets").getAsJsonObject("search");

        assertFalse(search.has("right"));
        assertEquals(expectedLeft, search.get("left").getAsInt());
        assertEquals(89, search.get("width").getAsInt());
        assertEquals(4, search.get("top").getAsInt());
        assertEquals(12, search.get("height").getAsInt());
    }

    @Test
    void wtlibUpperPanelDoesNotStretchEdgePixelsAcrossWideMargins() throws IOException {
        BufferedImage texture = ImageIO.read(
                TEXTURE_DIRECTORY.resolve("wtlib_extras_wide.png").toFile());

        assertEquals(0, texture.getRGB(10, 25) >>> 24);
        assertEquals(0, texture.getRGB(350, 25) >>> 24);
    }

    @Test
    void wtlibCraftingBottomKeepsWideMarginsTransparent() throws IOException {
        BufferedImage texture = ImageIO.read(
                TEXTURE_DIRECTORY.resolve("wtlib_extras_wide.png").toFile());

        assertEquals(0, texture.getRGB(10, 100) >>> 24);
        assertEquals(0, texture.getRGB(320, 100) >>> 24);
        assertTrue((texture.getRGB(90, 100) >>> 24) > 0);
    }

    @Test
    void patternEncodingBottomUsesCenteredNarrowPanelWithTransparentSides() throws IOException {
        BufferedImage texture = ImageIO.read(
                TEXTURE_DIRECTORY.resolve("pattern_wide.png").toFile());

        assertEquals(0, texture.getRGB(10, 100) >>> 24);
        assertEquals(0, texture.getRGB(330, 100) >>> 24);
        assertTrue((texture.getRGB(81, 100) >>> 24) > 0);
        assertTrue((texture.getRGB(256, 100) >>> 24) > 0);
        assertTrue((texture.getRGB(100, 100) >>> 24) > 0);
    }

    @Test
    void wtlibCraftingUtilityButtonsStayWithCenteredBottomPanel() throws IOException {
        JsonObject style = JsonParser.parseString(Files.readString(
                STYLE_DIRECTORY.resolve("wtlib_crafting_terminal_wide.json"))).getAsJsonObject();
        JsonObject widgets = style.getAsJsonObject("widgets");

        assertWidgetRight(widgets, "wirelessTerminalSettingsButton", 165);
        assertWidgetRight(widgets, "magnetCardMenuButton", 147);
        assertWidgetRight(widgets, "trashButton", 129);
    }

    @Test
    void wtlibPatternEncodingAreaUsesOneCenteredCoordinateSystem() throws IOException {
        JsonObject style = patternEncodingStyle();
        JsonObject slots = style.getAsJsonObject("slots");
        JsonObject widgets = style.getAsJsonObject("widgets");

        for (int mode = 0; mode < 4; mode++) {
            assertLeft(widgets, "modePanel" + mode, 90);
        }
        assertLeft(slots, "CRAFTING_GRID", 99);
        assertLeft(slots, "CRAFTING_RESULT", 191);
        assertLeft(slots, "BLANK_PATTERN", 228);
        assertLeft(widgets, "modeTabButton0", 254);
        assertLeft(widgets, "craftingClearPattern", 155);
        assertLeft(widgets, "craftingSubstitutions", 165);
        assertLeft(widgets, "craftingFluidSubstitutions", 175);
    }

    @Test
    void wtlibProcessingModeUsesCenteredSlotsButtonsAndTooltips() throws IOException {
        JsonObject style = patternEncodingStyle();
        JsonObject slots = style.getAsJsonObject("slots");
        JsonObject widgets = style.getAsJsonObject("widgets");
        JsonObject tooltips = style.getAsJsonObject("tooltips");

        assertLeft(slots, "PROCESSING_INPUTS", 107);
        assertLeft(slots, "PROCESSING_OUTPUTS", 191);
        assertLeft(widgets, "processingPatternModeScrollbar", 98);
        assertLeft(widgets, "processingCycleOutput", 208);
        assertLeft(widgets, "processingClearPattern", 163);
        assertLeft(tooltips, "processing-primary-output", 190);
        assertLeft(tooltips, "processing-optional-output1", 190);
        assertLeft(tooltips, "processing-optional-output2", 190);
        assertLeft(tooltips, "processing-optional-output3", 190);
    }

    @Test
    void wtlibSmithingModeUsesCenteredSlotsAndButtons() throws IOException {
        JsonObject style = patternEncodingStyle();
        JsonObject slots = style.getAsJsonObject("slots");
        JsonObject widgets = style.getAsJsonObject("widgets");

        assertLeft(slots, "SMITHING_TABLE_TEMPLATE", 102);
        assertLeft(slots, "SMITHING_TABLE_BASE", 120);
        assertLeft(slots, "SMITHING_TABLE_ADDITION", 138);
        assertLeft(slots, "SMITHING_TABLE_RESULT", 192);
        assertLeft(widgets, "smithingTableClearPattern", 155);
        assertLeft(widgets, "smithingTableSubstitutions", 165);
    }

    @Test
    void wtlibStonecuttingModeUsesCenteredInputAndScrollbar() throws IOException {
        JsonObject style = patternEncodingStyle();
        JsonObject slots = style.getAsJsonObject("slots");
        JsonObject widgets = style.getAsJsonObject("widgets");

        assertLeft(slots, "STONECUTTING_INPUT", 102);
        assertLeft(widgets, "stonecuttingPatternModeScrollbar", 200);
    }

    private static JsonObject patternEncodingStyle() throws IOException {
        return JsonParser.parseString(Files.readString(
                STYLE_DIRECTORY.resolve("wtlib_pattern_encoding_terminal_wide.json"))).getAsJsonObject();
    }

    private static void assertLeft(JsonObject elements, String elementName, int expectedLeft) {
        assertTrue(elements.has(elementName), "missing explicit position for " + elementName);
        assertEquals(expectedLeft, elements.getAsJsonObject(elementName).get("left").getAsInt(),
                "wrong left coordinate for " + elementName);
    }

    private static void assertWidgetRight(JsonObject widgets, String widgetName, int expectedRight) {
        assertTrue(widgets.has(widgetName), "missing explicit position for " + widgetName);
        assertEquals(expectedRight, widgets.getAsJsonObject(widgetName).get("right").getAsInt());
    }
}
