package net.sweenus.simplyskills;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.sweenus.simplyskills.config.*;
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
    public static GeneralConfig generalConfig;
    public static WayfarerConfig wayfarerConfig;
    public static WarriorConfig warriorConfig;
    public static InitiateConfig initiateConfig;
    public static BerserkerConfig berserkerConfig;
    public static WizardConfig wizardConfig;
    public static SpellbladeConfig spellbladeConfig;
    public static RogueConfig rogueConfig;
    public static RangerConfig rangerConfig;

    private static void setSpecialisations() {
        specialisations.add("simplyskills_rogue");
        specialisations.add("simplyskills_ranger");
        specialisations.add("simplyskills_berserker");
        specialisations.add("simplyskills_wizard");
        specialisations.add("simplyskills_spellblade");
    }
    public static String[] getSpecialisations() {return new String[] {
            "simplyskills_rogue",
            "simplyskills_ranger",
            "simplyskills_berserker",
            "simplyskills_wizard",
            "simplyskills_spellblade"};}

    public static List<String> getSpecialisationsAsArray() {
        return specialisations;
    }
    public static String[] getFrostSpells() {return new String[] {"frost", "ice"};}
    public static String[] getFireSpells() {return new String[] {"fire", "flame"};}
    public static String[] getLightningSpells() {return new String[] {"static", "lightning"};}
    public static String[] getSoulSpells() {return new String[] {"soul", "dark"};}
    public static String[] getHealingSpells() {return new String[] {"holy", "healing"};}
    public static String[] getArcaneSpells() {return new String[] {"arcane", "arcanes"};}
    public static String[] getPhysicalSpells() {return new String[] {"blade", "blades"};}

    @Override
    public void onInitialize() {

        AutoConfig.register(ConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        generalConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().client;
        wayfarerConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().wayfarer;
        warriorConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().warrior;
        initiateConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().initiate;
        berserkerConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().berserker;
        wizardConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().wizard;
        spellbladeConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().spellblade;
        rogueConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().rogue;
        rangerConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().ranger;

        PassiveSkillReward.registerSkillTypes();
        SoundRegistry.init();
        EffectRegistry.registerEffects();
        ItemRegistry.registerItems();
        KeybindPacket.init();
        setSpecialisations();
    }
}
