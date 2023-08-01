package net.sweenus.simplyskills.mixins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
import net.sweenus.simplyskills.abilities.AbilityLogic;
import net.sweenus.simplyskills.registry.SoundRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkillsMod.class)
public class SkillsModMixin {

    @Inject(at = @At("HEAD"), method = "tryUnlockSkill")
    public void simplyskills$tryUnlockSkill(ServerPlayerEntity player, String categoryId, String skillId, boolean force, CallbackInfo ci) {

        //Sound Event on skill unlock
        if (!SkillsAPI.getUnlockedSkills(player, categoryId).get().contains(skillId)
        && SkillsAPI.getPointsLeft(player, categoryId).get() > 0) {

            double choose_pitch = Math.random() * 1.2;
            SoundEvent sound = SoundRegistry.SOUNDEFFECT45;
            SoundEvent sound2 = SoundRegistry.SOUNDEFFECT47;

            player.getWorld().playSoundFromEntity(null, player, sound2,
                    SoundCategory.PLAYERS, 1, 1.5f);
            player.getWorld().playSoundFromEntity(null, player, sound,
                    SoundCategory.PLAYERS, 1, (float) choose_pitch);

            }
        }

    @Inject(at = @At("HEAD"), method = "unlockCategory", cancellable = true)
    public void simplyskills$unlockCategory(ServerPlayerEntity player, String categoryId, CallbackInfo ci) {
        if (AbilityLogic.skillTreeUnlockManager(player, categoryId))
            ci.cancel();
    }

    }
