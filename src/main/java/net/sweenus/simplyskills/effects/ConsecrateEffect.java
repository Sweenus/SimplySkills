package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.spell_engine.particle.Particles;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class ConsecrateEffect extends StatusEffect {
    public ConsecrateEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {

                int radius = SimplySkills.crusaderConfig.signatureCrusaderConsecrationRadius;
                double damageMultiplier = SimplySkills.crusaderConfig.signatureCrusaderConsecrationDMGMultiplier;
                int hitFrequency = SimplySkills.crusaderConfig.signatureCrusaderConsecrationHitFrequency;
                double damage = (player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute) * damageMultiplier);
                int tauntDuration = SimplySkills.crusaderConfig.signatureCrusaderConsecrationTauntDuration;
                int mightStacks = SimplySkills.crusaderConfig.signatureCrusaderConsecrationMightStacks - 1;
                int mightStacksMax = SimplySkills.crusaderConfig.signatureCrusaderConsecrationMightStacksMax - 1;
                int spellforgedStacks = SimplySkills.crusaderConfig.signatureCrusaderConsecrationSpellforgedStacks - 1;
                int spellforgedStacksMax = SimplySkills.crusaderConfig.signatureCrusaderConsecrationSpellforgedStacksMax - 1;

                Box box = HelperMethods.createBox(player, radius * 2);
                if (player.age % hitFrequency == 0) {
                    player.heal((float) damage / 5);
                    for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {

                                if (le.isUndead() && HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationConsecrationWard, player))
                                    le.setVelocity((le.getX() - player.getX()) /4,  (le.getY() - player.getY()) /4, (le.getZ() - player.getZ()) /4);

                                le.timeUntilRegen = 0;
                                le.damage(player.getDamageSources().magic(), (float) damage);
                                le.timeUntilRegen = 1;

                                // Taunt
                                if ((le instanceof MobEntity me) && HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationConsecrationTaunt, player)) {
                                    SimplyStatusEffectInstance tauntEffect = new SimplyStatusEffectInstance(
                                            EffectRegistry.TAUNTED, tauntDuration, 0, false,
                                            false, true);
                                    tauntEffect.setSourceEntity(livingEntity);
                                    me.addStatusEffect(tauntEffect);
                                }


                            }
                            if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, player)) {
                                le.heal((float) damage / 4);
                                if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationConsecrationMighty, player))
                                    HelperMethods.incrementStatusEffect(le, EffectRegistry.MIGHT, hitFrequency+1, mightStacks, mightStacksMax);
                                if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationConsecrationSpellforged, player))
                                    HelperMethods.incrementStatusEffect(le, EffectRegistry.SPELLFORGED, hitFrequency+1, spellforgedStacks, spellforgedStacksMax);
                            }
                        }
                    }
                }
                if (player.age % hitFrequency == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_ascend.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.4, 0);
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.2, 0);
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT25,
                            SoundCategory.PLAYERS, 0.05f, 0.8f);
                }
                if (player.age % hitFrequency-10 == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_spell.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.2, 0);
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.3, 0);
                }
                if (player.age % hitFrequency-5 == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.4, 0);
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
