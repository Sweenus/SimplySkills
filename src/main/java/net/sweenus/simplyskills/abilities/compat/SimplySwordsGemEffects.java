package net.sweenus.simplyskills.abilities.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sweenus.simplyswords.entity.BattleStandardEntity;
import net.sweenus.simplyswords.registry.EntityRegistry;
import net.sweenus.simplyswords.registry.SoundRegistry;

public class SimplySwordsGemEffects {


    public static void doGenericAbilityGemEffects(PlayerEntity user) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return;


        String mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String offHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;


        if (allNetherEffects.contains("assault_banner")) {
            //Use SimplySwordsAPI
        }


    }

    public static boolean doSignatureGemEffects(PlayerEntity user, String nether_power) {

        if (!FabricLoader.getInstance().isModLoaded("simplyswords"))
            return false;

        String mainHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String offHandNetherEffect = user.getMainHandStack().getOrCreateNbt().getString("nether_power");
        String allNetherEffects = offHandNetherEffect + mainHandNetherEffect;

        return allNetherEffects.contains(nether_power);
    }


    // Specifics

    // Renewed - Chance to significantly reduce cooldown
    public static int renewed(PlayerEntity player, int cooldown, int minimumCD) {
        int renewedChance = 30; // Simply Swords Config
        if (SimplySwordsGemEffects.doSignatureGemEffects(player, "renewed")
                && player.getRandom().nextInt(100) < renewedChance)
            return minimumCD;
        return cooldown;
    }

    // Chance to gain 5 stacks of precision on ability use

    // Chance to gain 2 stacks of might on ability use

    // Chance on spell hit to drop a banner that periodically grants precision

    // Berserkers signature ability Berserking, no longer provides stacks of Berserking but has a reduced base cooldown.

    // Chance to gain a stack of Barrier whenever you cast a spell

    // When in mainhand, grants + 1 to all Spell Power
    // When in main or offhand, grants + 2 to Soul & Lightning Spell Power

}
