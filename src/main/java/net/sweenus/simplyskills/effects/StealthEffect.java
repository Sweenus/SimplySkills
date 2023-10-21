package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class StealthEffect extends StatusEffect {
    public StealthEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {


        if (!livingEntity.getWorld().isClient()) {
            if (livingEntity instanceof ServerPlayerEntity serverPlayer) {

                int regenerationFrequency = SimplySkills.rogueConfig.passiveRogueRecoveryRegenerationFrequency;
                int regenerationAmplifier = SimplySkills.rogueConfig.passiveRogueRecoveryRegenerationAmplifier;
                int resistanceFrequency = SimplySkills.rogueConfig.passiveRogueShadowVeilResistanceFrequency;
                int resistanceStacks = SimplySkills.rogueConfig.passiveRogueShadowVeilResistanceStacks;
                int resistanceMaxStacks = SimplySkills.rogueConfig.passiveRogueShadowVeilResistanceMaxStacks;


                if (serverPlayer.hasStatusEffect(EffectRegistry.REVEALED)) {
                    livingEntity.removeStatusEffect(EffectRegistry.STEALTH);
                }
                if (serverPlayer.hasStatusEffect(EffectRegistry.STEALTH)
                        && serverPlayer.getStatusEffect(EffectRegistry.STEALTH).getDuration() < 10) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED,
                            180, 2, false, false, true));
                }


                if (HelperMethods.isUnlocked("simplyskills:rogue",
                        SkillReferencePosition.rogueRecovery, serverPlayer)
                        && serverPlayer.age % regenerationFrequency == 0)
                    serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,
                            regenerationFrequency + 5, regenerationAmplifier, false, false, true));

                if (HelperMethods.isUnlocked("simplyskills:rogue",
                        SkillReferencePosition.rogueShadowVeil, serverPlayer)
                        && serverPlayer.age % regenerationFrequency == 0)
                    HelperMethods.incrementStatusEffect(serverPlayer, StatusEffects.RESISTANCE,
                            resistanceFrequency + 5, resistanceStacks, resistanceMaxStacks);

            }
        }

        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
