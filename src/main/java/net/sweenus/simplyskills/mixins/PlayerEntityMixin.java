package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.Abilities;
import net.sweenus.simplyskills.util.AbilityEffects;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "onKilledOther")
    public void simplyskills$onKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            // fervour
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get()
                    .contains(SkillReferencePosition.fervour)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100));
            }

            // Effect Bloodthirsty
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_berserker").get()
                    .contains(SkillReferencePosition.berserkerSpecialisationBloodthirsty)) {
                AbilityEffects.effectBerserkerBloodthirsty(player, other);
            }
            // Effect Elemental Arrows Renewal
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_ranger").get()
                    .contains(SkillReferencePosition.rangerSpecialisationElementalArrowsRenewal)) {
                Abilities.passiveRangerElementalArrowsRenewal(player);
            }
            // Effect Fan of Blades Renewal
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_rogue").get()
                    .contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesRenewal)) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FANOFBLADES, 500, 1));
            }


        }
    }

    @Inject(at = @At("HEAD"), method = "takeShieldHit")
    public void simplyskills$takeShieldHit(LivingEntity attacker, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (SkillsAPI.getUnlockedSkills(serverPlayer,
                    "simplyskills").get().contains(SkillReferencePosition.bulwarkRebuke)) {
                Abilities.passiveBulwarkRebuke(player, attacker);
            }
        }
    }


}