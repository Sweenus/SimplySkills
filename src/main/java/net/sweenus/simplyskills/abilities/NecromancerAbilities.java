package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.entities.GreaterDreadglareEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.EntityRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class NecromancerAbilities {

    public static void effectNecromancerWinterborn(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationWinterborn, player)) {
            double frostSpellPower = player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FROST).attribute);
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.SOULSHOCK, 220, (int) frostSpellPower, false ,false, false));
        }
    }

    public static void effectNecromancerEnrage(LivingEntity livingEntity,PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationEnrage, player)) {
            Box box = HelperMethods.createBoxHeight(livingEntity, 15);
            for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof Tameable te) && !HelperMethods.checkFriendlyFire((LivingEntity) te, player)) {
                        HelperMethods.incrementStatusEffect((LivingEntity) te, StatusEffects.STRENGTH, 200, 1, 3);
                        HelperMethods.incrementStatusEffect((LivingEntity) te, StatusEffects.RESISTANCE, 200, 1, 3);
                    }
                }
            }
            player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_VOICE_20,
                    SoundCategory.PLAYERS, 0.2f, 1.2f);
        }
    }

    public static void effectNecromancerDeathEssence(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationDeathEssence, player)) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.BONEARMOR, 400, 1, 25);
        }
    }

    public static void effectDeathWarden(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationDeathWarden, player)) {
            boolean success = false;
            Box box = HelperMethods.createBoxHeight(player, 15);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {
                if (entities != null) {
                    if ((entities instanceof Tameable te) && te.getOwner() != null && te.getOwner().equals(player)) {
                        float healAmount = (float) (player.getMaxHealth() * 0.15);
                        player.heal(healAmount);
                        entities.damage(player.getDamageSources().generic(), healAmount);
                        HelperMethods.spawnWaistHeightParticles((ServerWorld) player.getWorld(), ParticleTypes.POOF, player, entities, 20);
                        success = true;
                    }
                }
            }
            if (success)
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_VOICE_20,
                        SoundCategory.PLAYERS, 0.2f, 1.3f);
        }
    }

    public static void effectPlague(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationPlague, player) && player.age % 20 == 0
                && player.getStatusEffects() != null && HelperMethods.hasHarmfulStatusEffect(player)) {
            Box box = HelperMethods.createBoxHeight(player, 15);
            List<Entity> validEntities = player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)
                    .stream()
                    .filter(entities -> entities instanceof TameableEntity)
                    .filter(entities -> {
                        TameableEntity te = (TameableEntity) entities;
                        return te.getOwner() != null && te.getOwner().equals(player);
                    })
                    .toList();

            if (!validEntities.isEmpty()) {
                // Choose a random entity from the list
                Entity randomEntity = validEntities.get(new Random().nextInt(validEntities.size()));
                HelperMethods.buffSteal((LivingEntity) randomEntity, player, true, true, true, false);
                HelperMethods.spawnWaistHeightParticles((ServerWorld) player.getWorld(), ParticleTypes.EFFECT, player, randomEntity, 12);
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_03,
                        SoundCategory.PLAYERS, 0.1f, 1.5f);
            }
        }
    }

    public static void effectPestilence(PlayerEntity player, LivingEntity minion, LivingEntity target) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationPestilence, player)) {
            HelperMethods.buffSteal(target, minion, true, true, true, false);
        }
    }

    public static void effectDelightfulSuffering(PlayerEntity player) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationDelightfulSuffering, player)) {

            int duration = 800;
            List<StatusEffectInstance> list = new ArrayList<>();
            list.add(0, new StatusEffectInstance(StatusEffects.HUNGER, duration, 0, false, false, true));
            list.add(1, new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 0, false, false, true));
            list.add(2, new StatusEffectInstance(StatusEffects.WITHER, duration, 0, false, false, true));
            list.add(3, new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 0, false, false, true));
            list.add(4, new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 0, false, false, true));
            if (!list.isEmpty()) {
                StatusEffect chosenEffect = list.get(player.getRandom().nextInt(4)).getEffectType();
                HelperMethods.incrementStatusEffect(player, chosenEffect, duration, 1, 2);
            }
        }
    }

    public static void effectEndlessServitude(PlayerEntity player, TameableEntity minion) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationEndlessServitude, player)) {
            int chanceThreshold = Math.min(21 + HelperMethods.countHarmfulStatusEffects(minion) * 5, 60);

            int chance = minion.getRandom().nextInt(100);
            if (chance < chanceThreshold) {
                EntityType<?> entityType = minion.getType();
                summonMinion((EntityType<? extends LivingEntity>) entityType, player);
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_02,
                        SoundCategory.PLAYERS, 0.2f, 1.0f);
            }
        }
    }

    public static void effectShadowCombust(PlayerEntity player, TameableEntity minion) {
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationShadowCombust, player)) {

            int radius = 4;
            if (minion instanceof GreaterDreadglareEntity)
                radius = 7;
            Box box = HelperMethods.createBox(minion, radius);

            minion.getWorld().getOtherEntities(minion, box, EntityPredicates.VALID_LIVING_ENTITY).stream()
                    .filter(Objects::nonNull)
                    .filter(entity -> entity instanceof LivingEntity)
                    .forEach(entity -> {
                        LivingEntity le = (LivingEntity) entity;
                        if (player != null && HelperMethods.checkFriendlyFire(le, player)) {
                            le.timeUntilRegen = 0;
                            float damageMulti = 3.2f;
                            if (minion instanceof GreaterDreadglareEntity)
                                damageMulti = 6.4f;
                            le.damage(player.getWorld().getDamageSources().indirectMagic(player, player),
                                    (float) player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute) * damageMulti);
                            HelperMethods.spawnWaistHeightParticles((ServerWorld) minion.getWorld(), ParticleTypes.SMOKE, minion, le, 8);
                            le.timeUntilRegen = 0;
                        }
                    });
            if (player != null) {
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_SPELL_03,
                        SoundCategory.PLAYERS, 0.1f, 1.0f);
                player.getWorld().playSoundFromEntity(null, player, SoundEvents.ENTITY_GENERIC_EXPLODE,
                        SoundCategory.PLAYERS, 0.1f, 1.0f);
            }
            HelperMethods.spawnOrbitParticles((ServerWorld) minion.getWorld(), minion.getPos(), ParticleTypes.EXPLOSION, 1, 2);
            HelperMethods.spawnOrbitParticles((ServerWorld) minion.getWorld(), minion.getPos(), ParticleTypes.SOUL, 2, 20);
            HelperMethods.spawnOrbitParticles((ServerWorld) minion.getWorld(), minion.getPos(), ParticleTypes.SMOKE, radius, 20);
            if (minion.isAlive())
                minion.damage(minion.getWorld().getDamageSources().indirectMagic(minion, minion), minion.getMaxHealth());
        }
    }

    //------- SIGNATURE ABILITIES --------

    public static int getMinionLimit(String necromancerTree, PlayerEntity player) {
        int count = 0;
        for (String skillRef : SkillReferencePosition.undeadLegionSkills) {
            if (HelperMethods.isUnlocked(necromancerTree, skillRef, player)) {
                count++;
            }
        }
        return 1 + count; // Add 1 for default summon count
    }

    // Summoning Ritual
    public static boolean signatureNecromancerSummoningRitual(String necromancerTree, PlayerEntity player) {
        for (int i = 0; i < getMinionLimit(necromancerTree, player); i ++) {

            if (HelperMethods.isUnlocked(necromancerTree, SkillReferencePosition.necromancerSpecialisationGreaterDreadglare, player)) {
                summonMinion(EntityRegistry.GREATER_DREADGLARE, player);
                break;
            }
            else if (HelperMethods.isUnlocked(necromancerTree, SkillReferencePosition.necromancerSpecialisationSummonWraith, player)) {
                int chance = player.getRandom().nextInt(100);

                if (HelperMethods.isUnlocked("simplyskills:necromancer",
                        SkillReferencePosition.necromancerSpecialisationWraithLegion, player))
                    chance = 5;

                if (chance < 50)
                    summonMinion(EntityRegistry.WRAITH, player);
                else
                    summonMinion(EntityRegistry.DREADGLARE, player);
            } else {
                summonMinion(EntityRegistry.DREADGLARE, player);
            }
        }
        player.getWorld().playSoundFromEntity(null, player, SoundRegistry.MAGIC_SHAMANIC_VOICE_20,
                SoundCategory.PLAYERS, 0.3f, 1.0f);
        return true;
    }

    public static void summonMinion(EntityType<? extends LivingEntity> livingEntity, PlayerEntity player) {
        LivingEntity minion = livingEntity.spawn((ServerWorld) player.getWorld(),
                player.getBlockPos().up(4).offset(player.getMovementDirection(), 3),
                SpawnReason.MOB_SUMMONED);

        if (minion != null) {
            if (minion instanceof TameableEntity tameableMinion) {
                tameableMinion.setOwner(player);
                tameableMinion.setTamed(true);
                tameableMinion.setPositionTarget(player.getBlockPos().up(3), 32);
                if (HelperMethods.isUnlocked("simplyskills:necromancer",
                        SkillReferencePosition.necromancerSpecialisationShadowAura, player)) {
                    int amplifier = 0;
                    if (tameableMinion instanceof GreaterDreadglareEntity)
                        amplifier = 3;
                    minion.addStatusEffect(new StatusEffectInstance(EffectRegistry.SHADOWAURA, 2400, amplifier, false, false, false));
                }
            }

            double attackDamageMultiplier = 1.2;
            if (minion instanceof GreaterDreadglareEntity)
                attackDamageMultiplier = 3.0;
            double healthMultiplier = getHealthMultiplier(livingEntity);
            setMinionAttributes(player, minion, attackDamageMultiplier, healthMultiplier);
        }
    }

    private static double getHealthMultiplier(EntityType<?> entityType) {
        if (entityType.equals(EntityRegistry.DREADGLARE)) {
            return 1.4;
        } else if (entityType.equals(EntityRegistry.WRAITH)) {
            return 0.8;
        } else if (entityType.equals(EntityRegistry.GREATER_DREADGLARE)) {
            return 4.8;
    }
        return 1.0;
    }

    private static void setMinionAttributes(PlayerEntity player, LivingEntity minion, double attackDamageMultiplier, double healthMultiplier) {
        double attackDamage = 3 + (attackDamageMultiplier * player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.SOUL).attribute));
        EntityAttributeInstance attackAttribute = minion.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attackAttribute != null) {
            attackAttribute.setBaseValue(attackDamage);
        }

        double maxHealth = 1 + (healthMultiplier * player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
        EntityAttributeInstance healthAttribute = minion.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.setBaseValue(maxHealth);
            minion.heal((float)maxHealth);
        }

        // Necrotic Fortification
        if (HelperMethods.isUnlocked("simplyskills:necromancer",
                SkillReferencePosition.necromancerSpecialisationNecroticFortification, player)) {
            double multiplier = 0.5;
            if (minion instanceof GreaterDreadglareEntity)
                multiplier = 1.0;
            double maxArmor = 1 + (multiplier * player.getAttributeValue(EntityAttributes.GENERIC_ARMOR));
            double maxArmorToughness = 1 + (multiplier * player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
            EntityAttributeInstance armorAttribute = minion.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
            EntityAttributeInstance armorToughnessAttribute = minion.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            if (armorAttribute != null) {
                armorAttribute.setBaseValue(maxArmor);
            }
            if (armorToughnessAttribute != null) {
                armorToughnessAttribute.setBaseValue(maxArmorToughness);
            }
        }
    }

}
