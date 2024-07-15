package net.sweenus.simplyskills.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InitiateAbilities {

    public static void passiveInitiateNullification(PlayerEntity player) {
        int nullificationFrequency = SimplySkills.initiateConfig.passiveInitiateNullificationFrequency;
        int radius = SimplySkills.initiateConfig.passiveInitiateNullificationRadius;
        if (player.age % nullificationFrequency == 0) {

            Box box = HelperMethods.createBox(player, radius);
            for (Entity entities : player.getWorld().getOtherEntities(player, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                if (entities != null) {
                    if ((entities instanceof LivingEntity) && HelperMethods.checkFriendlyFire((LivingEntity)entities, player)) {
                        LivingEntity le = (LivingEntity) entities;
                        for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                            if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                                HelperMethods.decrementStatusEffect(le, statusEffect.getEffectType());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    public static void passiveInitiateEmpower(PlayerEntity player, @Nullable SpellSchool school, @Nullable Set<? extends SpellSchool> schools) {
        int chance = SimplySkills.initiateConfig.passiveInitiateEmpowerChance;
        int duration = SimplySkills.initiateConfig.passiveInitiateEmpowerDuration;
        int amplifier = SimplySkills.initiateConfig.passiveInitiateEmpowerStacks;
        int amplifierMax = SimplySkills.initiateConfig.passiveInitiateEmpowerMaxStacks;
        List<StatusEffect> list = new ArrayList<>();
        if (school == SpellSchools.ARCANE || (schools != null ? schools.contains(SpellSchools.ARCANE) : false))
            list.add(EffectRegistry.ARCANEATTUNEMENT);
        if (school == SpellSchools.SOUL || (schools != null ? schools.contains(SpellSchools.SOUL) : false))
            list.add(EffectRegistry.SOULATTUNEMENT);
        if (school == SpellSchools.HEALING || (schools != null ? schools.contains(SpellSchools.HEALING) : false))
            list.add(EffectRegistry.HOLYATTUNEMENT);
        if (school == SpellSchools.FIRE || (schools != null ? schools.contains(SpellSchools.FIRE) : false))
            list.add(EffectRegistry.FIREATTUNEMENT);
        if (school == SpellSchools.FROST || (schools != null ? schools.contains(SpellSchools.FROST) : false))
            list.add(EffectRegistry.FROSTATTUNEMENT);
        if (school == SpellSchools.LIGHTNING || (schools != null ? schools.contains(SpellSchools.LIGHTNING) : false))
            list.add(EffectRegistry.LIGHTNINGATTUNEMENT);

        if (!list.isEmpty()) {

            int random = player.getRandom().nextInt(100);
            if (random < chance) {
                random = player.getRandom().nextInt(list.size());
                StatusEffect chosenEffect = list.get(random);
                HelperMethods.incrementStatusEffect(player, chosenEffect, duration, amplifier, amplifierMax);
            }

            if (player.hasStatusEffect(list.get(0))
                    && HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.initiateAttuned, player)) {
                int stackThreshold = SimplySkills.initiateConfig.passiveInitiateAttunedStackThreshold -1;
                StatusEffectInstance statusInstance = player.getStatusEffect(list.get(0));
                if (statusInstance != null && statusInstance.getAmplifier() > stackThreshold) {
                    passiveInitiateAttuned(player, statusInstance);
                }
            }
        }
    }

    public static void passiveInitiateAttuned(PlayerEntity player, StatusEffectInstance statusInstance) {
        int duration = SimplySkills.initiateConfig.passiveInitiateAttunedDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateAttunedStacks;
        int maxStacks = SimplySkills.initiateConfig.passiveInitiateAttunedMaxStacks;
        int frequency = SimplySkills.initiateConfig.passiveInitiateAttunedFrequency;
        if (player.age % frequency == 0) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.PRECISION, duration, stacks, maxStacks);
            HelperMethods.decrementStatusEffect(player, statusInstance.getEffectType());
        }
    }

    public static void passiveInitiateLightningRod(PlayerEntity player) {
        int duration = SimplySkills.initiateConfig.passiveInitiateLightningRodDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateLightningRodStacks;
        int maxStacks = SimplySkills.initiateConfig.passiveInitiateLightningRodMaxStacks;
        int frequency = SimplySkills.initiateConfig.passiveInitiateLightningRodFrequency;
        if (player.age % frequency == 0 && player.getWorld().isThundering()) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.LIGHTNINGATTUNEMENT, duration, stacks, maxStacks);
        }
    }

    public static void passiveInitiateHasty(PlayerEntity player) {
        int duration = SimplySkills.initiateConfig.passiveInitiateHastyDuration;
        int stacks = SimplySkills.initiateConfig.passiveInitiateHastyStacks;
        HelperMethods.incrementStatusEffect(player, StatusEffects.SLOWNESS, duration, stacks, 4);
    }

    public static void passiveInitiateFrail(PlayerEntity player) {
        int attackThreshold = SimplySkills.initiateConfig.passiveInitiateFrailAttackThreshold;
        int weaknessAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailWeaknessAmplifier;
        int miningFatigueAmplifier = SimplySkills.initiateConfig.passiveInitiateFrailMiningFatigueAmplifier;

        if (HelperMethods.getAttackDamage(player.getMainHandStack()) > attackThreshold
                || HelperMethods.getAttackDamage(player.getOffHandStack()) > attackThreshold
                && HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.wayfarerSlender, player)){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    25, weaknessAmplifier, false, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,
                    25, miningFatigueAmplifier, false, false, true));
        }
    }

    public static void passiveInitiateEldritchEnfeeblement(PlayerEntity player, List<Entity> targets) {
        if (targets.isEmpty())
            return;
        double  critDamage = 0;
        double  critChance = 0;
        if (Registries.ATTRIBUTE.get(new Identifier("spell_power:critical_chance")) != null)
            critChance = player.getAttributeValue(Registries.ATTRIBUTE.get(new Identifier("spell_power:critical_chance")));
        if (Registries.ATTRIBUTE.get(new Identifier("spell_power:critical_damage")) != null)
            critDamage = player.getAttributeValue(Registries.ATTRIBUTE.get(new Identifier("spell_power:critical_damage")));
        if (player.getRandom().nextInt(100) < critChance) {
            player.heal((float) Math.min(3, critDamage / 100));
        }
    }

    public static void passiveInitiatePerilousPrecision(PlayerEntity player, List<Entity> targets) {
        if (targets.isEmpty())
            return;
        int chance = Math.max(1, 50 - (int) player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
        if (player.getRandom().nextInt(100) < chance) {
            HelperMethods.incrementStatusEffect(player, EffectRegistry.BARRIER, 60, 1, 10);
        }
    }

}
