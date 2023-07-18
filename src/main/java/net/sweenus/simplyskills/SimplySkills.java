package net.sweenus.simplyskills;

import net.fabricmc.api.ModInitializer;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.ItemRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.rewards.PassiveSkillReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimplySkills implements ModInitializer {
    public static final String MOD_ID = "simplyskills";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static List<String> specialisations = new ArrayList<>();

    private static void setSpecialisations() {
        specialisations.add("simplyskills_rogue");
        specialisations.add("simplyskills_ranger");
        specialisations.add("simplyskills_berserker");
        specialisations.add("simplyskills_wizard");
        specialisations.add("simplyskills_spellblade");
    }

    public static List<String> getSpecialisations() {
        return specialisations;
    }

    @Override
    public void onInitialize() {

        PassiveSkillReward.registerSkillTypes();
        SoundRegistry.init();
        EffectRegistry.registerEffects();
        ItemRegistry.registerItems();
        KeybindPacket.init();
        setSpecialisations();
    }
}
