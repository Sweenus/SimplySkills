package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.Map;

public class GhostwalkEffect extends StatusEffect {
    public GhostwalkEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.GHOSTWALK)) {
                StatusEffectInstance ghostwalk = player.getStatusEffect(EffectRegistry.GHOSTWALK);
                if (ghostwalk == null)
                    return;

                double bullrushVelocity = 0.02 * ( 61 - ghostwalk.getDuration());
                int bullrushRadius = 10;
                double damageModifier = 0.8 + (0.03 * AscendancyAbilities.getAscendancyPoints(player));
                int bullrushHitFrequency = 5;

                HelperMethods.spawnOrbitParticles(player.getServerWorld(), player.getPos(), ParticleTypes.SMOKE, 1, 20);

                if (ghostwalk.getDuration() % 15 == 0) {
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_RADIANT_EXPIRE,
                            SoundCategory.PLAYERS, 0.3f, 1.0f);
                }

                if (ghostwalk.getDuration() > 10 && ghostwalk.getDuration() < 57) {
                    player.setVelocity(livingEntity.getRotationVector().multiply(+bullrushVelocity));
                    player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                    player.setNoGravity(true);
                    player.velocityModified = true;
                } else if (ghostwalk.getDuration() > 56) {
                    player.setVelocity(livingEntity.getVelocity().x, livingEntity.getVelocity().y + 0.2, livingEntity.getVelocity().z);
                    player.velocityModified = true;
                }
                double damage = (HelperMethods.getHighestAttributeValue(player) * damageModifier);

                Box box = HelperMethods.createBox(player, bullrushRadius);
                int chance = 15;
                for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                    if (entities != null) {
                        if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                            if (ghostwalk.getDuration() % bullrushHitFrequency == 0 && ((LivingEntity) entities).getRandom().nextInt(100) < chance) {
                                le.timeUntilRegen = 0;
                                le.damage(player.getDamageSources().playerAttack(player), (float) damage);
                                HelperMethods.spawnWaistHeightParticles((ServerWorld) player.getWorld(), ParticleTypes.SOUL, player, le, 20);
                                le.timeUntilRegen = 0;
                                if (AscendancyAbilities.getAscendancyPoints(player) > 29)
                                    player.heal((float)damage / 2);
                                return;
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
        if (!entity.getWorld().isClient()) {
            if (entity instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("simplyswords"))
                SimplySwordsGemEffects.warStandard(player);
            entity.setNoGravity(false);
        }

        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (!entity.getWorld().isClient() && entity instanceof  PlayerEntity player) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.SOULSHOCK, 60, 1 + (AscendancyAbilities.getAscendancyPoints(player) / 10), 9);
        }
        super.onApplied(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
