package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.skill.SkillState;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "damage")
    public void simplyskills$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        if (SkillsAPI.getUnlockedSkills(player, "combat").get().contains(SkillReferencePosition.hardy)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) amount * 20));
        }
        System.out.println(SkillsAPI.getUnlockedSkills(player, "combat"));

    }


}