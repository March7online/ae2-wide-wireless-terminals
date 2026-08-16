package com.ae2wideterminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

class MetadataContractTest {
    private static final Path MODS_TOML = Path.of("build/resources/main/META-INF/mods.toml");
    private static final Path MIXINS_JSON = Path.of("build/resources/main/ae2wideterminal.mixins.json");

    @Test
    void metadataDeclaresClientOnlyDependenciesAndMixin() throws IOException {
        TomlParseResult metadata = Toml.parse(readOrEmpty(MODS_TOML));
        assertFalse(metadata.hasErrors(), metadata.errors().toString());
        assertEquals("javafml", metadata.getString("modLoader"));

        TomlArray dependencies = metadata.getArray("dependencies.ae2wideterminal");
        assertNotNull(dependencies);
        assertDependency(dependencies, "ae2", "[15.4.10,15.5.0)");
        assertDependency(dependencies, "ae2wtlib", "[15.3.3,15.4.0)");

        JsonObject mixins = JsonParser.parseString(readOrEmpty(MIXINS_JSON)).getAsJsonObject();
        assertTrue(mixins.has("client"));
        JsonArray clientMixins = mixins.getAsJsonArray("client");
        assertEquals(3, clientMixins.size());
        assertTrue(contains(clientMixins, "client.InitScreensMixin"));
        assertTrue(contains(clientMixins, "client.EncodingModePanelPositionMixin"));
        assertTrue(contains(clientMixins, "client.MEStorageScreenSearchAccess"));
        assertFalse(mixins.has("mixins"));
    }

    private static boolean contains(JsonArray values, String expected) {
        return StreamSupport.stream(values.spliterator(), false)
                .anyMatch(value -> expected.equals(value.getAsString()));
    }

    private static void assertDependency(TomlArray dependencies, String modId, String versionRange) {
        for (int index = 0; index < dependencies.size(); index++) {
            TomlTable dependency = dependencies.getTable(index);
            if (modId.equals(dependency.getString("modId"))) {
                assertEquals(versionRange, dependency.getString("versionRange"));
                assertEquals("CLIENT", dependency.getString("side"));
                return;
            }
        }
        throw new AssertionError("Missing dependency " + modId);
    }

    private static String readOrEmpty(Path path) throws IOException {
        return Files.exists(path) ? Files.readString(path) : "{}";
    }
}
