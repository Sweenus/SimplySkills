package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.paladins.effect.Effects;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class SacredOnslaughtEffect extends StatusEffect {
    public SacredOnslaughtEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {

                int velocity = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtVelocity;
                int radius = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtRadius;
                double damageMultiplier = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtDMGMultiplier;
                double healing = (player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute) * damageMultiplier);
                int hitFrequency = 10;
                int stunDuration = SimplySkills.crusaderConfig.signatureCrusaderSacredOnslaughtStunDuration;

                player.setVelocity(livingEntity.getRotationVector().multiply(+velocity));
                player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                player.velocityModified = true;
                double damage = (player.getArmor() * damageMultiplier);

                Box box = HelperMethods.createBox(player, radius*2);
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if (entities instanceof LivingEntity le) {
                            if (player.age % hitFrequency == 0) {
                                if (HelperMethods.checkFriendlyFire(le, player) && player.isBlocking()) {
                                    le.setVelocity((le.getX() - player.getX()) /4,  (le.getY() - player.getY()) /4, (le.getZ() - player.getZ()) /4);
                                    le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT32,
                                            SoundCategory.PLAYERS, 0.6f, 1.0f);

                                    if (HelperMethods.isUnlocked("simplyskills:crusader", SkillReferencePosition.crusaderSpecialisationSacredOnslaughtStun, player))
                                        le.addStatusEffect(new StatusEffectInstance(Effects.JUDGEMENT, stunDuration));

                                }
                                if (!HelperMethods.checkFriendlyFire(le, player)
                                        && HelperMethods.isUnlocked("simplyskills:crusader",
                                        SkillReferencePosition.crusaderSpecialisationSacredOnslaughtHeal, player)) {
                                    SignatureAbilities.castSpellEngineIndirectTarget(player, "paladins:divine_protection", 32, le);
                                    le.heal((float)healing);
                                }

                                HelperMethods.spawnParticlesPlane(
                                        player.getWorld(),
                                        ParticleTypes.CLOUD,
                                        player.getBlockPos(),
                                        radius-2, 0, 1, 0 );
                            }
                        }
                    }
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("simplyswords"))
            SimplySwordsGemEffects.warStandard(player);

        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
