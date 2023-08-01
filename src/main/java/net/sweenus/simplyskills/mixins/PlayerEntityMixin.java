package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.sweenus.simplyskills.abilities.AbilityEffects;
import net.sweenus.simplyskills.abilities.RangerAbilities;
import net.sweenus.simplyskills.abilities.WarriorAbilities;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
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
        if (player instanceof ServerPlayerEntity) {

            // fervour
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.fervour, player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100));
            }

            // Effect Bloodthirsty
            if (HelperMethods.isUnlocked("simplyskills_berserker",
                    SkillReferencePosition.berserkerSpecialisationBloodthirsty, player)) {
                AbilityEffects.effectBerserkerBloodthirsty(player);
            }
            // Effect Elemental Arrows Renewal
            if (HelperMethods.isUnlocked("simplyskills_ranger",
                    SkillReferencePosition.rangerSpecialisationElementalArrowsRenewal, player)) {
                RangerAbilities.passiveRangerElementalArrowsRenewal(player);
            }
            // Effect Fan of Blades Renewal
            if (HelperMethods.isUnlocked("simplyskills_rogue",
                    SkillReferencePosition.rogueSpecialisationEvasionFanOfBladesRenewal, player)) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.FANOFBLADES, 500, 1));
            }


        }
    }

    @Inject(at = @At("HEAD"), method = "takeShieldHit")
    public void simplyskills$takeShieldHit(LivingEntity attacker, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity) {
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.bulwarkRebuke, player)) {
                WarriorAbilities.passiveWarriorRebuke(player, attacker);
            }
        }
    }


}