# Neuer Client

Neuer Client is a private Fabric client for combat automation, movement utilities, rendering tools, and quality-of-life systems for PvP-oriented gameplay. The click GUI, ESP, nametags, and HUD overlays are rendered with Dear ImGui rather than vanilla widgets.

> **Personal build.** This is a renamed build of **Marlow Client V4** by [nxghtCry0](https://github.com/nxghtCry0/marlowwwclient) kept for private use. The upstream NPSL 1.0 license permits private modification but forbids publishing or sharing modified versions — do not distribute this build.

## Technology Stack

- Java 25 source/target level, matching Minecraft 26.3 bytecode (run Gradle on JDK 25+)
- Gradle 9.2.1 with Fabric Loom `1.15-SNAPSHOT`
- Fabric Loader `0.19.5`, Fabric API `0.161.0+26.3`
- Minecraft `26.3`
- SpongePowered Mixin + MixinExtras
- Dear ImGui via `imgui-java` `1.86.12`, with LWJGL NanoVG for 2D primitives

Minecraft 26.3 uses SDL3-based windowing, so the ImGui overlay talks to input and rendering through a custom SDL bridge instead of imgui-java's stock GLFW backend.

## Build and Run

```bash
./gradlew build     # release jar in build/libs/neuer-client-1.1.0.jar
./gradlew test      # unit tests
./gradlew runClient # launch the dev client
```

On Windows use `gradlew.bat`. Use the main jar artifact, not the `-sources` jar.

The build has no hardcoded JDK paths. To compile or launch with a specific JDK, set `MOD_COMPILER_JAVA_HOME` / `MOD_RUN_JAVA_HOME`.

## Safety Keys

Two actions are available as bindable cards under **Settings → System**, each with a key bind row:

| Card | Key behaviour | Effect |
| --- | --- | --- |
| **Panic** | fires on press | Disables every module, saves the config in that disabled state, closes any open screen and hides the streamproof surface. |
| **Self Destruct** | press and **hold for 1.5 s** | Runs panic, then wipes every file the client owns. The card button requires a second click within 5 s instead of a hold. |

Self destruct immediately removes `config/neuerclient/` (config, key, profiles, friends, filters, macros, GUI prefs, recommended configs, detection rules), the hidden `~/.minecraft_perf_cache` directory and `mc_ec_*` temp artefacts. The mod jar is **not** touched while the game runs, because deleting a loaded jar breaks resource loading on Linux and fails on Windows; it is queued and deleted from a JVM shutdown hook when you close Minecraft. If the OS still holds the file open (typical on Windows), delete it by hand after the game exits. After a wipe the persistence hooks refuse to recreate the files for the rest of the session, so autosave and the shutdown hook cannot bring traces back.

Panic persists the disabled state on purpose: after a panic you start with everything off on the next launch. Use a saved profile in the config center to restore your setup.

## Data Files

Everything the client reads or writes lives in the game directory. Nothing is fetched from the network.

| Path | Purpose |
| --- | --- |
| `config/neuerclient/config.enc` | Saved config (encrypted) |
| `config/neuerclient/config.key` | Per-install AES-256-GCM key, generated on first use |
| `config/neuerclient/configs/*.json` | Recommended configs; replaces the bundled set when present |
| `config/neuerclient/detection_db.txt` | Detection rules; replaces the bundled set when present |

Configs are encrypted with AES-256-GCM using the per-install key and a fresh IV per payload. Configs written by older builds (static key) still load, and are re-encrypted with the install key on first load.

Detection rules are one per line, `<module>|<server>`, for example `KillAura|*example.com`. Rules bundled in the jar ship as documentation only; add your own.

## Project Layout

```
src/main/java/com/neuer/          mod entrypoint (Neuer)
src/client/java/com/neuer/client/ client initializer, module system, ImGui UI, render, config
src/client/java/com/neuer/mixin/  client mixins
src/main/resources/               fabric.mod.json, assets (fonts, shaders, textures, sounds), bundled configs and rules
src/test/java/com/neuer/          unit tests
.github/workflows/build.yml       CI: build + test on every push
```

## Feature Catalog

The client registers **119 modules** across 16 categories:

- **Combat** – AimAssist, AutoClicker, Triggerbot, HitSelect, STap, WTap, Weapons, AutoArmor, BowAimbot, AutoShieldBreaker, NoHitDelay
- **Crystal** – CrystalAura, AutoPlaceCrystal, AutoHitCrystal, CrystalHelper, Surround
- **Mace** – AutoMace, AutoMaceCounter, AutoWindcharge, LungeSwap, PearlCatch
- **Movement** – AntiAFK, AutoWalk, NoSlow, FakeLag, ElytraBoost, ElytraBounce
- **Render** – ESP, BlockESP, StorageESP, Chams, Nametags, Tracers, Trajectories, Xray, TrueSight, Freecam, Fullbright, HandView, LowFire, NoParticles
- **HUD** – ArrayList, ArmorHUD, TargetHUD, KeybindList, HUDEditor
- **World** – Scaffold, FastPlace, FastBreak, ChestStealer, AutoSign, Automine
- **Exploit** – Reach, Backtrack, Blink, AttributeSwap, HitSwap, BreachSwap
- **Utility** – AutoTotem, AutoTool, AutoDHand, AutoDrain, AutoMLG, AutoWeb, BridgeAssist, PearlGrapple, ShieldDrain, WebStun, JumpReset
- **Blatant** – KillAura, Flight, BoatFly, NoFall, SilentAim, GUIMove, KBDisplacement
- **Misc** – AntiBot, Teams, NameProtect, NPC, DetectionDB, PacketAuditor, ClientSpoof, RecommendedConfigs, Filter
- **Configs** – Configurator (config menu), HUD editor, `/config gui`, `/config export`

Additional systems: persistent configs (`ConfigManager`), friend list (`FriendManager`), target filters (`TargetFilterManager`), macros (`MacroManager`), keybind toggling, and per-tick module hooks.

## Recommended Presets

Every registered module ships two curated configurations in `assets/neuerclient/presets.json`:

- **Legit (bypass)** — conservative values that stay inside Grim / Vulcan / Intave thresholds: near-vanilla reach, slower rotations, human click rates, no packet exploits.
- **Blatant (unsafe)** — the strongest values each module supports: maximum ranges, instant delays, silent rotations.

Apply them per module from the **Recommended Presets** block on any module card, or to every module at once from `Settings → System → Legit Preset` / `Blatant Preset`. Values are matched by setting name and clamped by each option's own bounds, so a new setting never breaks a preset; modules without tunables simply have empty preset objects. Presets only change settings — they never enable a module for you.

## Tests

`./gradlew test` runs the JUnit 5 suite. Coverage focuses on logic that runs outside the game loop:

- `CryptoUtilsTest` — per-install key round trip, fresh IVs, legacy config compatibility, foreign keys, corrupt key files
- `ServerGlobTest` — server address matching, wildcards, ports, lists
- `DetectionRuleTest` — rule parsing, comments, wildcards, malformed input
- `SprintResetTest`, `PearlGrappleTest`, `CombatAndUtilityAdditionsTest` — tap/grapple helpers
- `ModulePresetsTest` — every module has both modes, legit/blatant differ where it matters, values apply and clamp, re-applying is a no-op
- `GlassUiSystemTest` — UI source-layout invariants
- `LicenseMigrationTest` — license and metadata guards

## Credits & License

Original work: **Marlow Client** © 2026 Nxght (nxghtCry0) — <https://github.com/nxghtCry0/marlowwwclient>, distributed under the **Night Public Software License (NPSL) Version 1.0** (see [LICENSE.md](LICENSE.md)).

This repository only changes project identity (mod id `neuerclient`, group `com.neuer`, display name "Neuer Client") plus local-only data sources. The upstream license and copyright notices are kept intact. Under NPSL 1.0 §3(a) and §4(c), modified versions may not be shared, published, or redistributed without the copyright holder's explicit written permission.
