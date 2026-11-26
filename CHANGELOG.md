# Neuer Client 1.1.0

First release of the private Neuer Client build. Do not redistribute (NPSL 1.0).

## Recommended presets
- Every module now ships two curated value sets in `assets/neuerclient/presets.json`: `legit` (bypass-safe, anticheat-aware) and `blatant` (maximum supported values). 119 modules, 523 options covered.
- New **Recommended Presets** block on every module card with one-click Legit / Blatant buttons, plus global **Legit Preset** / **Blatant Preset** cards under `Settings → System`.
- The applier matches options by name and lets each `Setting` clamp the value, so adding or renaming an option can never corrupt a preset. Re-applying an identical preset reports "already at recommended values" instead of a fake change count.
- Presets change settings only; they never toggle modules on or off.

## Descriptions
- Filled in the blank module descriptions (`ArrayList`, `AutoArmor`, `AutoShieldBreaker`, `AutoTool`, `Blink`, `BreachSwap`, `Config Menu`, `FastBreak`, `HitSelect`, `NoHitDelay`, `Triggerbot`, `TrueSight`, `AutoSprint`, `NoJumpDelay`, `Theme`) so every card explains what it does.

## Tests
- `ModulePresetsTest`: full coverage check of the preset table, legit/blatant deltas for combat modules, apply + clamp behaviour, and no-op re-application.

## Restored modules
- Registered the seven module classes that were never wired into the manager: `AutoArmor`, `AutoTool`, `BowAimbot`, `FastBreak`, `NoHitDelay`, `NoSlow` and `TrueSight`.
- `NoSlow` had no implementation at all; it now has a real one (`NoSlowMixin` overrides `LocalPlayer.itemUseSpeedMultiplier`) and a usable `Multiplier` range of 0.2–1.0.
- The `FastBreak` and `TrueSight` mixin hooks are live again instead of null-guarded dead code. Registry is now 119 modules.

## Safety
- **Panic** (`Settings → System → Panic`): disables every module, saves the config in that state, closes the GUI and hides the streamproof surface. Fires on key press.
- **Self Destruct** (`Settings → System → Self Destruct`): runs panic, then deletes the config directory, hidden cache and temp artefacts. The mod jar is queued and removed from a JVM shutdown hook instead of being deleted mid-session, which broke resource loading on Linux and failed on Windows. Key requires a 1.5 s hold, the card requires a second click; persistence hooks are disabled afterwards so nothing is recreated.

# Neuer Client 1.0.0

Private build for personal use. Do not redistribute (NPSL 1.0).

## Project identity
- Mod id `neuerclient`, package `com.neuer.*`, artifact `neuer-client-1.0.0.jar`, group `com.neuer`.
- Display name "Neuer Client" across the title screen, glass UI, HUD and chat feedback.

## Security
- Config encryption moved to AES-256-GCM with a per-install random key (`config/neuerclient/config.key`) and a fresh IV per payload.
- Legacy static-key configs, clipboard strings and bundled recommended configs still decrypt; `config.enc` is re-encrypted with the new key on first load.

## Independence (no upstream services)
- `RecommendedConfigs` reads `config/neuerclient/configs/*.json`, falling back to configs bundled in the jar. No GitHub calls.
- `DetectionDB` reads `config/neuerclient/detection_db.txt`, falling back to bundled rules. No GitLab calls.
- PSA screen no longer links external sources.

## Build and test
- Compilation target aligned with Minecraft 26.3 (Java 25 bytecode, mixin compatibility level `JAVA_25`); the build now compiles on JDK 25.
- Removed machine-specific JDK paths from Gradle; toolchain overrides now come from `MOD_COMPILER_JAVA_HOME` / `MOD_RUN_JAVA_HOME`.
- Added a CI workflow that builds and tests on push.
- Added tests for config crypto (round trip, random IV, legacy compatibility, foreign key, corrupt key file), server glob matching and detection rule parsing.

## Removed
- Stale root copy of the client initializer, `scratch/`, the uncompiled `net/` renderer copy, Eclipse `bin/` output, and the upstream `configs/` folder (now bundled as a resource).

---

# Upstream Marlow Client v3.1 Changelog

This is a minor release (v3.1) adding the new WebStun module and improving the LungeAssist module.

## 🚀 New Features

### **WebStun**
- **Automatic Cobweb Placement:** Places a cobweb directly under target player's feet when their shield is disabled by AutoShieldBreaker or when hit.
- **Anti-Cheat Safe:** Spans actions across multiple ticks using a multi-tick state machine.
- **Silent Rotations & Placements:** Uses exact GCD-snapped rotations and post-movement callbacks to place blocks against solid support blocks, preventing GrimAC alerts.

## 🛠️ Bug Fixes & Improvements

### **LungeAssist**
- **Jumping Apex Logic:** Fixed LungeAssist so that if enabled while jumping/falling, it waits for the apex of the jump before initiating the spear lunge attack.
- **GPL-3 Compliance:** Updated base implementation to align with project licensing constraints.

### **Bypass Infrastructure**
- **MultiPlayerGameMode client-carried sync:** Prevented duplicate selection packets (BadPacketsA bypass) by reflectively syncing carried index fields.
- **Mixin Optimization:** Replaced static mixin fields with localized manager references to prevent loader crashes.
