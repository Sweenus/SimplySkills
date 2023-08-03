package net.sweenus.simplyskills.abilities.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;

public class SimplySwordsGemEffects {


    public static void doGenericAbilityGemEffects(PlayerEntity user) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return;


        String mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String offHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;


        // Banner removes exhaustion stacks
        if (allNetherEffects.contains("assault_banner")) {
            //Use SimplySwordsAPI
        }

        // Chance on spell hit to drop a banner that periodically grants precision & spellforged
        if (allNetherEffects.contains("mage_banner")) {
            //Use SimplySwordsAPI
        }

        // Chance to gain 5 stacks of precision on ability use
        if (allNetherEffects.contains("precise")) {
            int procChance = 30; // Simply Swords Config
            if (user.getRandom().nextInt(100) < procChance)
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.PRECISION, 200, 5));
        }

        // Chance to gain 2 stacks of might on ability use
        if (allNetherEffects.contains("mighty")) {
            int procChance = 30; // Simply Swords Config
            if (user.getRandom().nextInt(100) < procChance)
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.MIGHT, 200, 3));
        }

        // Chance to gain stealth on ability use
        if (allNetherEffects.contains("stealthy")) {
            int procChance = 30; // Simply Swords Config
            if (user.getRandom().nextInt(100) < procChance)
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 600));
        }


    }

    public static boolean doSignatureGemEffects(PlayerEntity user, String nether_power) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return false;

        String mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String offHandNetherEffect = user.getOffHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

        return allNetherEffects.contains(nether_power);
    }


    // Specifics

    // Renewed - Chance to significantly reduce cooldown
    public static int renewed(PlayerEntity player, int cooldown, int minimumCD) {
        int procChance = 30; // Simply Swords Config
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "renewed")
                && player.getRandom().nextInt(100) < procChance)
            return minimumCD;
        return cooldown;
    }

    // Accelerant - Berserkers signature ability Berserking, no longer provides stacks of Berserking but has a reduced base cooldown.
    public static int accelerant(PlayerEntity player, int cooldown, int minimumCD) {
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "accelerant"))
            return (cooldown - 12000);
        return cooldown;
    }

    // Chance to gain a stack of Barrier whenever you cast a spell
    public static void spellshield(PlayerEntity player) {
        int procChance = 15; // Simply Swords Config
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "spellshield")
                && player.getRandom().nextInt(100) < procChance)
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BARRIER, 100, 0));
    }

    // When in mainhand, grants + 1 to all Spell Power
    public static void spellforged(PlayerEntity player) {
        if (player.age %20 == 0 && player.getMainHandStack().
                getOrCreateNbt().getString("nether_power").contains("spellforged"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SPELLFORGED, 25, 0));
    }

    // When in main or offhand, grants + 2 to Soul & Lightning Spell Power
    public static void soulshock(PlayerEntity player) {
        if (player.age %20 == 0 && SimplySwordsGemEffects.doSignatureGemEffects(player, "soulshock"))
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SOULSHOCK, 25, 0));
    }

}
