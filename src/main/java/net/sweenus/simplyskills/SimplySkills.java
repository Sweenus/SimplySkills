package net.sweenus.simplyskills;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.config.*;
import net.sweenus.simplyskills.network.KeybindPacket;
import net.sweenus.simplyskills.network.ModPacketHandler;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.ItemRegistry;
import net.sweenus.simplyskills.registry.ModelRegistry;
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
    public static CrusaderConfig crusaderConfig;
    public static ClericConfig clericConfig;

    private static void setSpecialisations() {
        specialisations.add("simplyskills:rogue");
        specialisations.add("simplyskills:ranger");
        specialisations.add("simplyskills:berserker");
        specialisations.add("simplyskills:wizard");
        specialisations.add("simplyskills:spellblade");
        specialisations.add("simplyskills:crusader");
        specialisations.add("simplyskills:cleric");
    }
    public static String[] getSpecialisations() {return new String[] {
            "simplyskills:rogue",
            "simplyskills:ranger",
            "simplyskills:berserker",
            "simplyskills:wizard",
            "simplyskills:spellblade",
            "simplyskills:crusader",
            "simplyskills:cleric",
    };}

    public static List<String> getSpecialisationsAsArray() {
        return specialisations;
    }
    public static String[] getFrostSpells() {return new String[] {"frost", "ice"};}
    public static String[] getFireSpells() {return new String[] {"fire", "flame"};}
    public static String[] getLightningSpells() {return new String[] {"static", "lightning"};}
    public static String[] getSoulSpells() {return new String[] {"soul", "dark"};}
    public static String[] getHealingSpells() {return new String[] {"holy", "healing", "sacred"};}
    public static String[] getArcaneSpells() {return new String[] {"arcane", "arcanes"};}
    public static String[] getPhysicalSpells() {return new String[] {"blade", "blades", "judgement"};}

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
        crusaderConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().crusader;
        clericConfig = AutoConfig.getConfigHolder(ConfigWrapper.class).getConfig().cleric;

        EffectRegistry.registerEffects();
        PassiveSkillReward.register();
        SoundRegistry.registerSounds();
        ItemRegistry.registerItems();
        ModelRegistry.registerModels();
        KeybindPacket.init();
        ModPacketHandler.registerServer();
        setSpecialisations();


        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (generalConfig.disableDefaultPuffishTrees) {
                processPlayer(handler.player);
            }
            ModPacketHandler.sendSignatureAbility(handler.player);
        });
    }

    private void processPlayer(ServerPlayerEntity player) {
        List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(player);
        if (!unlockedCategories.isEmpty()) {
            unlockedCategories.forEach(category -> processCategory(player, category));
        }
    }

    private void processCategory(ServerPlayerEntity player, Category category) {
        String categoryId = category.getId().toString();
        if (categoryId.equals("minecraft:combat") || categoryId.equals("minecraft:mining")) {
            SkillsAPI.getCategory(new Identifier(categoryId)).ifPresent(categoryObj -> {
                categoryObj.erase(player);
                categoryObj.lock(player);
            });
        }
    }

}
