package net.sweenus.simplyskills.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DynamicDamage {


    public static float DynamicDamageReduction(LivingEntity attacker, LivingEntity target, float amount, float lastDamageTaken, float damage, float lastDamageTime) {

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

    public static void DynamicAttributeScaling(LivingEntity entity, EntityAttribute attribute, String name, double amount, UUID uuid) {
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

    public static int getSpentPoints(ServerPlayerEntity player) {
        List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(player);
        int count = 0;
        for (Category category : unlockedCategories) {
            count += category.getUnlockedSkills(player).size();
        }
        return count;
    }



}
