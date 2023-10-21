package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElementalImpactEffect extends StatusEffect {
    public ElementalImpactEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {
                int velocity = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactVelocity;

                player.setVelocity(livingEntity.getRotationVector().multiply(+velocity));
                player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                player.velocityModified = true;
                List<String> list = new ArrayList<>();
                list.add("simplyskills:frost_arrow_rain");
                list.add("simplyskills:fire_arrow_rain");
                list.add("simplyskills:lightning_arrow_rain");
                List<String> list2 = new ArrayList<>();
                list2.add("simplyskills:fire_explosion");
                list2.add("simplyskills:frost_explosion");
                list2.add("simplyskills:lightning_explosion");
                Random rand = new Random();
                String randomSpell = list.get(rand.nextInt(list.size()));
                String randomSpell2 = list2.get(rand.nextInt(list2.size()));
                int radius = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactRadius;
                int chance = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactChance;
                int slownessDuration = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactSlownessDuration;
                int slownessAmplifier = SimplySkills.spellbladeConfig.signatureSpellbladeElementalImpactSlownessAmplifier;

                SignatureAbilities.castSpellEngineAOE(player, randomSpell, radius, chance, true);
                SignatureAbilities.castSpellEngineAOE(player, randomSpell2, radius, (int)(chance * 0.35), true);

                if (HelperMethods.isUnlocked("simplyskills:spellblade",
                        SkillReferencePosition.spellbladeSpecialisationElementalImpactMagnet, player)){
                    Box box = HelperMethods.createBox(player, radius*2);
                    for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player) && !le.hasStatusEffect(StatusEffects.SLOWNESS)) {
                                le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                                le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, slownessDuration, slownessAmplifier, false, false, true));
                            }
                        }
                    }
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
