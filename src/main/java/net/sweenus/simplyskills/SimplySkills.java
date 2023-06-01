package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;

public class SimplySkills implements ModInitializer {
    @Override
    public void onInitialize() {

        PassiveSkillReward.registerSkillTypes();

    }
}
