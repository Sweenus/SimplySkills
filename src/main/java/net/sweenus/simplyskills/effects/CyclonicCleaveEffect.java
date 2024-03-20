package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class CyclonicCleaveEffect extends StatusEffect {
    public CyclonicCleaveEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.CYCLONICCLEAVE)) {
                StatusEffectInstance cyclonicCleave = player.getStatusEffect(EffectRegistry.CYCLONICCLEAVE);
                if (cyclonicCleave == null)
                    return;

                double bullrushVelocity = 0.05 * ( 39 - cyclonicCleave.getDuration());
                int bullrushRadius = 2;
                double damageModifier = 0.8 + (0.03 * AscendancyAbilities.getAscendancyPoints(player));
                int bullrushHitFrequency = 5;

                if (cyclonicCleave.getDuration() % 5 == 0 && cyclonicCleave.getDuration() < 25)
                    player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                            SoundCategory.PLAYERS, 1f, 1.1f);

                if (cyclonicCleave.getDuration() > 10) {
                    player.setVelocity(livingEntity.getRotationVector().multiply(+bullrushVelocity));
                    player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                    player.velocityModified = true;
                }
                double damage = (HelperMethods.getHighestAttributeValue(player) * damageModifier);

                Box box = HelperMethods.createBox(player, bullrushRadius);
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null && cyclonicCleave.getDuration() < 30) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            if (AscendancyAbilities.getAscendancyPoints(player) > 29)
                                le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                            if (cyclonicCleave.getDuration() % bullrushHitFrequency == 0) {
                                le.timeUntilRegen = 0;
                                le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                                le.timeUntilRegen = 0;
                                ParticleEffect particleType = ParticleTypes.CLOUD;
                                if (FabricLoader.getInstance().isModLoaded("prominent"))
                                    particleType = ParticleTypes.PORTAL;

                                HelperMethods.spawnParticlesPlane(
                                        player.getWorld(),
                                        particleType,
                                        player.getBlockPos(),
                                        bullrushRadius -1, 0, 1, 0 );
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
