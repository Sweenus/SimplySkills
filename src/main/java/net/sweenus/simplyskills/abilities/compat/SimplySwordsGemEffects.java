package net.sweenus.simplyskills.abilities.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.entity.BattleStandardEntity;
import net.sweenus.simplyswords.item.UniqueSwordItem;

public class SimplySwordsGemEffects {


    public static void doGenericAbilityGemEffects(PlayerEntity user) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return;

        // Used for non-specialisation specific effects that proc on signature ability use

        String mainHandNetherEffect = "";
        String offHandNetherEffect = "";

        if (user.getMainHandStack().getItem() instanceof UniqueSwordItem)
            mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        if (user.getOffHandStack().getItem() instanceof UniqueSwordItem)
            offHandNetherEffect = user.getOffHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

        // Chance to gain 5 stacks of precision on ability use
        if (allNetherEffects.contains("precise")) {
            int procChance = SimplySwords.gemEffectsConfig.preciseChance;
            if (user.getRandom().nextInt(100) < procChance) {
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.PRECISION, 200, 5));
                doSound(user);
            }
        }

        // Chance to gain 2 stacks of might on ability use
        if (allNetherEffects.contains("mighty")) {
            int procChance = SimplySwords.gemEffectsConfig.mightyChance;
            if (user.getRandom().nextInt(100) < procChance) {
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.MIGHT, 200, 3));
                doSound(user);
            }

        }

        // Chance to gain stealth on ability use
        if (allNetherEffects.contains("stealthy")) {
            int procChance = SimplySwords.gemEffectsConfig.stealthyChance;
            if (user.getRandom().nextInt(100) < procChance) {
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 600));
                doSound(user);
            }
        }


    }

    // Socket checking
    public static boolean doSignatureGemEffects(PlayerEntity user, String nether_power) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return false;

        String mainHandNetherEffect = "";
        String offHandNetherEffect = "";

        if (user.getMainHandStack().getItem() instanceof UniqueSwordItem)
            mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        if (user.getOffHandStack().getItem() instanceof UniqueSwordItem && !nether_power.contains("spellforged"))
            offHandNetherEffect = user.getOffHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

        return allNetherEffects.contains(nether_power);
    }

    public static void doSound(PlayerEntity user) {
        user.getWorld().playSoundFromEntity(null, user, SoundRegistry.FX_UI_UNLOCK3,
                SoundCategory.PLAYERS, 1f, 1.6f);
    }



    // Specific effects

    // Renewed - Chance to significantly reduce cooldown
    public static int renewed(PlayerEntity player, int cooldown, int minimumCD) {
        int procChance = SimplySwords.gemEffectsConfig.renewedChance;
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "renewed")
                && player.getRandom().nextInt(100) < procChance) {
            doSound(player);
            return minimumCD;
        }
        return cooldown;
    }

    // Accelerant - Berserkers signature ability Berserking, no longer provides stacks of Berserking but has a reduced base cooldown.
    public static int accelerant(PlayerEntity player, int cooldown, int minimumCD) {
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "accelerant")) {
            doSound(player);
            return (cooldown - 12000);
        }
        return cooldown;
    }

    // Chance to gain a stack of Barrier whenever you cast a spell
    public static void spellshield(PlayerEntity player) {
        int procChance = SimplySwords.gemEffectsConfig.spellshieldChance;
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "spellshield")
                && player.getRandom().nextInt(100) < procChance) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BARRIER, 100, 0));
            doSound(player);
        }
    }

    // When in mainhand, grants + 1 to all Spell Power
    public static void spellforged(PlayerEntity player) {
        if (player.age %20 == 0 && SimplySwordsGemEffects.doSignatureGemEffects(player, "spellforged"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLFORGED, 25, 0));
    }

    // When in main or offhand, grants + 2 to Soul & Lightning Spell Power
    public static void soulshock(PlayerEntity player) {
        if (player.age %20 == 0 && SimplySwordsGemEffects.doSignatureGemEffects(player, "soulshock"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SOULSHOCK, 25, 0));
    }

    // Chance on spell hit to drop a banner that periodically grants precision & spellforged
    public static void spellStandard(PlayerEntity user) {
        if (doSignatureGemEffects(user, "spell_Standard")) {
            Box box = HelperMethods.createBox(user, 20);
            int chance = 10; // Simply Swords Config

            for (Entity entities : user.getWorld().getOtherEntities(user, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if (entities instanceof BattleStandardEntity bse) {

                        if (bse.ownerEntity == user && bse.positiveEffect.contains("simplyskills:precision")
                                && bse.positiveEffectSecondary.contains("simplyskills:spellforged"))
                            return;
                    }
                }
            }
            if (user.getRandom().nextInt(100) < chance) {
                SimplySwordsAPI.spawnBattleStandard(user, 3, "api",
                        3, -2, "simplyskills:precision",
                        "simplyskills:spellforged", 0,
                        null, null, 0,
                        false, false);
                doSound(user);
            }
        }
    }

    // Drop a banner at the end of your charge, revealing enemies and granting might to allies
    public static void warStandard(PlayerEntity user) {
        if (doSignatureGemEffects(user, "war_standard")) {
            SimplySwordsAPI.spawnBattleStandard(user, 3, "api", 3, 3,
                    "simplyskills:might", null, 4,
                    "simplyskills:revealed", null, 0,
                    false, false);
            doSound(user);
        }
    }

    //Chance to remove Revealed stacks on Evasion proc
    public static void deception(PlayerEntity user) {
        if (doSignatureGemEffects(user, "deception")) {
            int chance = 50; // Simply Swords Config
            if (user.getRandom().nextInt(100) < chance && user.hasStatusEffect(EffectRegistry.REVEALED)) {
                user.removeStatusEffect(EffectRegistry.REVEALED);
                doSound(user);
            }
        }
    }


}
