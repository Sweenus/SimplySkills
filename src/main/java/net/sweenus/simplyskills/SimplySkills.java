package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;

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
