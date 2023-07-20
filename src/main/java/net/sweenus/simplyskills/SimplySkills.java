package net.sweenus.simplyskills;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.sweenus.simplyskills.config.ServerConfig;
import net.sweenus.simplyskills.config.ServerConfigWrapper;
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
    public static ServerConfig config;

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

        AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        config = AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig().server;

        PassiveSkillReward.registerSkillTypes();
        SoundRegistry.init();
        EffectRegistry.registerEffects();
        ItemRegistry.registerItems();
        KeybindPacket.init();
        setSpecialisations();
    }
}
