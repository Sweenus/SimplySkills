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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class SkywardSunderEffect extends StatusEffect {
    public SkywardSunderEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.SKYWARDSUNDER)) {
                StatusEffectInstance skywardSunder = player.getStatusEffect(EffectRegistry.SKYWARDSUNDER);
                if (skywardSunder == null)
                    return;

                double bullrushVelocity = 0.1 * ( 46 - skywardSunder.getDuration());
                int bullrushRadius = 2;
                double damageModifier = 0.9 + (0.03 * AscendancyAbilities.getAscendancyPoints(player));
                int slash_1 = 20+10;
                int slash_2 = 5+10;

                if (skywardSunder.getDuration() > slash_1) {
                    player.setVelocity(livingEntity.getRotationVector().multiply(+bullrushVelocity));
                    player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                    player.velocityModified = true;
                }
                if (skywardSunder.getDuration() == slash_1 + 12) {
                    SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:skyward_sunder", 3, player, null);
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.OBJECT_IMPACT_THUD_REPEAT,
                            SoundCategory.PLAYERS, 0.6f, 1.0f);
                }
                if (skywardSunder.getDuration() == slash_1) {
                    player.setVelocity(0, 1.2, 0);
                    player.velocityModified = true;
                }
                if (skywardSunder.getDuration() == slash_2) {
                    player.setVelocity(0, -1.2, 0);
                    player.velocityModified = true;
                }
                if (skywardSunder.getDuration() == slash_2 + 5) {
                    SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:skyward_sunder_slam", 3, player, null);
                }
                if (skywardSunder.getDuration() == slash_2 - 2) {
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.DAMAGE_03,
                            SoundCategory.PLAYERS, 0.8f, 1.0f);
                }
                if (skywardSunder.getDuration() == 12) {
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_EARTH_PUNCH,
                            SoundCategory.PLAYERS, 0.6f, 1.0f);
                }
                if (skywardSunder.getDuration() == 2) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                            player.getBlockPos(),
                            bullrushRadius, 0, 1, 0);
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            ParticleTypes.POOF,
                            player.getBlockPos(),
                            bullrushRadius, 0, 1, 0);
                }


                double damage = (HelperMethods.getHighestAttributeValue(player) * damageModifier);

                Box box = new Box(player.getX() + bullrushRadius, player.getY() + (float) bullrushRadius, player.getZ() + bullrushRadius,
                        player.getX() - bullrushRadius, player.getY() - (float) bullrushRadius, player.getZ() - bullrushRadius);
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player) && (skywardSunder.getDuration() == 1 || skywardSunder.getDuration() == slash_2 || skywardSunder.getDuration() == slash_1)) {
                            le.timeUntilRegen = 0;
                            le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                            le.timeUntilRegen = 0;
                            le.setVelocity(player.getVelocity().x, player.getVelocity().y, player.getVelocity().z);
                            le.velocityModified = true;

                            HelperMethods.spawnParticlesPlane(
                                    player.getWorld(),
                                    ParticleTypes.CLOUD,
                                    player.getBlockPos(),
                                    bullrushRadius - 1, 0, 1, 0);
                        }
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)
                                && skywardSunder.getDuration() > slash_1 && skywardSunder.getDuration() % 2 == 0) {

                            if (AscendancyAbilities.getAscendancyPoints(player) > 30)
                                le.addStatusEffect(new StatusEffectInstance(EffectRegistry.DEATHMARK, 60, 0));

                            le.timeUntilRegen = 0;
                            le.damage(player.getDamageSources().playerAttack(player), 0.5f);
                            le.timeUntilRegen = 0;
                            le.setVelocity(player.getVelocity().x, player.getVelocity().y, player.getVelocity().z);
                            le.velocityModified = true;
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
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient() && entity instanceof  PlayerEntity player) {
            if (player.hasStatusEffect(EffectRegistry.MIGHT)) {
                StatusEffectInstance mightEffect = player.getStatusEffect(EffectRegistry.MIGHT);
                if (mightEffect !=null) {
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.BARRIER, mightEffect.getDuration(), mightEffect.getAmplifier(), 9);
                }
            }
        }
        super.onApplied(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
