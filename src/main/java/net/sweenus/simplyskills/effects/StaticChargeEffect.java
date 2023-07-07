package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SignatureAbilities;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class StaticChargeEffect extends StatusEffect {

    public PlayerEntity ownerEntity;
    public StaticChargeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {
            if (livingEntity.age % 20 == 0) {
                int speedChance = 5;
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) ownerEntity, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedTwo))
                    speedChance = 10;
                else if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) ownerEntity, "simplyskills_wizard").get()
                        .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeedThree))
                    speedChance = 15;

                Box box = HelperMethods.createBox(livingEntity, 3);
                for (Entity entities : livingEntity.world.getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null && ownerEntity != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, ownerEntity)
                        && le.getRandom().nextInt(100) < 30) {
                            SignatureAbilities.castSpellEngineIndirectTarget(ownerEntity,
                                    "simplyskills:static_charge",
                                    3, le);
                            le.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80));
                            StatusEffect sc = EffectRegistry.STATICCHARGE;
                            HelperMethods.decrementStatusEffect(livingEntity, sc);
                            if (livingEntity.hasStatusEffect(sc)) {
                                le.addStatusEffect(new StatusEffectInstance(sc,
                                        livingEntity.getStatusEffect(sc).getDuration(),
                                        livingEntity.getStatusEffect(sc).getAmplifier()));
                                livingEntity.removeStatusEffect(sc);
                            }

                            if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) ownerEntity, "simplyskills_wizard").get()
                                    .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeSpeed) &&
                                    ownerEntity.getRandom().nextInt(100) < speedChance)
                                HelperMethods.incrementStatusEffect(ownerEntity, StatusEffects.SPEED, 800, 1, 4);


                            break;
                        }
                    }
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.world.isClient()) {
            Box box = HelperMethods.createBox(entity, 80);
            for (Entity entities : entity.world.getOtherEntities(entity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if (entities instanceof PlayerEntity pe) {
                        if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) pe, "simplyskills_wizard").get()
                                .contains(SkillReferencePosition.wizardSpecialisationStaticDischargeLeap)) {
                            ownerEntity = pe;
                            break;
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
