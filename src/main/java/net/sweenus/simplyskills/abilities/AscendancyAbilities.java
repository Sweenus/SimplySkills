package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class AscendancyAbilities {

    public static int getAscendancyPoints(PlayerEntity player) {
        if (player instanceof  ServerPlayerEntity serverPlayer) {
            Identifier category = new Identifier(SimplySkills.MOD_ID, "ascendancy");
            if (SkillsAPI.getCategory(category).isPresent() && !SkillsAPI.getCategory(category).get().getUnlockedSkills(serverPlayer).isEmpty()) {
                return SkillsAPI.getCategory(category).get().getUnlockedSkills(serverPlayer).size();
            }
        }
        return 0;
    }


    //------- ASCENDANCY ABILITIES --------

    public static boolean righteousHammers(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RIGHTEOUSHAMMERS,
                800, 1 + (getAscendancyPoints(player) / 10), false, false, true));
        return true;
    }

    public static boolean boneArmor(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BONEARMOR,
                800, 3 + (getAscendancyPoints(player) / 10), false, false, true));
        return true;
    }
    public static void boneArmorEffect(ServerPlayerEntity player) {
            if (HelperMethods.isUnlocked("simplyskills:ascendancy", SkillReferencePosition.ascendancyBoneArmor, player) && player.hasStatusEffect(EffectRegistry.BONEARMOR))
                HelperMethods.decrementStatusEffect(player, EffectRegistry.BONEARMOR);
    }


}
