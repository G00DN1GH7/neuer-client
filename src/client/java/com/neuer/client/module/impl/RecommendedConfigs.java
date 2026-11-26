package com.neuer.client.module.impl;

import com.neuer.client.clickgui.ConfigRecommendationScreen;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.utils.ClientPaths;
import com.neuer.client.utils.CryptoUtils;
import com.neuer.client.utils.ServerGlob;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Suggests a stored config when you join a server the config was made for.
 *
 * <p>Configs are read from disk only: the {@code config/neuerclient/configs} folder if it
 * exists, otherwise the configs bundled with the client. No network access.</p>
 */
public class RecommendedConfigs extends Module {
    private static final String BUNDLED_ROOT = "/assets/neuerclient/configs/";
    private static final String BUNDLE_INDEX = "index.txt";
    private static final String LOCAL_DIR_NAME = "configs";

    private String lastServerIp = null;
    private boolean fetched = false;

    public static class FoundConfig {
        public String name;
        public String base64;
        public String modulesPreview;

        public FoundConfig(String name, String base64, String modulesPreview) {
            this.name = name;
            this.base64 = base64;
            this.modulesPreview = modulesPreview;
        }
    }

    private List<FoundConfig> currentServerConfigs = new ArrayList<>();

    public RecommendedConfigs() {
        super("RecommendedConfigs", Category.Misc, "Recommends server-specific configs upon joining (local files).", true);
        this.setToggled(true);
    }

    @Override
    public void onTick() {
        if (!isToggled() || mc.player == null) return;

        String currentServer = mc.getCurrentServer() != null ? mc.getCurrentServer().ip : null;

        if (currentServer != null && !currentServer.equals(lastServerIp)) {
            lastServerIp = currentServer;
            fetched = false;
            currentServerConfigs.clear();
            loadConfigsAndCheck(currentServer);
        } else if (currentServer == null) {
            lastServerIp = null;
            fetched = false;
            currentServerConfigs.clear();
        }

        if (fetched && currentServer != null && mc.gui.screen() == null && !currentServerConfigs.isEmpty()) {
            mc.setScreenAndShow(new ConfigRecommendationScreen(new ArrayList<>(currentServerConfigs)));
            currentServerConfigs.clear();
        }
    }

    private void loadConfigsAndCheck(String ip) {
        CompletableFuture.supplyAsync(() -> findMatchingConfigs(ip)).thenAccept(found -> {
            mc.execute(() -> {
                currentServerConfigs = found;
                fetched = true;
            });
        });
    }

    private List<FoundConfig> findMatchingConfigs(String ip) {
        List<FoundConfig> found = new ArrayList<>();
        for (String json : configDocuments()) {
            try {
                FoundConfig config = parseConfig(json, ip);
                if (config != null) {
                    found.add(config);
                }
            } catch (Exception e) {
                System.err.println("[Neuer] Skipping unreadable recommended config: " + e.getMessage());
            }
        }
        return found;
    }

    private List<String> configDocuments() {
        List<String> documents = readLocalConfigs();
        if (documents.isEmpty()) {
            documents = readBundledConfigs();
        }
        return documents;
    }

    private List<String> readLocalConfigs() {
        List<String> documents = new ArrayList<>();
        Path directory = ClientPaths.resolve(LOCAL_DIR_NAME);
        if (!Files.isDirectory(directory)) {
            return documents;
        }
        try (Stream<Path> files = Files.list(directory)) {
            List<Path> jsonFiles = files
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .toList();
            for (Path file : jsonFiles) {
                documents.add(Files.readString(file, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.err.println("[Neuer] Failed to read local recommended configs: " + e.getMessage());
        }
        return documents;
    }

    private List<String> readBundledConfigs() {
        List<String> documents = new ArrayList<>();
        List<String> names = ServerGlob.splitList(readResource(BUNDLE_INDEX));
        for (String name : names) {
            String document = readResource(name.startsWith("configs/") ? name.substring("configs/".length()) : name);
            if (document != null && !document.isBlank()) {
                documents.add(document);
            }
        }
        return documents;
    }

    private String readResource(String fileName) {
        try (InputStream stream = RecommendedConfigs.class.getResourceAsStream(BUNDLED_ROOT + fileName)) {
            return stream == null ? null : new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("[Neuer] Failed to read bundled config " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    private FoundConfig parseConfig(String json, String ip) {
        JsonObject configObj = JsonParser.parseString(json).getAsJsonObject();
        if (!configObj.has("server") || !configObj.has("name") || !configObj.has("configBase64")) {
            return null;
        }

        JsonElement serverElement = configObj.get("server");
        List<String> serverGlobs = new ArrayList<>();
        if (serverElement.isJsonArray()) {
            JsonArray servers = serverElement.getAsJsonArray();
            for (JsonElement element : servers) {
                serverGlobs.add(element.getAsString());
            }
        } else {
            serverGlobs.addAll(ServerGlob.splitList(serverElement.getAsString()));
        }

        if (!ServerGlob.matchesAny(serverGlobs, ip)) {
            return null;
        }

        String configName = configObj.get("name").getAsString();
        String base64 = configObj.get("configBase64").getAsString();
        return new FoundConfig(configName, base64, previewModules(base64));
    }

    private String previewModules(String base64) {
        try {
            String rawJson = CryptoUtils.decrypt(base64);
            if (rawJson == null) {
                return "Could not preview modules.";
            }
            JsonObject payload = JsonParser.parseString(rawJson).getAsJsonObject();
            if (!payload.has("Modules")) {
                return "Modules: unknown";
            }
            JsonArray modules = payload.getAsJsonArray("Modules");
            List<String> moduleNames = new ArrayList<>();
            for (JsonElement moduleElement : modules) {
                JsonObject moduleObj = moduleElement.getAsJsonObject();
                String moduleName = moduleObj.get("Name").getAsString();
                String keybind = "None";
                if (moduleObj.has("Keybind") && moduleObj.get("Keybind").getAsInt() != -1) {
                    keybind = String.valueOf(moduleObj.get("Keybind").getAsInt());
                }
                moduleNames.add(moduleName + " (Key: " + keybind + ")");
            }
            return "Modules: " + String.join(", ", moduleNames);
        } catch (Exception e) {
            return "Could not preview modules.";
        }
    }
}
