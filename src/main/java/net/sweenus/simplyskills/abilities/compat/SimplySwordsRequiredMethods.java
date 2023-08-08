package net.sweenus.simplyskills.abilities.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.api.SimplySwordsAPI;
import net.sweenus.simplyswords.entity.BattleStandardEntity;

public class SimplySwordsRequiredMethods {

    // Config fetching

    public static int preciseChance = SimplySwords.gemEffectsConfig.preciseChance;
    public static int mightyChance = SimplySwords.gemEffectsConfig.mightyChance;
    public static int renewedChance = SimplySwords.gemEffectsConfig.renewedChance;
    public static int stealthyChance = SimplySwords.gemEffectsConfig.stealthyChance;
    public static int spellshieldChance = SimplySwords.gemEffectsConfig.spellshieldChance;
    public static int leapingChance = SimplySwords.gemEffectsConfig.leapingChance;

    public static int spellStandardChance = 10;
    public static int deceptionChance = 50;




    // API Reliant

    public static void spawnSpellStandard(PlayerEntity user) {
        Box box = HelperMethods.createBox(user, 20);
        int chance = SimplySwordsRequiredMethods.spellStandardChance;

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
            SimplySwordsGemEffects.doSound(user);
        }
    }

    public static void spawnWarStandard(PlayerEntity user) {
        SimplySwordsAPI.spawnBattleStandard(user, 3, "api", 3, 3,
                "simplyskills:might", null, 4,
                "simplyskills:revealed", null, 0,
                false, false);
    }

}