package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.entities.DreadglareEntity;
import net.sweenus.simplyskills.entities.GreaterDreadglareEntity;
import net.sweenus.simplyskills.entities.WraithEntity;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.EntityRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

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
            return 3.8;
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
