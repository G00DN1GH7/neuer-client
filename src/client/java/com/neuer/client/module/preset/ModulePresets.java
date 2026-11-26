package com.neuer.client.module.preset;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Loads the bundled per-module presets and applies them to live {@link Setting}s.
 *
 * <p>Data lives in {@code assets/neuerclient/presets.json}. Every module has a
 * {@code legit} and a {@code blatant} object; engines look settings up by name so
 * adding a new setting never breaks a preset, and values are always clamped by the
 * setting itself. Modules without tunable settings simply have empty objects.</p>
 */
public final class ModulePresets {
    private static final String RESOURCE = "/assets/neuerclient/presets.json";
    private static final String LEGACY_KEY = "legit";

    private static volatile JsonObject modules;
    private static volatile String raw;

    private ModulePresets() {
    }

    /** The {@code modules} object of the bundled preset file, never null. */
    public static JsonObject modules() {
        JsonObject local = modules;
        if (local != null) {
            return local;
        }
        JsonObject loaded = new JsonObject();
        String document = readResource();
        if (document != null) {
            try {
                JsonElement root = JsonParser.parseString(document);
                if (root.isJsonObject()) {
                    JsonObject obj = root.getAsJsonObject();
                    JsonElement entries = obj.get("modules");
                    if (entries != null && entries.isJsonObject()) {
                        loaded = entries.getAsJsonObject();
                    }
                }
            } catch (Exception e) {
                System.err.println("[Neuer] Could not parse module presets: " + e.getMessage());
            }
        }
        modules = loaded;
        return loaded;
    }

    /** Raw JSON text, used by tests and the catalog exporter. */
    public static String rawJson() {
        if (raw == null) {
            raw = readResource();
        }
        return raw == null ? "" : raw;
    }

    private static String readResource() {
        try (InputStream stream = ModulePresets.class.getResourceAsStream(RESOURCE)) {
            if (stream == null) {
                return null;
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("[Neuer] Could not read module presets: " + e.getMessage());
            return null;
        }
    }

    /** True when the module has an entry for at least one mode. */
    public static boolean hasPreset(Module module) {
        return entryFor(module, PresetMode.LEGIT) != null || entryFor(module, PresetMode.BLATANT) != null;
    }

    /** True when there is at least one value to apply, i.e. the module has tunables. */
    public static boolean hasTunablePreset(Module module) {
        JsonObject legit = entryFor(module, PresetMode.LEGIT);
        JsonObject blatant = entryFor(module, PresetMode.BLATANT);
        return (legit != null && !legit.isEmpty()) || (blatant != null && !blatant.isEmpty());
    }

    /** The preset object for a module and mode, or null when the module has none. */
    public static JsonObject entryFor(Module module, PresetMode mode) {
        if (module == null || module.getName() == null || mode == null) {
            return null;
        }
        JsonObject entries = modules();
        JsonElement moduleEntry = entries.get(module.getName());
        if (moduleEntry == null || !moduleEntry.isJsonObject()) {
            for (Map.Entry<String, JsonElement> candidate : entries.entrySet()) {
                if (candidate.getKey().equalsIgnoreCase(module.getName()) && candidate.getValue().isJsonObject()) {
                    moduleEntry = candidate.getValue();
                    break;
                }
            }
        }
        if (moduleEntry == null || !moduleEntry.isJsonObject()) {
            return null;
        }
        JsonElement modeEntry = moduleEntry.getAsJsonObject().get(mode.key());
        if (modeEntry == null) {
            modeEntry = moduleEntry.getAsJsonObject().get(LEGACY_KEY);
        }
        return modeEntry != null && modeEntry.isJsonObject() ? modeEntry.getAsJsonObject() : null;
    }

    /** Applies one preset to one module and returns how many settings changed. */
    public static int apply(Module module, PresetMode mode) {
        return apply(module, mode, null);
    }

    /**
     * Applies one preset, recording per-setting notes into {@code notes} when supplied
     * (used by the catalog exporter so skipped values are visible rather than silent).
     */
    public static int apply(Module module, PresetMode mode, Map<String, String> notes) {
        JsonObject entry = entryFor(module, mode);
        if (entry == null || module == null) {
            return 0;
        }
        List<Setting> settings;
        try {
            settings = module.getSettings();
        } catch (Exception e) {
            return 0;
        }
        if (settings == null || settings.isEmpty()) {
            return 0;
        }
        Map<String, Setting> byName = new LinkedHashMap<>();
        for (Setting setting : settings) {
            if (setting != null && setting.getName() != null) {
                byName.putIfAbsent(setting.getName().toLowerCase(Locale.ROOT), setting);
            }
        }

        int changed = 0;
        for (Map.Entry<String, JsonElement> override : entry.entrySet()) {
            Setting setting = byName.get(override.getKey().toLowerCase(Locale.ROOT));
            if (setting == null) {
                if (notes != null) {
                    notes.put(override.getKey(), "unknown setting");
                }
                continue;
            }
            String result = applyValue(setting, override.getValue());
            if ("changed".equals(result)) {
                changed++;
            } else if (notes != null) {
                notes.put(override.getKey(), result);
            }
        }
        return changed;
    }

    /** Applies a preset to every module and returns the total number of changed settings. */
    public static int applyAll(Collection<Module> modulesToApply, PresetMode mode) {
        if (modulesToApply == null || mode == null) {
            return 0;
        }
        int changed = 0;
        for (Module module : modulesToApply) {
            changed += apply(module, mode);
        }
        return changed;
    }

    /** Number of settings a preset touches (after name matching), used for UI counts. */
    public static int presetSize(Module module, PresetMode mode) {
        JsonObject entry = entryFor(module, mode);
        return entry == null ? 0 : entry.size();
    }

    private static String applyValue(Setting setting, JsonElement value) {
        try {
            if (setting.isCheck()) {
                if (!value.isJsonPrimitive()) {
                    return "not a boolean";
                }
                boolean next = value.getAsJsonPrimitive().isBoolean()
                    ? value.getAsBoolean()
                    : value.getAsDouble() != 0.0;
                if (setting.getValBoolean() == next) {
                    return "unchanged";
                }
                setting.setValBoolean(next);
                return "changed";
            }
            if (setting.isCombo()) {
                if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
                    return "not a string";
                }
                String next = value.getAsString();
                List<String> options = setting.getOptions();
                String match = null;
                if (options != null) {
                    for (String option : options) {
                        if (option != null && option.equalsIgnoreCase(next)) {
                            match = option;
                            break;
                        }
                    }
                }
                if (match == null) {
                    return "no such option";
                }
                if (match.equals(setting.getValString())) {
                    return "unchanged";
                }
                setting.setValString(match);
                return "changed";
            }
            if (setting.isText()) {
                if (!value.isJsonPrimitive()) {
                    return "not text";
                }
                String next = value.getAsString();
                if (next.equals(setting.getValText())) {
                    return "unchanged";
                }
                setting.setValText(next);
                return "changed";
            }
            if (setting.isColor()) {
                Integer next = readColor(value);
                if (next == null) {
                    return "not a colour";
                }
                if (next == setting.getValColor()) {
                    return "unchanged";
                }
                setting.setValColor(next);
                return "changed";
            }
            if (setting.isRange()) {
                if (!value.isJsonArray() || value.getAsJsonArray().size() < 2) {
                    return "needs [low, high]";
                }
                JsonArray pair = value.getAsJsonArray();
                double low = pair.get(0).getAsDouble();
                double high = pair.get(1).getAsDouble();
                if (setting.getRangeLow() == low && setting.getRangeHigh() == high) {
                    return "unchanged";
                }
                setting.setRange(low, high);
                return "changed";
            }
            if (setting.isCurve()) {
                if (!value.isJsonArray() || value.getAsJsonArray().size() < 4) {
                    return "needs [x1, y1, x2, y2]";
                }
                JsonArray points = value.getAsJsonArray();
                double x1 = points.get(0).getAsDouble();
                double y1 = points.get(1).getAsDouble();
                double x2 = points.get(2).getAsDouble();
                double y2 = points.get(3).getAsDouble();
                setting.setCurve(x1, y1, x2, y2);
                return "changed";
            }
            if (setting.isSlider()) {
                if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
                    return "not a number";
                }
                double next = value.getAsDouble();
                if (setting.getValDouble() == next) {
                    return "unchanged";
                }
                setting.setValDouble(next);
                return "changed";
            }
        } catch (Exception e) {
            return "invalid value";
        }
        return "unsupported setting";
    }

    private static Integer readColor(JsonElement value) {
        try {
            if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
                return value.getAsInt() | 0xFF000000;
            }
            if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString()) {
                return null;
            }
            String text = value.getAsString().trim();
            if (text.startsWith("#")) {
                text = text.substring(1);
            }
            if (text.length() == 6) {
                return (0xFF << 24) | Integer.parseInt(text, 16);
            }
            if (text.length() == 8) {
                return (int) Long.parseLong(text, 16);
            }
        } catch (Exception ignored) {
            return null;
        }
        return null;
    }
}
