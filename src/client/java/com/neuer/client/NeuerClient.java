package com.neuer.client;

import com.neuer.client.glassui.bind.UiInputBind;
import com.neuer.client.glassui.bridge.ClientGuiRegistry;
import com.neuer.client.glassui.bridge.ClientGuiScreen;
import com.neuer.client.glassui.bridge.ClientKeybindBridge;
import com.neuer.client.glassui.bridge.ClientNotificationService;
import com.neuer.client.glassui.bridge.ClientUiSounds;
import com.neuer.client.glassui.render.StreamproofOverlay;
import com.neuer.client.glassui.screen.GlassUiScreen;
import com.neuer.client.macro.MacroManager;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.module.ModuleManager;
import com.neuer.client.module.impl.AimAssist;
import com.neuer.client.module.impl.Anchor;
import com.neuer.client.module.impl.AntiAFK;
import com.neuer.client.module.impl.AntiBot;
import com.neuer.client.module.impl.AntiTranslationKey;
import com.neuer.client.module.impl.ArmorHUD;
import com.neuer.client.module.impl.ArrayListMod;
import com.neuer.client.module.impl.AttributeSwap;
import com.neuer.client.module.impl.AutoClicker;
import com.neuer.client.module.impl.AutoDHand;
import com.neuer.client.module.impl.AutoDrain;
import com.neuer.client.module.impl.AutoFish;
import com.neuer.client.module.impl.AutoHitCrystal;
import com.neuer.client.module.impl.AutoMLG;
import com.neuer.client.module.impl.AutoMace;
import com.neuer.client.module.impl.AutoMaceCounter;
import com.neuer.client.module.impl.AutoPlaceCrystal;
import com.neuer.client.module.impl.AutoShieldBreaker;
import com.neuer.client.module.impl.AutoSign;
import com.neuer.client.module.impl.AutoTotem;
import com.neuer.client.module.impl.AutoTrade;
import com.neuer.client.module.impl.AutoArmor;
import com.neuer.client.module.impl.AutoTool;
import com.neuer.client.module.impl.AutoWalk;
import com.neuer.client.module.impl.BowAimbot;
import com.neuer.client.module.impl.AutoWeb;
import com.neuer.client.module.impl.AutoWindcharge;
import com.neuer.client.module.impl.Automine;
import com.neuer.client.module.impl.Backtrack;
import com.neuer.client.module.impl.BlinkModule;
import com.neuer.client.module.impl.BlockESP;
import com.neuer.client.module.impl.BoatFly;
import com.neuer.client.module.impl.BreachSwap;
import com.neuer.client.module.impl.BridgeAssist;
import com.neuer.client.module.impl.Bypass;
import com.neuer.client.module.impl.CartRefill;
import com.neuer.client.module.impl.Chams;
import com.neuer.client.module.impl.ClientSpoof;
import com.neuer.client.module.impl.Configurator;
import com.neuer.client.module.impl.CrystalAura;
import com.neuer.client.module.impl.CrystalHelper;
import com.neuer.client.module.impl.DetectionAlert;
import com.neuer.client.module.impl.ESP;
import com.neuer.client.module.impl.ElytraBoost;
import com.neuer.client.module.impl.ElytraBounce;
import com.neuer.client.module.impl.ElytraSwapMacro;
import com.neuer.client.module.impl.FakeLag;
import com.neuer.client.module.impl.FastBreak;
import com.neuer.client.module.impl.FastPlace;
import com.neuer.client.module.impl.FastThrowables;
import com.neuer.client.module.impl.Flight;
import com.neuer.client.module.impl.Freecam;
import com.neuer.client.module.impl.FriendProtector;
import com.neuer.client.module.impl.Fullbright;
import com.neuer.client.module.impl.GUI;
import com.neuer.client.module.impl.GUIMove;
import com.neuer.client.module.impl.GhostBlockMacro;
import com.neuer.client.module.impl.HUDEditor;
import com.neuer.client.module.impl.HandView;
import com.neuer.client.module.impl.HitSelect;
import com.neuer.client.module.impl.HitSwap;
import com.neuer.client.module.impl.Hitboxes;
import com.neuer.client.module.impl.InstaCart;
import com.neuer.client.module.impl.InventoryClean;
import com.neuer.client.module.impl.InventoryFill;
import com.neuer.client.module.impl.JumpReset;
import com.neuer.client.module.impl.KeybindLava;
import com.neuer.client.module.impl.KeybindList;
import com.neuer.client.module.impl.KeybindWater;
import com.neuer.client.module.impl.KeybindWeb;
import com.neuer.client.module.impl.KillAura;
import com.neuer.client.module.impl.KillSounds;
import com.neuer.client.module.impl.KnockbackDisplacement;
import com.neuer.client.module.impl.Loot;
import com.neuer.client.module.impl.LowFire;
import com.neuer.client.module.impl.LungeAssist;
import com.neuer.client.module.impl.MaceDTap;
import com.neuer.client.module.impl.Menu;
import com.neuer.client.module.impl.NPC;
import com.neuer.client.module.impl.NameProtect;
import com.neuer.client.module.impl.Nametags;
import com.neuer.client.module.impl.NoHitDelay;
import com.neuer.client.module.impl.NoFall;
import com.neuer.client.module.impl.NoSlow;
import com.neuer.client.module.impl.NoParticles;
import com.neuer.client.module.impl.NoTotemPop;
import com.neuer.client.module.impl.PacketAuditor;
import com.neuer.client.module.impl.PearlBind;
import com.neuer.client.module.impl.PearlCatch;
import com.neuer.client.module.impl.PearlGrapple;
import com.neuer.client.module.impl.Reach;
import com.neuer.client.module.impl.RecommendedConfigs;
import com.neuer.client.module.impl.RenderOptimizer;
import com.neuer.client.module.impl.STap;
import com.neuer.client.module.impl.Scaffold;
import com.neuer.client.module.impl.SelfShader;
import com.neuer.client.module.impl.ShieldDrain;
import com.neuer.client.module.impl.SilentAim;
import com.neuer.client.module.impl.SpawnerFinder;
import com.neuer.client.module.impl.SpearMaceDTap;
import com.neuer.client.module.impl.SprintResetController;
import com.neuer.client.module.impl.StorageESP;
import com.neuer.client.module.impl.Surround;
import com.neuer.client.module.impl.SuspiciousChunks;
import com.neuer.client.module.impl.TargetHUD;
import com.neuer.client.module.impl.Teams;
import com.neuer.client.module.impl.Tracers;
import com.neuer.client.module.impl.Trajectories;
import com.neuer.client.module.impl.Triggerbot;
import com.neuer.client.module.impl.TrueSight;
import com.neuer.client.module.impl.VapeMacro;
import com.neuer.client.module.impl.WTap;
import com.neuer.client.module.impl.WeakDevice;
import com.neuer.client.module.impl.Weapons;
import com.neuer.client.module.impl.WebStun;
import com.neuer.client.module.impl.XbowCart;
import com.neuer.client.module.impl.Xray;
import com.neuer.client.setting.ConfigManager;
import com.neuer.client.setting.Setting;
import com.neuer.client.setting.SettingsManager;
import com.neuer.client.utils.ClientWipe;
import com.neuer.client.utils.FriendManager;
import com.neuer.client.utils.InputUtil;
import com.neuer.client.utils.ModuleUtils;
import com.neuer.client.utils.PanicHandler;
import com.neuer.client.utils.RotationManager;
import com.neuer.client.utils.SpoofManager;
import com.neuer.client.utils.TargetFilterManager;
import com.neuer.client.utils.remnant.Fonts;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.awt.Color;
import java.util.ArrayList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NeuerClient implements ClientModInitializer {
    public static NeuerClient INSTANCE;

    private static final long SELF_DESTRUCT_HOLD_MS = 1_500L;

    public ModuleManager moduleManager;
    public SettingsManager settingsManager;

    private boolean prevNotificationsBindDown = false;
    private boolean prevStreamproofBindDown = false;
    private boolean prevPanicBindDown = false;
    private long selfDestructHoldStart = 0L;

    private void addColorSettings(SettingsManager sm, Module module, String colorName, int r, int g, int b) {
        sm.rSetting(new Setting(colorName, module, new Color(r, g, b)));
    }

    private void tickSystemKeybinds(Minecraft client) {
        if (client == null || client.player == null || client.gui == null || client.gui.screen() != null) {
            prevNotificationsBindDown = false;
            prevStreamproofBindDown = false;
            return;
        }
        ClientKeybindBridge bridge = ClientKeybindBridge.INSTANCE;

        UiInputBind notifBind = bridge.getBind("notifications");
        boolean notifDown = notifBind.isBound() && InputUtil.isDown(notifBind.toModuleKeyBind());
        if (notifDown && !prevNotificationsBindDown) {
            boolean next = !ClientGuiRegistry.isFeatureEnabled("notifications");
            ClientGuiRegistry.setFeatureEnabled("notifications", next, true);
        }
        prevNotificationsBindDown = notifDown;

        UiInputBind spBind = bridge.getBind("streamproof_menu");
        boolean spDown = spBind.isBound() && InputUtil.isDown(spBind.toModuleKeyBind());
        if (spDown && !prevStreamproofBindDown) {
            boolean next = !ClientGuiRegistry.isFeatureEnabled("streamproof_menu");
            ClientGuiRegistry.setFeatureEnabled("streamproof_menu", next, true);
            if (!next) {
                StreamproofOverlay.hide();
            }
        }
        prevStreamproofBindDown = spDown;
    }

    /** Panic fires on press; self destruct needs the key held so it cannot fire on a stray tap. */
    private void tickSafetyKeybinds(Minecraft client) {
        if (client == null || client.player == null || client.gui == null) {
            prevPanicBindDown = false;
            selfDestructHoldStart = 0L;
            return;
        }
        Screen active = client.gui.screen();
        boolean typingOrBinding = (active instanceof GlassUiScreen glass && glass.isInputCaptured())
                || active instanceof ChatScreen;
        if (typingOrBinding || ClientWipe.isWiped()) {
            prevPanicBindDown = false;
            selfDestructHoldStart = 0L;
            return;
        }

        ClientKeybindBridge bridge = ClientKeybindBridge.INSTANCE;

        UiInputBind panicBind = bridge.getBind("panic");
        boolean panicDown = panicBind.isBound() && InputUtil.isDown(panicBind.toModuleKeyBind());
        if (panicDown && !prevPanicBindDown) {
            PanicHandler.panic();
        }
        prevPanicBindDown = panicDown;

        UiInputBind wipeBind = bridge.getBind("self_destruct");
        boolean wipeDown = wipeBind.isBound() && InputUtil.isDown(wipeBind.toModuleKeyBind());
        long now = System.currentTimeMillis();
        if (!wipeDown) {
            selfDestructHoldStart = 0L;
        } else if (selfDestructHoldStart == 0L) {
            selfDestructHoldStart = now;
        } else if (now - selfDestructHoldStart >= SELF_DESTRUCT_HOLD_MS) {
            selfDestructHoldStart = 0L;
            PanicHandler.selfDestruct();
        }
    }

    @Override
    public void onInitializeClient() {
        Module.mc = Minecraft.getInstance();
        INSTANCE = this;
        new Fonts();
        moduleManager = new ModuleManager();
        settingsManager = new SettingsManager();

        Module autoSprint = new Module("AutoSprint", Category.Movement,
                "Sprints for you whenever you move forward.");
        Module noJumpDelay = new Module("NoJumpDelay", Category.Movement,
                "Removes the vanilla cooldown between jumps.");
        Module aimAssist = new AimAssist();
        Module triggerbot = new Triggerbot();
        Module autoWeb = new AutoWeb();
        Module shieldDrain = new ShieldDrain();
        Module attributeSwap = new AttributeSwap();
        Module autoMaceCounter = new AutoMaceCounter();
        Module autoDrain = new AutoDrain();
        Module wTap = new WTap();
        Module hitSelect = new HitSelect();
        Module autoShieldBreaker = new AutoShieldBreaker();
        Module kbDisplacement = new KnockbackDisplacement();
        Module arrayListMod = new ArrayListMod();
        Module nameProtect = new NameProtect();
        Module breachSwap = new BreachSwap();

        Module lungeAssist = new LungeAssist();
        Module webStun = new WebStun();
        Module autoMace = new AutoMace();
        Module elytraSwapMacro = new ElytraSwapMacro();
        Module elytraBounce = new ElytraBounce();
        Module pearlCatch = new PearlCatch();
        Module jumpReset = new JumpReset();
        Module pearlGrapple = new PearlGrapple();

        Module autoClicker = new AutoClicker();
        Module fastPlace = new FastPlace();
        Module bridgeAssist = new BridgeAssist();
        Module scaffold = new Scaffold();

        Module xray = new Xray();
        Module elytraBoost = new ElytraBoost();

        Module configurator = new Configurator();
        Module fullbright = new Fullbright();
        Module reach = new Reach();
        Module handView = new HandView();
        Module esp = new ESP();
        Module noParticles = new NoParticles();
        Module lowFire = new LowFire();
        Module noTotemPop = new NoTotemPop();
        Module tracers = new Tracers();
        Module nametags = new Nametags();
        Module silentAim = new SilentAim();
        Module storageESP = new StorageESP();
        Module blockESP = new BlockESP();
        Module spawnerFinder = new SpawnerFinder();
        Module suspiciousChunks = new SuspiciousChunks();
        Module killAura = new KillAura();
        Module detectionAlert = new DetectionAlert();
        Module backtrack = new Backtrack();
        Module pearlBind = new PearlBind();
        Module autoTotem = new AutoTotem();
        Module anchor = new Anchor();
        Module crystalAura = new CrystalAura();
        Module crystalHelper = new CrystalHelper();
        Module antibot = new AntiBot();
        Module teams = new Teams();
        Module blink = new BlinkModule();
        Module fakeLag = new FakeLag();
        Module packetAuditor = new PacketAuditor();
        Module autoHitCrystal = new AutoHitCrystal();
        Module autoPlaceCrystal = new AutoPlaceCrystal();
        Module fastThrowables = new FastThrowables();
        Module sTap = new STap();
        Module chams = new Chams();
        Module trajectories = new Trajectories();
        Module surround = new Surround();

        moduleManager.modules.add(autoSprint);
        moduleManager.modules.add(noJumpDelay);
        moduleManager.modules.add(aimAssist);
        moduleManager.modules.add(wTap);
        moduleManager.modules.add(sTap);
        moduleManager.modules.add(triggerbot);
        moduleManager.modules.add(autoWeb);
        moduleManager.modules.add(shieldDrain);
        moduleManager.modules.add(attributeSwap);
        moduleManager.modules.add(autoMaceCounter);
        moduleManager.modules.add(autoDrain);
        moduleManager.modules.add(hitSelect);
        moduleManager.modules.add(autoShieldBreaker);
        moduleManager.modules.add(kbDisplacement);
        moduleManager.modules.add(arrayListMod);
        moduleManager.modules.add(nameProtect);
        moduleManager.modules.add(breachSwap);

        moduleManager.modules.add(lungeAssist);
        moduleManager.modules.add(webStun);
        moduleManager.modules.add(autoMace);
        moduleManager.modules.add(new SpearMaceDTap());
        moduleManager.modules.add(new MaceDTap());
        moduleManager.modules.add(elytraSwapMacro);
        moduleManager.modules.add(elytraBounce);
        moduleManager.modules.add(pearlCatch);
        moduleManager.modules.add(jumpReset);
        moduleManager.modules.add(pearlGrapple);
        moduleManager.modules.add(new Hitboxes());
        moduleManager.modules.add(new KillSounds());

        // Modules that previously existed only as unreferenced classes.
        moduleManager.modules.add(new AutoArmor());
        moduleManager.modules.add(new AutoTool());
        moduleManager.modules.add(new BowAimbot());
        moduleManager.modules.add(new FastBreak());
        moduleManager.modules.add(new NoHitDelay());
        moduleManager.modules.add(new NoSlow());
        moduleManager.modules.add(new TrueSight());

        moduleManager.modules.add(autoClicker);
        moduleManager.modules.add(fastPlace);
        moduleManager.modules.add(bridgeAssist);
        moduleManager.modules.add(scaffold);
        moduleManager.modules.add(xray);
        moduleManager.modules.add(elytraBoost);
        moduleManager.modules.add(configurator);

        moduleManager.modules.add(fullbright);
        moduleManager.modules.add(reach);
        moduleManager.modules.add(handView);
        moduleManager.modules.add(noParticles);
        moduleManager.modules.add(lowFire);
        moduleManager.modules.add(noTotemPop);
        moduleManager.modules.add(detectionAlert);
        moduleManager.modules.add(esp);
        moduleManager.modules.add(chams);
        moduleManager.modules.add(tracers);
        moduleManager.modules.add(nametags);
        moduleManager.modules.add(silentAim);
        moduleManager.modules.add(storageESP);
        moduleManager.modules.add(blockESP);
        moduleManager.modules.add(spawnerFinder);
        moduleManager.modules.add(suspiciousChunks);
        moduleManager.modules.add(killAura);
        moduleManager.modules.add(backtrack);
        moduleManager.modules.add(pearlBind);
        moduleManager.modules.add(autoTotem);
        moduleManager.modules.add(anchor);
        moduleManager.modules.add(crystalAura);
        moduleManager.modules.add(crystalHelper);
        moduleManager.modules.add(autoHitCrystal);
        moduleManager.modules.add(autoPlaceCrystal);
        moduleManager.modules.add(fastThrowables);
        moduleManager.modules.add(blink);
        moduleManager.modules.add(fakeLag);
        moduleManager.modules.add(packetAuditor);
        moduleManager.modules.add(trajectories);
        moduleManager.modules.add(surround);
        Module hitSwap = new HitSwap();
        moduleManager.modules.add(hitSwap);
        moduleManager.modules.add(antibot);
        moduleManager.modules.add(teams);
        Module renderOptimizer = new RenderOptimizer();
        moduleManager.modules.add(renderOptimizer);
        Module automine = new Automine();
        Module autowalk = new AutoWalk();
        Module antiAfk = new AntiAFK();
        Module guimove = new GUIMove();
        Module autosign = new AutoSign();
        Module freecam = new Freecam();
        Module friendProtector = new FriendProtector();
        moduleManager.modules.add(automine);
        moduleManager.modules.add(autowalk);
        moduleManager.modules.add(antiAfk);
        moduleManager.modules.add(guimove);
        moduleManager.modules.add(autosign);
        moduleManager.modules.add(freecam);
        moduleManager.modules.add(friendProtector);
        moduleManager.modules.add(new AntiTranslationKey());

        Module autoDHand = new AutoDHand();
        Module autoWindcharge = new AutoWindcharge();
        Module boatFly = new BoatFly();
        Module flight = new Flight();
        Module weapons = new Weapons();
        Module bypassModule = new Bypass();
        Module npcModule = new NPC();
        Module weakDevice = new WeakDevice();
        moduleManager.modules.add(weakDevice);

        Module autoMLG = new AutoMLG();
        moduleManager.modules.add(autoMLG);
        moduleManager.modules.add(autoDHand);
        moduleManager.modules.add(autoWindcharge);
        moduleManager.modules.add(boatFly);
        moduleManager.modules.add(flight);
        moduleManager.modules.add(weapons);
        moduleManager.modules.add(bypassModule);
        moduleManager.modules.add(npcModule);
        Module recommendedConfigs = new RecommendedConfigs();
        moduleManager.modules.add(recommendedConfigs);

        Module keybindWater = new KeybindWater();
        Module noFall = new NoFall();
        moduleManager.modules.add(noFall);
        Module keybindLava = new KeybindLava();
        Module keybindWeb = new KeybindWeb();
        Module vapeMacro = new VapeMacro();
        Module keybindList = new KeybindList();

        moduleManager.modules.add(keybindWater);
        moduleManager.modules.add(keybindLava);
        moduleManager.modules.add(keybindWeb);
        moduleManager.modules.add(vapeMacro);
        moduleManager.modules.add(keybindList);

        settingsManager.rSetting(new Setting("Scale", keybindList, 0.75, 0.3, 1.5, false));
        settingsManager.rSetting(new Setting("Only Enabled", keybindList, false));

        Module loot = new Loot();
        moduleManager.modules.add(loot);

        Module instaCart = new InstaCart();
        Module cartRefill = new CartRefill();
        Module xbowCart = new XbowCart();
        moduleManager.modules.add(instaCart);
        moduleManager.modules.add(cartRefill);
        moduleManager.modules.add(xbowCart);

        Module targetHUD = new TargetHUD();
        Module armorHUD = new ArmorHUD();
        Module hudEditor = new HUDEditor();
        Module ghostBlockMacro = new GhostBlockMacro();
        moduleManager.modules.add(targetHUD);
        moduleManager.modules.add(armorHUD);
        moduleManager.modules.add(hudEditor);
        moduleManager.modules.add(ghostBlockMacro);
        moduleManager.modules.add(new AutoFish());
        moduleManager.modules.add(new AutoTrade());
        moduleManager.modules.add(new InventoryClean());
        moduleManager.modules.add(new InventoryFill());

        Module clientSpoof = new ClientSpoof();
        moduleManager.modules.add(clientSpoof);
        ArrayList<String> csModes = new ArrayList<>();
        csModes.add("Lunar");
        csModes.add("Vanilla");
        csModes.add("Fabric");
        csModes.add("Custom");
        settingsManager.rSetting(new Setting("Mode", clientSpoof, "Lunar", csModes));
        settingsManager.rSetting(new Setting("Custom Brand", clientSpoof, "lunarclient:v2.12.0-2629", true));

        Module theme = new Module("Theme", Category.Render, "Customizes the client's UI colors and animations.");
        theme.setDescription("Shared accent colour, background alpha and animation speed for every screen.");
        moduleManager.modules.add(theme);
        settingsManager.rSetting(new Setting("Accent Color", theme, new Color(42, 168, 255)));
        settingsManager.rSetting(new Setting("Background Alpha", theme, 240.0, 0.0, 255.0, true));
        settingsManager.rSetting(new Setting("Anim Speed", theme, 5.0, 1.0, 10.0, false));

        settingsManager.rSetting(new Setting("Target Item", vapeMacro, "pearl", true));

        ArrayList<String> acButtons = new ArrayList<>();
        acButtons.add("Left");
        acButtons.add("Right");
        settingsManager.rSetting(new Setting("Button", autoClicker, "Left", acButtons));
        settingsManager.rSetting(new Setting("Min CPS", autoClicker, 9.0, 1.0, 20.0, true));
        settingsManager.rSetting(new Setting("Max CPS", autoClicker, 14.0, 1.0, 20.0, true));
        settingsManager.rSetting(new Setting("Require Click", autoClicker, true));
        settingsManager.rSetting(new Setting("Right Clicker", autoClicker, false));

        settingsManager.rSetting(new Setting("Delay (Ticks)", fastPlace, 0.0, 0.0, 3.0, true));

        ArrayList<String> safewalkModes = new ArrayList<>();
        safewalkModes.add("Normal");
        safewalkModes.add("Blatant");
        settingsManager.rSetting(new Setting("Mode", bridgeAssist, "Normal", safewalkModes));
        settingsManager.rSetting(new Setting("Edge Distance", bridgeAssist, 0.25, 0.00, 0.30, false));
        settingsManager.rSetting(new Setting("Pitch Check", bridgeAssist, true));

        settingsManager.rSetting(new Setting("Firework Level", elytraBoost, 1.0, 0.0, 3.0, true));
        settingsManager.rSetting(new Setting("Play Sound", elytraBoost, true));

        ArrayList<String> fbModes = new ArrayList<>();
        fbModes.add("Night Vision");
        fbModes.add("Gamma");
        settingsManager.rSetting(new Setting("Mode", fullbright, "Night Vision", fbModes));

        ArrayList<String> wtapModes = new ArrayList<>();
        wtapModes.add("Normal");
        wtapModes.add("Silent");
        wtapModes.add("Auto");
        settingsManager.rSetting(new Setting("WTap Mode", wTap, "Normal", wtapModes));
        settingsManager.rSetting(new Setting("Chance (%)", wTap, 100.0, 0.0, 100.0, true));
        settingsManager.rSetting(new Setting("Wait Delay (ms)", wTap, 0.0, 0.0, 300.0, true));
        settingsManager.rSetting(new Setting("Action Delay (ms)", wTap, 85.0, 20.0, 350.0, true));
        settingsManager.rSetting(new Setting("Jitter (ms)", wTap, 15.0, 0.0, 150.0, true));
        settingsManager.rSetting(new Setting("Fresh Hits Only", wTap, true));
        settingsManager.rSetting(new Setting("Only Players", wTap, true));
        settingsManager.rSetting(new Setting("Sprint Only", wTap, true));

        ArrayList<String> hsModes = new ArrayList<>();
        hsModes.add("Pause");
        hsModes.add("Dynamic");
        settingsManager.rSetting(new Setting("Mode", hitSelect, "Pause", hsModes));
        settingsManager.rSetting(new Setting("Auto Punish", hitSelect, false));
        settingsManager.rSetting(new Setting("Punish Delay (Ticks)", hitSelect, 3.0, 0.0, 10.0, true));

        ArrayList<String> asbModes = new ArrayList<>();
        asbModes.add("Swap");
        asbModes.add("Silent");
        settingsManager.rSetting(new Setting("Mode", autoShieldBreaker, "Swap", asbModes));
        settingsManager.rSetting(new Setting("Delay (ms)", autoShieldBreaker, 50.0, 0.0, 1000.0, true));
        settingsManager.rSetting(new Setting("Swap Back", autoShieldBreaker, true));
        settingsManager.rSetting(new Setting("Swap Back Delay (ms)", autoShieldBreaker, 100.0, 0.0, 1000.0, true));
        settingsManager.rSetting(new Setting("Reaction Time (ms)", autoShieldBreaker, 100.0, 0.0, 1000.0, true));

        settingsManager.rSetting(new Setting("Delay (ms)", autoWeb, 250.0, 0.0, 1000.0, true));
        settingsManager.rSetting(new Setting("Movement Correction", autoWeb, true));
        settingsManager.rSetting(new Setting("Smooth Rotation", autoWeb, 2.0, 1.0, 10.0, false));
        settingsManager.rSetting(new Setting("Swap Back", autoWeb, true));

        ArrayList<String> kbModes = new ArrayList<>();
        kbModes.add("Pull");
        kbModes.add("Upward");
        kbModes.add("Horizontal");
        kbModes.add("Custom");
        settingsManager.rSetting(new Setting("Mode", kbDisplacement, "Pull", kbModes));
        settingsManager.rSetting(new Setting("Auto Sprint", kbDisplacement, true));
        settingsManager.rSetting(new Setting("Delay (Ticks)", kbDisplacement, 2.0, 0.0, 5.0, true));
        settingsManager.rSetting(new Setting("Cooldown (Ticks)", kbDisplacement, 15.0, 0.0, 40.0, true));
        settingsManager.rSetting(new Setting("Custom Yaw", kbDisplacement, 0.0, -180.0, 180.0, true));
        settingsManager.rSetting(new Setting("Custom Pitch", kbDisplacement, 0.0, -90.0, 90.0, true));

        ArrayList<String> alAlignments = new ArrayList<>();
        alAlignments.add("Left");
        alAlignments.add("Right");
        settingsManager.rSetting(new Setting("Alignment", arrayListMod, "Left", alAlignments));
        settingsManager.rSetting(new Setting("Sync Theme", arrayListMod, true));
        settingsManager.rSetting(new Setting("Text Color", arrayListMod, new Color(42, 168, 255)));
        settingsManager.rSetting(new Setting("Y Offset", arrayListMod, 5.0, 0.0, 100.0, true));
        settingsManager.rSetting(new Setting("Scale", arrayListMod, 1.0, 0.5, 1.5, false));

        ArrayList<String> npNames = new ArrayList<>();
        npNames.add("Neuer");
        npNames.add("Hidden");
        npNames.add("You");
        settingsManager.rSetting(new Setting("Name", nameProtect, "Neuer", npNames));
        ArrayList<String> npModes = new ArrayList<>();
        npModes.add("Self");
        npModes.add("Everyone");
        settingsManager.rSetting(new Setting("Mode", nameProtect, "Self", npModes));
        settingsManager.rSetting(new Setting("TIP: Hide chat in Accessibility Settings", nameProtect, false));

        ArrayList<String> bsModes = new ArrayList<>();
        bsModes.add("Swap");
        bsModes.add("Silent");
        settingsManager.rSetting(new Setting("Mode", breachSwap, "Swap", bsModes));
        settingsManager.rSetting(new Setting("Swap Back", breachSwap, true));
        settingsManager.rSetting(new Setting("Swap Back Delay (ms)", breachSwap, 100.0, 0.0, 1000.0, true));

        settingsManager.rSetting(new Setting("AutoJump", lungeAssist, true));

        ArrayList<String> jrModes = new ArrayList<>();
        jrModes.add("Smart");
        jrModes.add("Classic");
        jrModes.add("Blatant");
        settingsManager.rSetting(new Setting("Mode", jumpReset, "Smart", jrModes));
        settingsManager.rSetting(new Setting("Reset Hit", jumpReset, 2.0, 1.0, 10.0, true));
        settingsManager.rSetting(new Setting("Max Trade Length", jumpReset, 4.0, 1.0, 20.0, true));
        settingsManager.rSetting(new Setting("Short Trade Reset", jumpReset, true));
        settingsManager.rSetting(new Setting("Trade Timeout (ms)", jumpReset, 500.0, 100.0, 2000.0, true));
        settingsManager.rSetting(new Setting("Delay (Ticks)", jumpReset, 0.0, 0.0, 5.0, true));
        settingsManager.rSetting(new Setting("Chance (%)", jumpReset, 100.0, 0.0, 100.0, true));
        settingsManager.rSetting(new Setting("Velocity Threshold", jumpReset, 0.1, 0.0, 1.0, false));

        ArrayList<String> afkActions = new ArrayList<>();
        afkActions.add("Cycle");
        afkActions.add("Jump");
        afkActions.add("Swing");
        afkActions.add("Sneak");
        settingsManager.rSetting(new Setting("Action", antiAfk, "Cycle", afkActions));
        settingsManager.rSetting(new Setting("Interval (s)", antiAfk, 5.0, 1.0, 300.0, true));

        settingsManager.rSetting(new Setting("Distance", reach, 0.5, 0.0, 1.0, false));

        settingsManager.rSetting(new Setting("Main Scale X", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Main Scale Y", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Main Scale Z", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Main Pos X", handView, 0.0, -2.0, 2.0, false));
        settingsManager.rSetting(new Setting("Main Pos Y", handView, 0.0, -2.0, 2.0, false));
        settingsManager.rSetting(new Setting("Main Pos Z", handView, 0.0, -2.0, 2.0, false));
        settingsManager.rSetting(new Setting("Off Scale X", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Off Scale Y", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Off Scale Z", handView, 1.0, 0.1, 3.0, false));
        settingsManager.rSetting(new Setting("Off Pos X", handView, 0.0, -2.0, 2.0, false));
        settingsManager.rSetting(new Setting("Off Pos Y", handView, 0.0, -2.0, 2.0, false));
        settingsManager.rSetting(new Setting("Off Pos Z", handView, 0.0, -2.0, 2.0, false));

        ArrayList<String> espModes = new ArrayList<>();
        espModes.add("Outline");
        espModes.add("2D");
        espModes.add("Hybrid");
        espModes.add("Glow");
        espModes.add("Shader");
        settingsManager.rSetting(new Setting("Mode", esp, "Outline", espModes));
        settingsManager.rSetting(new Setting("Show Mobs", esp, false));
        settingsManager.rSetting(new Setting("Fill", esp, true));
        settingsManager.rSetting(new Setting("Health", esp, true));
        settingsManager.rSetting(new Setting("Names", esp, true));
        settingsManager.rSetting(new Setting("Outline Thickness", esp, 1.0, 1.0, 5.0, true));
        settingsManager.rSetting(new Setting("Corner Gap", esp, 50.0, 10.0, 100.0, true));
        settingsManager.rSetting(new Setting("Border", esp, true));
        settingsManager.rSetting(new Setting("Feather", esp, 3.0, 1.0, 5.0, true));
        settingsManager.rSetting(new Setting("Fill Opacity", esp, 20.0, 0.0, 50.0, true));
        ArrayList<String> espColorModes = new ArrayList<>();
        espColorModes.add("Theme");
        espColorModes.add("Custom");
        espColorModes.add("Per Type");
        settingsManager.rSetting(new Setting("Color Mode", esp, "Theme", espColorModes));
        addColorSettings(settingsManager, esp, "Custom Color", 190, 150, 255);
        addColorSettings(settingsManager, esp, "Player Color", 190, 150, 255);
        addColorSettings(settingsManager, esp, "Hostile Color", 255, 95, 95);
        addColorSettings(settingsManager, esp, "Passive Color", 120, 230, 150);
        addColorSettings(settingsManager, esp, "Neutral Color", 255, 205, 90);
        settingsManager.rSetting(new Setting("Name Tags", esp, true));
        settingsManager.rSetting(new Setting("Tag Distance", esp, true));
        settingsManager.rSetting(new Setting("Tag Health", esp, true));
        settingsManager.rSetting(new Setting("Tag Item", esp, true));

        settingsManager.rSetting(new Setting("Range", backtrack, 3.0, 1.0, 6.0, false));
        settingsManager.rSetting(new Setting("Delay", backtrack, 150.0, 50.0, 500.0, true));
        settingsManager.rSetting(new Setting("Chance", backtrack, 50.0, 0.0, 100.0, true));
        settingsManager.rSetting(new Setting("Attack Timeout", backtrack, 1000.0, 100.0, 5000.0, true));
        settingsManager.rSetting(new Setting("Visualizer", backtrack, false));

        settingsManager.rSetting(new Setting("Crosshair Attach", tracers, true));
        settingsManager.rSetting(new Setting("Show Mobs", tracers, false));
        settingsManager.rSetting(new Setting("Players", nametags, true));
        settingsManager.rSetting(new Setting("Show Mobs", nametags, false));

        ArrayList<String> btModes = new ArrayList<>();
        btModes.add("Latency");
        btModes.add("Pulse");
        settingsManager.rSetting(new Setting("Mode", backtrack, "Latency", btModes));
        settingsManager.rSetting(new Setting("Delay Min (ms)", backtrack, 100.0, 0.0, 2000.0, true));
        settingsManager.rSetting(new Setting("Delay Max (ms)", backtrack, 500.0, 0.0, 2000.0, true));
        settingsManager.rSetting(new Setting("Through Walls", backtrack, false));

        ArrayList<String> blinkModes = new ArrayList<>();
        blinkModes.add("Pulse");
        blinkModes.add("Latency");
        settingsManager.rSetting(new Setting("Mode", blink, "Pulse", blinkModes));
        settingsManager.rSetting(new Setting("Delay Min (ms)", blink, 100.0, 0.0, 2000.0, true));
        settingsManager.rSetting(new Setting("Delay Max (ms)", blink, 500.0, 0.0, 2000.0, true));

        settingsManager.rSetting(new Setting("Range", silentAim, 4.5, 1.0, 8.0, false));
        settingsManager.rSetting(new Setting("FOV", silentAim, 120.0, 10.0, 360.0, true));
        settingsManager.rSetting(new Setting("Players", silentAim, true));
        settingsManager.rSetting(new Setting("Hostile Mobs", silentAim, true));
        settingsManager.rSetting(new Setting("Passive Mobs", silentAim, false));

        settingsManager.rSetting(new Setting("Chest", storageESP, true));
        settingsManager.rSetting(new Setting("Barrel", storageESP, true));
        settingsManager.rSetting(new Setting("Shulker Box", storageESP, true));
        settingsManager.rSetting(new Setting("Ender Chest", storageESP, true));
        settingsManager.rSetting(new Setting("Trapped Chest", storageESP, true));
        settingsManager.rSetting(new Setting("Hopper", storageESP, false));
        settingsManager.rSetting(new Setting("Dispenser", storageESP, false));
        settingsManager.rSetting(new Setting("Dropper", storageESP, false));
        settingsManager.rSetting(new Setting("Furnaces", storageESP, false));
        settingsManager.rSetting(new Setting("Range", storageESP, 64.0, 8.0, 128.0, true));
        settingsManager.rSetting(new Setting("FPS", storageESP, 144.0, 1.0, 1000.0, true));
        settingsManager.rSetting(new Setting("Tracers", storageESP, true));
        settingsManager.rSetting(new Setting("Fill", storageESP, true));
        settingsManager.rSetting(new Setting("Outline", storageESP, true));

        addColorSettings(settingsManager, storageESP, "Chest Color", 255, 165, 0);
        addColorSettings(settingsManager, storageESP, "Barrel Color", 139, 90, 43);
        addColorSettings(settingsManager, storageESP, "Shulker Color", 130, 90, 130);
        addColorSettings(settingsManager, storageESP, "Ender Chest Color", 138, 43, 226);
        addColorSettings(settingsManager, storageESP, "Trapped Chest Color", 255, 0, 0);
        addColorSettings(settingsManager, storageESP, "Hopper Color", 100, 100, 100);
        addColorSettings(settingsManager, storageESP, "Dispenser Color", 128, 128, 128);
        addColorSettings(settingsManager, storageESP, "Dropper Color", 169, 169, 169);
        addColorSettings(settingsManager, storageESP, "Furnace Color", 160, 160, 160);

        ArrayList<String> combatSystems = new ArrayList<>();
        combatSystems.add("Modern (1.9+)");
        combatSystems.add("1.8.9");
        settingsManager.rSetting(new Setting("Combat System", killAura, "Modern (1.9+)", combatSystems));
        ArrayList<String> rotModes = new ArrayList<>();
        rotModes.add("Smooth");
        rotModes.add("Bypass");
        settingsManager.rSetting(new Setting("Rotation Mode", killAura, "Smooth", rotModes));
        settingsManager.rSetting(new Setting("Range", killAura, 4.0, 1.0, 6.0, false));
        settingsManager.rSetting(new Setting("Criticals Only", killAura, false));
        settingsManager.rSetting(new Setting("Turn Speed", killAura, 30.0, 1.0, 180.0, false));
        settingsManager.rSetting(new Setting("Movement Correction", killAura, true));
        settingsManager.rSetting(new Setting("Target Players", killAura, true));
        settingsManager.rSetting(new Setting("Target Mobs", killAura, false));
        settingsManager.rSetting(new Setting("Target Animals", killAura, false));
        settingsManager.rSetting(new Setting("Modern Delay (Ticks)", killAura, 0.0, 0.0, 5.0, true));
        settingsManager.rSetting(new Setting("Silent", killAura, true));
        settingsManager.rSetting(new Setting("1.8.9 Min CPS", killAura, 8.0, 1.0, 20.0, true));
        settingsManager.rSetting(new Setting("1.8.9 Max CPS", killAura, 12.0, 1.0, 20.0, true));

        moduleManager.modules.add(new SelfShader());

        Module menu = new Menu();
        moduleManager.modules.add(menu);
        ArrayList<String> menuBgModes = new ArrayList<>();
        menuBgModes.add("Shader");
        menuBgModes.add("Gradient");
        menuBgModes.add("Vanilla Panorama");
        settingsManager.rSetting(new Setting("Background", menu, "Shader", menuBgModes));
        settingsManager.rSetting(new Setting("Show Logo", menu, true));
        settingsManager.rSetting(new Setting("Logo Scale", menu, 1.0, 0.5, 1.5, false));
        settingsManager.rSetting(new Setting("Show Config Button", menu, true));
        settingsManager.rSetting(new Setting("Show Watermark", menu, true));
        settingsManager.rSetting(new Setting("Button Width", menu, 200.0, 140.0, 280.0, true));
        settingsManager.rSetting(new Setting("Panel Alpha", menu, 165.0, 0.0, 255.0, true));
        addColorSettings(settingsManager, menu, "Primary", 42, 168, 255);
        addColorSettings(settingsManager, menu, "Secondary", 16, 20, 30);
        settingsManager.rSetting(new Setting("Use Verdana Font", menu, true));
        settingsManager.rSetting(new Setting("Background Alpha", menu, 240.0, 0.0, 255.0, true));
        settingsManager.rSetting(new Setting("Corner Radius", menu, 12.0, 0.0, 30.0, true));
        settingsManager.rSetting(new Setting("Animation Speed", menu, 5.0, 1.0, 10.0, false));
        settingsManager.rSetting(new Setting("Hover Glow", menu, 35.0, 0.0, 100.0, true));

        Module guiModule = new GUI();
        moduleManager.modules.add(guiModule);
        ArrayList<String> guiLayouts = new ArrayList<>();
        guiLayouts.add("Compact");
        guiLayouts.add("Vertical");
        settingsManager.rSetting(new Setting("Layout", guiModule, "Compact", guiLayouts));
        ArrayList<String> guiScaleModes = new ArrayList<>();
        guiScaleModes.add("Auto");
        guiScaleModes.add("Custom");
        settingsManager.rSetting(new Setting("Scale Mode", guiModule, "Auto", guiScaleModes));
        settingsManager.rSetting(new Setting("Scale", guiModule, 1.0, 0.5, 3.0, false));
        settingsManager.rSetting(new Setting("Glass Opacity", guiModule, 70.0, 20.0, 100.0, true));
        settingsManager.rSetting(new Setting("Glow", guiModule, true));

        settingsManager.rSetting(new Setting("On Crystal", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Obsidian", crystalHelper, true));
        settingsManager.rSetting(new Setting("Exclude Bedrock", crystalHelper, false));
        settingsManager.rSetting(new Setting("Only Selected", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Any", crystalHelper, false));
        settingsManager.rSetting(new Setting("On Empty", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Sword", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Crystal Item", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Obsidian Item", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Totem", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Glowstone", crystalHelper, true));
        settingsManager.rSetting(new Setting("On Anchor", crystalHelper, true));
        settingsManager.rSetting(new Setting("Hold Trigger", crystalHelper, false));
        settingsManager.rSetting(new Setting("Cooldown (ms)", crystalHelper, 200.0, 0.0, 1000.0, true));

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ModuleUtils.onClientTickStart();
            SpoofManager.onTick();
            ModuleUtils.tickSilentRevert();
            SprintResetController.onClientTick(client);
            PearlGrapple.onWorldTick(client);
            tickSystemKeybinds(client);
            tickSafetyKeybinds(client);

            Screen activeScreen = (client != null && client.gui != null) ? client.gui.screen() : null;
            if (!(activeScreen instanceof GlassUiScreen) && !ClientNotificationService.hasActiveNotifications()) {
                StreamproofOverlay.hide();
            }

            for (Module m : moduleManager.modules) {
                m.tickKeybind();
                if (m.isToggled() || m.needsTick()) {
                    m.onTick();
                }
            }

            RotationManager.tick();
            MacroManager.tick();
            MacroManager.tickKeybinds();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            RotationManager.visualTick();
            ModuleUtils.onClientTickEnd();
        });

        ConfigManager.load();
        MacroManager.load();
        FriendManager.load();
        TargetFilterManager.load();

        ClientUiSounds.init();
        ClientGuiRegistry.init();

        Thread saveHook = new Thread(() -> {
            ConfigManager.save();
            ClientGuiRegistry.saveGuiPrefs();
        }, "ConfigSaveHook");
        saveHook.setDaemon(true);
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(saveHook);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralArgumentBuilder<FabricClientCommandSource> configRoot = ClientCommands.literal("config");
            LiteralArgumentBuilder<FabricClientCommandSource> configGui = ClientCommands.literal("gui");
            configGui.executes(context -> {
                Minecraft mc = Minecraft.getInstance();
                mc.execute(ClientGuiScreen::openConfigCenter);
                return 1;
            });
            LiteralArgumentBuilder<FabricClientCommandSource> configExport = ClientCommands.literal("export");
            configExport.executes(context -> {
                String exp = ConfigManager.exportSpecific(moduleManager.modules);
                Minecraft mc = Minecraft.getInstance();
                mc.keyboardHandler.setClipboard(exp);
                FabricClientCommandSource src = context.getSource();
                src.sendFeedback(Component.literal("§d[Neuer] §7Config exported to clipboard!"));
                return 1;
            });
            configRoot.then(configGui);
            configRoot.then(configExport);
            dispatcher.register(configRoot);

            LiteralArgumentBuilder<FabricClientCommandSource> mcRoot = ClientCommands.literal("mc");
            LiteralArgumentBuilder<FabricClientCommandSource> macroCmd = ClientCommands.literal("macro");
            LiteralArgumentBuilder<FabricClientCommandSource> macroExport = ClientCommands.literal("export");
            macroExport.executes(context -> {
                MacroManager.exportToClipboard();
                FabricClientCommandSource src = context.getSource();
                src.sendFeedback(Component.literal("§d[Neuer] §7Macro exported to clipboard!"));
                return 1;
            });
            LiteralArgumentBuilder<FabricClientCommandSource> macroImport = ClientCommands.literal("import");
            macroImport.executes(context -> {
                MacroManager.importFromClipboard();
                FabricClientCommandSource src = context.getSource();
                src.sendFeedback(Component.literal("§d[Neuer] §7Macro imported from clipboard!"));
                return 1;
            });
            macroCmd.then(macroExport);
            macroCmd.then(macroImport);

            LiteralArgumentBuilder<FabricClientCommandSource> enableCmd = ClientCommands.literal("enable");
            RequiredArgumentBuilder<FabricClientCommandSource, String> enableArg = ClientCommands.argument("module", StringArgumentType.word());
            enableArg.executes(context -> {
                String modName = StringArgumentType.getString(context, "module");
                Module mod = moduleManager.getModule(modName);
                FabricClientCommandSource src = context.getSource();
                if (mod != null) {
                    if (!mod.isToggled()) {
                        mod.toggle();
                    }
                    src.sendFeedback(Component.literal("§d[Neuer] §aEnabled " + mod.getName()));
                } else {
                    src.sendFeedback(Component.literal("§d[Neuer] §cModule not found!"));
                }
                return 1;
            });
            enableCmd.then(enableArg);

            LiteralArgumentBuilder<FabricClientCommandSource> disableCmd = ClientCommands.literal("disable");
            RequiredArgumentBuilder<FabricClientCommandSource, String> disableArg = ClientCommands.argument("module", StringArgumentType.word());
            disableArg.executes(context -> {
                String modName = StringArgumentType.getString(context, "module");
                Module mod = moduleManager.getModule(modName);
                FabricClientCommandSource src = context.getSource();
                if (mod != null) {
                    if (mod.isToggled()) {
                        mod.toggle();
                    }
                    src.sendFeedback(Component.literal("§d[Neuer] §cDisabled " + mod.getName()));
                } else {
                    src.sendFeedback(Component.literal("§d[Neuer] §cModule not found!"));
                }
                return 1;
            });
            disableCmd.then(disableArg);

            LiteralArgumentBuilder<FabricClientCommandSource> modCfgCmd = ClientCommands.literal("config");
            RequiredArgumentBuilder<FabricClientCommandSource, String> modCfgArg = ClientCommands.argument("module", StringArgumentType.word());
            modCfgArg.executes(context -> {
                String modName = StringArgumentType.getString(context, "module");
                Module mod = moduleManager.getModule(modName);
                FabricClientCommandSource src = context.getSource();
                if (mod != null) {
                    Minecraft mc = Minecraft.getInstance();
                    mc.execute(() -> ClientGuiScreen.openModuleDetail(mod));
                } else {
                    src.sendFeedback(Component.literal("§d[Neuer] §cModule not found!"));
                }
                return 1;
            });
            modCfgCmd.then(modCfgArg);

            mcRoot.then(macroCmd);
            mcRoot.then(enableCmd);
            mcRoot.then(disableCmd);
            mcRoot.then(modCfgCmd);
            dispatcher.register(mcRoot);
        });
    }
}
