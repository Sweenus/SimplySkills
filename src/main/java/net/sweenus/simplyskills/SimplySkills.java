package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
import net.puffish.skillsmod.config.ModConfig;
import net.puffish.skillsmod.json.JsonElementWrapper;
import net.puffish.skillsmod.json.JsonPath;
import net.puffish.skillsmod.server.network.ServerPacketSender;
import net.puffish.skillsmod.utils.PathUtils;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;
import net.sweenus.simplyskills.util.FileCopier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SimplySkills implements ModInitializer {
    public static final String MOD_ID = "simplyskills";

    @Override
    public void onInitialize() {

        PassiveSkillReward.registerSkillTypes();
        SoundRegistry.init();
        EffectRegistry.registerEffects();
        KeybindPacket.init();

    }
}
