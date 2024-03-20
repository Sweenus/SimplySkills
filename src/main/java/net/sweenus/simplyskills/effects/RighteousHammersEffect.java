package net.sweenus.simplyskills.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.List;
import java.util.Objects;

public class RighteousHammersEffect extends StatusEffect {
    public RighteousHammersEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient() && livingEntity.age % Math.max((22 - (amplifier * 2)), 1) == 0) {
            int radius = 4;
            Box box = HelperMethods.createBox(livingEntity, radius);

            livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                    .filter(Objects::nonNull)
                    .filter(entity -> entity instanceof LivingEntity)
                    .filter(entity -> livingEntity instanceof ServerPlayerEntity)
                    .forEach(entity -> {
                        LivingEntity le = (LivingEntity) entity;
                        ServerPlayerEntity playerEntity = (ServerPlayerEntity) livingEntity;
                        if (HelperMethods.checkFriendlyFire(le, playerEntity)) {
                            le.timeUntilRegen = 0;
                            le.damage(playerEntity.getWorld().getDamageSources().playerAttack(playerEntity),
                                    (float) playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.8f);
                            le.timeUntilRegen = 0;
                        }
                    });

            if (livingEntity instanceof PlayerEntity player && AscendancyAbilities.getAscendancyPoints(player) > 29) {
                int radiusThrow = (int) (radius * 2.5);
                Box box2 = HelperMethods.createBox(livingEntity, radiusThrow);
                ServerWorld world = (ServerWorld) livingEntity.getWorld();
                List<Entity> nearbyEntities = world.getOtherEntities(livingEntity, box2, EntityPredicates.VALID_LIVING_ENTITY);

                if (!nearbyEntities.isEmpty()) {
                    Entity randomEntity = nearbyEntities.get(player.getRandom().nextInt(nearbyEntities.size()));

                    if (randomEntity instanceof LivingEntity ee) {
                        if (HelperMethods.checkFriendlyFire(ee, player)) {
                            BlockPos blockPos = ee.getBlockPos().up(1);
                            SignatureAbilities.castSpellEngineIndirectTarget(player, "simplyskills:righteous_hammer_projectile", 20, ee, blockPos);
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
