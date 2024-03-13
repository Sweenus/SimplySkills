package net.sweenus.simplyskills.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;

import java.util.List;
import java.util.UUID;

public class DynamicDamage {


    public static float dynamicDamageReduction(LivingEntity attacker, LivingEntity target, float amount, float lastDamageTaken, float damage, float lastDamageTime) {

        if (attacker != null && lastDamageTaken > 0 && SimplySkills.generalConfig.enableDDR) {
            if (!(target instanceof PlayerEntity) || SimplySkills.generalConfig.DDRAffectsPlayers) {
                World world = attacker.getWorld();
                float maxHp = target.getMaxHealth();
                float thresholdCheck = amount / maxHp;
                float ddrAttackSpeedWeight = (float) SimplySkills.generalConfig.DDRAttackSpeedWeight / 100;
                float ddrAmount = (float) SimplySkills.generalConfig.DDRAmount / 100;
                float ddrHealthThreshold = (float) SimplySkills.generalConfig.DDRHealthThreshold / 100;
                float damageFrequency = 0.01f + (world.getTime() - lastDamageTime) / (damage * ddrAttackSpeedWeight);
                float healthPercent = Math.min((damage / maxHp) * damage, 0.9f * damage);
                float minimumHp = (float) SimplySkills.generalConfig.DDRHealthRequirement;

                if (thresholdCheck > ddrHealthThreshold && maxHp >= minimumHp) {
                    float damageReduction = Math.min((healthPercent * ddrAmount), (damage / 2));
                    float newAmount = Math.max((damage - damageReduction), 1) * Math.max(Math.min(damageFrequency, 1.0f), 0.3f);
                    if (SimplySkills.generalConfig.enableDDRDebugLog && attacker instanceof PlayerEntity)
                        attacker.sendMessage(Text.literal("§fDamage reduced from §6" + damage + " §fto§a " + newAmount + " §fusing DR: §6" + damageReduction + "§f & SDR: §b" + damageFrequency));
                    //System.out.println("Damage reduced from " + damage + " to " + newAmount + " using DR: " + damageReduction + " & SDR: " + damageFrequency);
                    return newAmount;
                }
            }
        }
        return amount;
    }

    public static void dynamicAttributeScaling(LivingEntity entity, EntityAttribute attribute, String name, double amount, UUID uuid) {
        if (entity.getAttacker() != null && (entity.getAttacker() instanceof ServerPlayerEntity player)) {
            EntityAttributeInstance attributeInstance = entity.getAttributeInstance(attribute);
            int pointsSpent = getSpentPoints(player);
            double totalAmount = amount * pointsSpent;
            if (attributeInstance != null && attributeInstance.getModifier(uuid) == null) {
                EntityAttributeModifier modifier = new EntityAttributeModifier(uuid, name, totalAmount, EntityAttributeModifier.Operation.ADDITION);
                attributeInstance.addTemporaryModifier(modifier);
                if (attribute.equals(EntityAttributes.GENERIC_MAX_HEALTH))
                    entity.heal((float) totalAmount);
            }
        }
    }

    public static void dynamicPlayerCountScaling(LivingEntity livingEntity) {
        double checkDistance = SimplySkills.generalConfig.DASRadius;

        // Collect nearby players
        List<ServerPlayerEntity> nearbyPlayers = livingEntity.getWorld().getEntitiesByClass(
                ServerPlayerEntity.class,
                livingEntity.getBoundingBox().expand(checkDistance),
                playerEntity -> true
        );

        int totalPointsSpent = 0;
        int totalPlayerCount = 0;
        float entityTypeModifier = 1.0f;
        if (livingEntity instanceof PassiveEntity
                || livingEntity instanceof SchoolingFishEntity
                || livingEntity instanceof VillagerEntity
                || livingEntity instanceof ArmorStandEntity)
            entityTypeModifier = SimplySkills.generalConfig.DASPassiveEntityModifier;

        // Iterate through each player and accumulate the total points spent
        for (ServerPlayerEntity player : nearbyPlayers) {
            int pointsSpent = getSpentPoints(player);
            totalPlayerCount++;
            //The lower the Player Scaling Weight number, the less impact each successive player has on scaling
            double playerCountDampener = totalPointsSpent * ((double) totalPlayerCount / SimplySkills.generalConfig.DASPlayerScalingWeight);
            totalPointsSpent += (int) Math.max(((pointsSpent - playerCountDampener) * entityTypeModifier), 0);

            if (SimplySkills.generalConfig.enableDASDebugLog) {
                String message = "§fScaling §6" + livingEntity.getName().getString() +
                        " §fusing§a " + player.getName().getString() +
                        " §fspent skill points of§6 " + getSpentPoints(player) +
                        "§f. Total scale factor: §b" + totalPointsSpent;
                player.sendMessage(Text.literal(message));
            }

        }

        if (SimplySkills.generalConfig.enableDAS && !(livingEntity instanceof PlayerEntity)) {
            if (SimplySkills.generalConfig.enableDASDebugLog) {
                String message = "Attempting to apply DAS attributes to " + livingEntity.getName().getString() + ". Player Count: " + totalPlayerCount + ". total points spent: " + totalPointsSpent;
                System.out.println(message);
            }
            if (SimplySkills.generalConfig.DASHealth > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_MAX_HEALTH, "SimplySkills health DAS",
                        SimplySkills.generalConfig.DASHealth, UUID.fromString("631937f6-bc47-486b-b07a-542823d668a6"), totalPointsSpent);
            if (SimplySkills.generalConfig.DASAttack > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_ATTACK_DAMAGE, "SimplySkills attack damage DAS",
                        SimplySkills.generalConfig.DASAttack, UUID.fromString("8097403f-ed25-4534-b7f9-854e16ef2fbb"), totalPointsSpent);
            if (SimplySkills.generalConfig.DASArmor > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_ARMOR, "SimplySkills armor DAS",
                        SimplySkills.generalConfig.DASArmor, UUID.fromString("eae99963-45c4-4651-b501-4b2e16879705"), totalPointsSpent);
            if (SimplySkills.generalConfig.DASArmorToughness > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "SimplySkills armor toughness DAS",
                        SimplySkills.generalConfig.DASArmorToughness, UUID.fromString("0794ab19-7dbd-421f-90c7-6b65e3aee495"), totalPointsSpent);
            if (SimplySkills.generalConfig.DASSpeed / 100 > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_MOVEMENT_SPEED, "SimplySkills movement speed DAS",
                        SimplySkills.generalConfig.DASSpeed / 100, UUID.fromString("426d5ed8-020b-484f-93d9-4327a3c05c97"), totalPointsSpent);
            if (SimplySkills.generalConfig.DASKnockbackResist / 100 > 0)
                DynamicDamage.dynamicTotalAttributeScaling(livingEntity, EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "SimplySkills knockback resistance DAS",
                        SimplySkills.generalConfig.DASKnockbackResist / 100, UUID.fromString("9ffef649-025d-4c30-98ed-d4378cd07d36"), totalPointsSpent);
        }

    }

    public static void dynamicTotalAttributeScaling(LivingEntity entity, EntityAttribute attribute, String name, double amount, UUID uuid, double pointsSpent) {
        EntityAttributeInstance attributeInstance = entity.getAttributeInstance(attribute);
        //If pointsSpent is 0 and an existing modifier is found, remove it
        if (pointsSpent == 0) {
            if (attributeInstance != null && attributeInstance.getModifier(uuid) != null) {
                if (attribute.equals(EntityAttributes.GENERIC_MAX_HEALTH)) {
                    // Remove health attribute while maintaining health to maxHealth ratio
                    float healthRatio = entity.getHealth() / entity.getMaxHealth();
                    attributeInstance.removeModifier(uuid);
                    entity.setHealth(entity.getMaxHealth() * healthRatio);
                } else {
                    attributeInstance.removeModifier(uuid);
                }
            }
            return;
        }

        double totalAmount = amount * pointsSpent;
        if (attributeInstance != null) {
            EntityAttributeModifier existingModifier = attributeInstance.getModifier(uuid);
            if (existingModifier == null) {
                // If the modifier does not exist, add a new one
                EntityAttributeModifier newModifier = new EntityAttributeModifier(uuid, name, totalAmount, EntityAttributeModifier.Operation.ADDITION);
                attributeInstance.addTemporaryModifier(newModifier);
                if (attribute.equals(EntityAttributes.GENERIC_MAX_HEALTH)) {
                    entity.heal((float) totalAmount);
                }
            } else if (existingModifier.getValue() != totalAmount) {
                // If the modifier exists but the amount is different, update it by removing & re-adding
                attributeInstance.removeModifier(existingModifier);
                EntityAttributeModifier updatedModifier = new EntityAttributeModifier(uuid, name, totalAmount, EntityAttributeModifier.Operation.ADDITION);
                attributeInstance.addTemporaryModifier(updatedModifier);
                if (attribute.equals(EntityAttributes.GENERIC_MAX_HEALTH)) {
                    // Adjust health accordingly
                    double difference = totalAmount - existingModifier.getValue();
                    entity.heal((float) difference);
                }
            }
        }
    }

    public static int getSpentPoints(ServerPlayerEntity player) {
        List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(player);
        int count = 0;
        for (Category category : unlockedCategories) {
            count += category.getUnlockedSkills(player).size();
        }
        return count;
    }



}
