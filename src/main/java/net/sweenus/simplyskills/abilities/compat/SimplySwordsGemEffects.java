package net.sweenus.simplyskills.abilities.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundCategory;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;

public class SimplySwordsGemEffects {

    public static boolean passVersionCheck() {
        if (FabricLoader.getInstance().isModLoaded("simplyswords")) {
            if (FabricLoader.getInstance().getModContainer("simplyswords").isPresent()) {
                String blacklistedVersion1 = "1.50";
                String blacklistedVersion2 = "1.48";
                String version = FabricLoader.getInstance().getModContainer("simplyswords").get().getMetadata().getVersion().toString();
                //System.out.println("Comparing current Simply Swords version: " + version + " against blacklisted versions: " + blacklistedVersion1 + " & " + blacklistedVersion2);
                if (version.contains(blacklistedVersion1) || version.contains(blacklistedVersion2)) {
                    //System.out.println("Detected BLACKLISTED version of Simply Swords");
                    return false;
                }
            }
            return true;
        }
        return  false;
    }


    public static void doGenericAbilityGemEffects(PlayerEntity user) {

        if (FabricLoader.getInstance().isModLoaded("simplyswords") && passVersionCheck()) {

            // Used for non-specialisation specific effects that proc on signature ability use

            String mainHandNetherEffect = "";
            String offHandNetherEffect = "";

            if (user.getMainHandStack().getItem() instanceof SwordItem)
                mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
            if (user.getOffHandStack().getItem() instanceof SwordItem)
                offHandNetherEffect = user.getOffHandStack().getOrCreateNbt().getString("nether_power");
            String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

            // Chance to gain 5 stacks of precision on ability use
            if (allNetherEffects.contains("precise")) {
                int procChance = SimplySwordsRequiredMethods.preciseChance;
                if (user.getRandom().nextInt(100) < procChance) {
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.PRECISION, 200, 5, false, false, true));
                    doSound(user);
                }
            }

            // Chance to gain 2 stacks of might on ability use
            if (allNetherEffects.contains("mighty")) {
                int procChance = SimplySwordsRequiredMethods.mightyChance;
                if (user.getRandom().nextInt(100) < procChance) {
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.MIGHT, 200, 3, false, false, true));
                    doSound(user);
                }
            }

            // Chance to gain stealth on ability use
            if (allNetherEffects.contains("stealthy")) {
                int procChance = SimplySwordsRequiredMethods.stealthyChance;
                if (user.getRandom().nextInt(100) < procChance) {
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 600, 0, false, false, true));
                    doSound(user);
                }
            }
        }
    }

    // Socket checking
    public static boolean doSignatureGemEffects(PlayerEntity user, String nether_power) {

        if (FabricLoader.getInstance().isModLoaded("simplyswords") && passVersionCheck()) {

            String mainHandNetherEffect = "";
            String offHandNetherEffect = "";

            if (user.getMainHandStack().getItem() instanceof SwordItem)
                mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
            if (user.getOffHandStack().getItem() instanceof SwordItem && !nether_power.contains("spellforged"))
                offHandNetherEffect = user.getOffHandStack().getOrCreateNbt().getString("nether_power");
            String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

            return allNetherEffects.contains(nether_power);
        }
        return false;
    }

    public static void doSound(PlayerEntity user) {
        user.getWorld().playSoundFromEntity(null, user, SoundRegistry.FX_UI_UNLOCK3,
                SoundCategory.PLAYERS, 1f, 1.6f);
    }



    // Specific effects

    // Renewed - Chance to significantly reduce cooldown
    public static int renewed(PlayerEntity player, int cooldown, int minimumCD) {
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "renewed")) {
            int procChance = SimplySwordsRequiredMethods.renewedChance;
            if (player.getRandom().nextInt(100) < procChance) {
                doSound(player);
                return minimumCD;
            }
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
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "spellshield")) {
            int procChance = SimplySwordsRequiredMethods.spellshieldChance;
            if (player.getRandom().nextInt(100) < procChance) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BARRIER, 100, 0, false, false, true));
                doSound(player);
            }
        }
    }

    // When in mainhand, grants + 1 to all Spell Power
    public static void spellforged(PlayerEntity player) {
        if (player.age %20 == 0 && SimplySwordsGemEffects.doSignatureGemEffects(player, "spellforged"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLFORGED, 25, 0, false, false, true));
    }

    // When in main or offhand, grants + 2 to Soul & Lightning Spell Power
    public static void soulshock(PlayerEntity player) {
        if (player.age %20 == 0 && SimplySwordsGemEffects.doSignatureGemEffects(player, "soulshock"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SOULSHOCK, 25, 0, false, false, true));
    }

    // Chance on spell hit to drop a banner that periodically grants precision & spellforged
    public static void spellStandard(PlayerEntity user) {
        if (doSignatureGemEffects(user, "spell_Standard")) {
            SimplySwordsRequiredMethods.spawnSpellStandard(user);
        }
    }

    // Drop a banner at the end of your charge, revealing enemies and granting might to allies
    public static void warStandard(PlayerEntity user) {
        if (doSignatureGemEffects(user, "war_standard")) {
            SimplySwordsRequiredMethods.spawnWarStandard(user);
            doSound(user);
        }
    }

    //Chance to remove Revealed stacks on Evasion proc
    public static void deception(PlayerEntity user) {
        if (doSignatureGemEffects(user, "deception")) {
            int chance = SimplySwordsRequiredMethods.deceptionChance;
            if (user.getRandom().nextInt(100) < chance && user.hasStatusEffect(EffectRegistry.REVEALED)) {
                user.removeStatusEffect(EffectRegistry.REVEALED);
                doSound(user);
            }
        }
    }


}
