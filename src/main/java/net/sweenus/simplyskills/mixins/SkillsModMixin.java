package net.sweenus.simplyskills.mixins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
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

            //int choose_sound = (int) (Math.random() * 3);
            float choose_pitch = (float) Math.random() * 2;
            SoundEvent sound = SoundRegistry.FX_UI_UNLOCK;

            player.world.playSoundFromEntity(null, player, sound,
                    SoundCategory.PLAYERS, 1, choose_pitch);

        }
        }
    }
