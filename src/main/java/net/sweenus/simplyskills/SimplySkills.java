package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;
import net.sweenus.simplyskills.util.FileGen;

public class SimplySkills implements ModInitializer {
    public static final String MOD_ID = "simplyskills";
    @Override
    public void onInitialize() {

        PassiveSkillReward.registerSkillTypes();
        SoundRegistry.init();

        //Copy files from bundled data once we figure out how do
        //FileGen.generateSkillFiles();
        //FileGen.checkFileDirectory();

    }
}
