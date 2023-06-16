package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;
import net.sweenus.simplyskills.util.FileGen;

public class SimplySkills implements ModInitializer {
    @Override
    public void onInitialize() {

        PassiveSkillReward.registerSkillTypes();

        //Copy files from bundled data once we figure out how do
        //FileGen.generateSkillFiles();
        //FileGen.checkFileDirectory();

    }
}
