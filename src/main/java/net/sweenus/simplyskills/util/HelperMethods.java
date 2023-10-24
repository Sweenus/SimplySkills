package net.sweenus.simplyskills.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.network.ModPacketHandler;

import java.util.Collection;
import java.util.List;

import static net.puffish.skillsmod.api.SkillsAPI.getCategory;

public class HelperMethods {


    //Check if we should be able to hit the target
    public static boolean checkFriendlyFire (LivingEntity livingEntity, PlayerEntity player) {
        if (livingEntity == null || player == null)
            return false;
        if (!checkEntityBlacklist(livingEntity, player))
            return false;

        // Check if the player and the living entity are on the same team
        AbstractTeam playerTeam = player.getScoreboardTeam();
        AbstractTeam entityTeam = livingEntity.getScoreboardTeam();
        if (playerTeam != null && entityTeam != null && livingEntity.isTeammate(player)) {
            // They are on the same team, so friendly fire should not be allowed
            return false;
        }

        if (livingEntity instanceof PlayerEntity playerEntity) {
            if (playerEntity == player)
                return false;
            return playerEntity.shouldDamagePlayer(player);
        }
        if (livingEntity instanceof Tameable tameable) {
            if (tameable.getOwner() != null) {
                if (tameable.getOwner() != player
                        && (tameable.getOwner() instanceof PlayerEntity ownerPlayer))
                    return player.shouldDamagePlayer(ownerPlayer);
                return tameable.getOwner() != player;
            }
            return true;
        }
        return true;
    }

    // Check for back attack
    public static boolean isBehindTarget(LivingEntity attacker, LivingEntity target) {
        return target.getBodyYaw() < (attacker.getBodyYaw() + 32)
                && target.getBodyYaw() > (attacker.getBodyYaw() - 32);
    }

    //Checks if skill is unlocked with presence checks.
    //If provided null for the skill argument, it will instead return if the category is unlocked.
    public static boolean isUnlocked (String skillTree, String skill, LivingEntity livingEntity) {
        Identifier tree = new Identifier(skillTree);
        if (livingEntity instanceof ServerPlayerEntity serverPlayer) {
            if (getCategory(tree).isPresent()) {
                if (skill != null) {
                    Collection<Skill> skillList = getCategory(tree).get().getUnlockedSkills(serverPlayer);
                    Skill realSkill = new Skill(getCategory(tree).get(), skill);
                    //System.out.println("Checking " + tree + " for skill: " + realSkill.getId());

                    for (Skill value : skillList) {
                        if (value.getId().equals(realSkill.getId())) {
                            //System.out.println("Found a match!     " + value.getId() + "  is equal to  " + realSkill.getId());
                            return value.getId().equals(realSkill.getId());
                        }
                    }
                }
                else {
                    //return SkillsAPI.getUnlockedCategories(serverPlayer).equals(SkillsAPI.getCategory(tree));

                    Collection<Category> categories = SkillsAPI.getUnlockedCategories(serverPlayer);
                    for (Category value : categories) {
                        if (value.getId().equals(tree)) {
                            //System.out.println("Found a CATEGORY match!     " + value.getId() + "  is equal to  " + tree);
                            return value.getId().equals(tree);
                        }
                    }

                }
            }
        }
        return false;
    }

    //Check if the target matches blacklisted entities (expand this to be configurable if there is demand)
    public static boolean checkEntityBlacklist (LivingEntity livingEntity, PlayerEntity player) {
        if (livingEntity == null || player == null) {
            return false;
        }
        return !(livingEntity instanceof ArmorStandEntity)
                && !(livingEntity instanceof VillagerEntity);
    }

    //Get Item attack damage
    public static double getAttackDamage(ItemStack stack){
        return stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .stream()
                .mapToDouble(EntityAttributeModifier::getValue)
                .sum();
    }

    //Create Box
    public static Box createBox(Entity entity, int radius) {
        Box box = new Box(entity.getX() + radius, entity.getY() + (float) radius / 3, entity.getZ() + radius,
                entity.getX() - radius, entity.getY() - (float) radius / 3, entity.getZ() - radius);

        return box;
    }
    public static Box createBoxAtBlock(BlockPos blockpos, int radius) {
        Box box = new Box(blockpos.getX() + radius, blockpos.getY() + radius, blockpos.getZ() + radius,
                blockpos.getX() - radius, blockpos.getY() - radius, blockpos.getZ() - radius);

        return box;
    }
    public static Box createBoxBetween(BlockPos blockpos, BlockPos blockpos2, int radius) {
        Box box = new Box(blockpos.getX() + radius, blockpos.getY() + radius, blockpos.getZ() + radius,
                blockpos2.getX() - radius, blockpos2.getY() - radius, blockpos2.getZ() - radius);

        return box;
    }


    /*
     * getTargetedEntity taken heavily from ZsoltMolnarrr's CombatSpells
     * https://github.com/ZsoltMolnarrr/CombatSpells/blob/main/common/src/main/java/net/combatspells/utils/TargetHelper.java#L72
     */
    public static Entity getTargetedEntity(Entity user, int range) {
        Vec3d rayCastOrigin = user.getEyePos();
        Vec3d userView = user.getRotationVec(1.0F).normalize().multiply(range);
        Vec3d rayCastEnd = rayCastOrigin.add(userView);
        Box searchBox = user.getBoundingBox().expand(range, range, range);
        EntityHitResult hitResult = ProjectileUtil.raycast(user, rayCastOrigin, rayCastEnd, searchBox,
                (target) -> !target.isSpectator() && target.canHit() && target instanceof LivingEntity, range * range);
        if (hitResult != null) {
            return hitResult.getEntity();
        }
        return null;
    }

    public static Vec3d getPositionLookingAt(PlayerEntity player, int range) {
        HitResult result = player.raycast(range, 0, false);
        if (!(result.getType() == HitResult.Type.BLOCK)) return null;

        BlockHitResult blockResult = (BlockHitResult) result;
        return blockResult.getPos();
    }

    public static void incrementStatusEffect(
            LivingEntity livingEntity,
            StatusEffect statusEffect,
            int duration,
            int amplifier,
            int amplifierMax) {

        if (livingEntity.hasStatusEffect(statusEffect)) {
            int currentAmplifier = livingEntity.getStatusEffect(statusEffect).getAmplifier();

            if (currentAmplifier >= amplifierMax) {
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        statusEffect, duration, currentAmplifier, false, false, true));
                return;
            }

            livingEntity.addStatusEffect(new StatusEffectInstance(
                    statusEffect, duration, currentAmplifier + amplifier, false, false, true));
        }
        livingEntity.addStatusEffect(new StatusEffectInstance(
                statusEffect, duration, amplifier, false,false, true ));

    }

    public static void capStatusEffect (LivingEntity livingEntity) {

        int spellforgedCap = 5;
        int mightCap = 30;

        List<StatusEffectInstance> list = livingEntity.getStatusEffects().stream().toList();
        if (!list.isEmpty()) {
            for (StatusEffectInstance statusEffectInstance : list) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType();

                switch (statusEffect.getName().getString()) {

                    case "Spellforged":
                        if (statusEffectInstance.getAmplifier() > spellforgedCap)
                            decrementStatusEffects(livingEntity, statusEffect,
                                    statusEffectInstance.getAmplifier() - spellforgedCap);
                    case "Might":
                        if (statusEffectInstance.getAmplifier() > mightCap)
                            decrementStatusEffects(livingEntity, statusEffect,
                                    statusEffectInstance.getAmplifier() - mightCap);

                }
            }
        }
    }

    public static boolean stringContainsAny (String string, String[] stringList) {
        for (String s : stringList) {
            if (string.contains(s))
                return true;
        }
        return false;
    }

    public static void decrementStatusEffect(
            LivingEntity livingEntity,
            StatusEffect statusEffect) {

        if (livingEntity.hasStatusEffect(statusEffect)) {
            int currentAmplifier = livingEntity.getStatusEffect(statusEffect).getAmplifier();
            int currentDuration = livingEntity.getStatusEffect(statusEffect).getDuration();

            if (currentAmplifier < 1 ) {
                livingEntity.removeStatusEffect(statusEffect);
                return;
            }

            livingEntity.removeStatusEffect(statusEffect);
            livingEntity.addStatusEffect(new StatusEffectInstance(
                    statusEffect, currentDuration, currentAmplifier - 1, false, false, true));
        }
    }

    public static void decrementStatusEffects(
            LivingEntity livingEntity,
            StatusEffect statusEffect,
            int stacksRemoved) {

        if (livingEntity.hasStatusEffect(statusEffect)) {
            int currentAmplifier = livingEntity.getStatusEffect(statusEffect).getAmplifier();
            int currentDuration = livingEntity.getStatusEffect(statusEffect).getDuration();

            if (currentAmplifier < 1 ) {
                livingEntity.removeStatusEffect(statusEffect);
                return;
            }

            livingEntity.removeStatusEffect(statusEffect);
            livingEntity.addStatusEffect(new StatusEffectInstance(
                    statusEffect, currentDuration, currentAmplifier - stacksRemoved, false, false, true));
        }
    }

    public static void cleanseStatusEffects(LivingEntity livingEntity, int stacksRemoved) {


    }

    public static boolean buffSteal(
            LivingEntity user,
            LivingEntity target,
            boolean strip,
            boolean singular,
            boolean debuff,
            boolean cleanse) {

        // Strip - removes the status effect
        // Singular - affects one status effect per method call
        // Debuff - affects non-beneficial status effects instead of beneficial
        // Cleanse - does not increment the effect on the user (effectively cleansing when debuff & strip are true)

        List<StatusEffectInstance> list = target.getStatusEffects().stream().toList();
        if (list.isEmpty())
            return false;

        for (StatusEffectInstance statusEffectInstance : list) {
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            int duration = statusEffectInstance.getDuration();
            int amplifier = statusEffectInstance.getAmplifier()-1;

            if (statusEffect.isBeneficial() && !debuff) {
                if (user != null && !cleanse)
                    HelperMethods.incrementStatusEffect(user, statusEffect, duration, 1, amplifier);
                if (strip)
                    HelperMethods.decrementStatusEffect(target, statusEffectInstance.getEffectType());
                if (singular)
                    return true;
            }
            else if (!statusEffect.isBeneficial() && debuff) {
                if (user != null && !cleanse)
                    HelperMethods.incrementStatusEffect(user, statusEffect, duration, 1, amplifier);
                if (strip)
                    HelperMethods.decrementStatusEffect(target, statusEffectInstance.getEffectType());
                if (singular)
                    return true;
            }
        }


        return true;
    }

    //Spawns particles across both client & server
    public static void spawnParticle(World world, ParticleEffect particle, double  xpos, double ypos, double zpos,
                                     double xvelocity, double yvelocity, double zvelocity) {

        if (world.isClient) {
            world.addParticle(particle, xpos, ypos, zpos, xvelocity, yvelocity, zvelocity);
        } else {
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(particle, xpos, ypos, zpos, 1, xvelocity, yvelocity, zvelocity, 0);
            }
        }
    }

    //Spawn particles at plane
    public static void spawnParticlesPlane(
            World world,
            ParticleEffect particle,
            BlockPos blockpos,
            int radius,
            double xvelocity,
            double yvelocity,
            double zvelocity) {

        double xpos = blockpos.getX() - (radius + 1);
        double ypos = blockpos.getY();
        double zpos = blockpos.getZ() - (radius + 1);
        for (int i = radius * 2; i > 0; i--) {
            for (int j = radius * 2; j > 0; j--) {
                float choose = (float) (Math.random() * 1);
                HelperMethods.spawnParticle(world, particle, xpos + i + choose,
                        ypos,
                        zpos + j + choose,
                        xvelocity, yvelocity, zvelocity);
            }
        }
    }


    public static boolean respecialise( ServerPlayerEntity user ) {

        List<String> specialisations = SimplySkills.getSpecialisationsAsArray();
        for (String specialisation : specialisations) {
            getCategory(new Identifier(specialisation)).get().erase(user);
        }
        getCategory(new Identifier("simplyskills:tree")).get().resetSkills(user);
        return true;
    }
    public static boolean levelAll( ServerPlayerEntity user ) {

        List<String> specialisations = SimplySkills.getSpecialisationsAsArray();
        for (String specialisation : specialisations) {
            getCategory(new Identifier(specialisation)).get().unlock(user);
            getCategory(new Identifier(specialisation)).get().setExperience(user, 50000);
        }
        getCategory(new Identifier("simplyskills:tree")).get().setExperience(user, 50000);
        return true;
    }

    public static void treeResetOnDeath(ServerPlayerEntity user ) {
        if (SimplySkills.generalConfig.treeResetOnDeath) {
            resetAllTrees(user);
        }
    }

    public static void resetAllTrees (ServerPlayerEntity user) {
        List<String> specialisations = SimplySkills.getSpecialisationsAsArray();
        for (String specialisation : specialisations) {
            getCategory(new Identifier(specialisation)).get().erase(user);
            getCategory(new Identifier("simplyskills:tree")).get().erase(user);
        }
    }

    public static int getSlotWithStack(PlayerEntity player, ItemStack stack) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            if (ItemStack.areEqual(player.getInventory().getStack(i), stack)) {
                return i;
            }
        }
        return -1; // Return -1 if the stack is not found in the inventory
    }

    public static boolean storeBuildTemplate( ServerPlayerEntity user, ItemStack stack ) {
        List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(user);
        int categoryCount = 0;
        int skillCount = 0;
        String userUUID = user.getUuidAsString();

        stack.getOrCreateNbt().putString("player_name", user.getName().getString());

        if (!stack.getOrCreateNbt().getString("player_uuid").isEmpty()) {
            return false;
        }

        NbtCompound nbt = stack.getOrCreateNbt();

        for (Category category : unlockedCategories) {
            String categoryKey = "category" + categoryCount;
            nbt.putString(categoryKey, category.getId().toString());

            Collection<Skill> unlockedSkills = category.getUnlockedSkills(user);

            for (Skill skill : unlockedSkills) {
                String skillKey = "skill" + skillCount;
                nbt.putString(skillKey, skill.getId());
                skillCount++;
            }
            categoryCount++;
        }

        resetAllTrees(user);
        nbt.putString("player_uuid", userUUID);
        int slot = getSlotWithStack(user, stack);
        if (slot != -1) {
            ModPacketHandler.syncItemStackNbt(user, slot, stack);
        }

        return true;
    }

    public static boolean applyBuildTemplate( ServerPlayerEntity user, ItemStack stack ) {

        NbtCompound nbt = stack.getOrCreateNbt();
        String uuid = user.getUuidAsString();

        if (!nbt.getString("player_uuid").equals(uuid) && !SimplySkills.generalConfig.enableBuildSharing) {
            return false;
        }

        resetAllTrees(user);
        int size = stack.getNbt() != null ? stack.getNbt().getSize() : 0;
        for (int i = 0; i < size; i++) {
            String categoryKey = "category" + i;
            String category = nbt.getString(categoryKey);
            if (category.isEmpty()) continue;

            getCategory(new Identifier(category)).ifPresent(categoryObj -> {
                categoryObj.unlock(user);
                for (int s = 0; s < size; s++) {
                    String skillKey = "skill" + s;
                    String skill = nbt.getString(skillKey);
                    if (skill.isEmpty()) continue;

                    categoryObj.getSkill(skill).ifPresent(skillObj -> skillObj.unlock(user));
                }
            });
        }

        //Clear NBT
        if (!stack.getNbt().isEmpty()) {
            int tempSize = stack.getNbt().getSize();
            for (int i = 0; i < tempSize; i++) {
                String categoryKey = "category" + i;
                if (!nbt.getString(categoryKey).isEmpty()) {
                    nbt.remove(categoryKey);

                    for (int s = 0; s < tempSize; s++) {
                        String skillKey = "skill" + s;
                        if (!nbt.getString(skillKey).isEmpty())
                            nbt.remove(skillKey);
                    }
                }
            }
            nbt.remove("player_uuid");
            nbt.remove("player_name");
        }
        int slot = getSlotWithStack(user, stack);
        if (slot != -1) {
            ModPacketHandler.syncItemStackNbt(user, slot, stack);
        }
        return true;
    }

    public static void printNBT(ItemStack stack, List<Text> tooltip, String type) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null)
            return;

        if (!nbt.isEmpty()) {
            int tempSize = nbt.getSize();
            int skillPrintCount = 0;
            for (int i = 0; i < tempSize; i++) {

                if (!nbt.getString("category" + i).isEmpty()) {
                    if (type.equals("category") && !nbt.getString("category" + i).contains("tree"))
                        tooltip.add(Text.literal("  §6◇ §f" + nbt.getString("category" + i).
                                replace("simplyskills:", "")));
                }
                if (!nbt.getString("skill" + i).isEmpty())
                    skillPrintCount++;
            }

            if (type.equals("skill"))
                tooltip.add(Text.literal("  §b◇ §f" + skillPrintCount));

            if (!nbt.getString("player_name").isEmpty()) {
                String name = nbt.getString("player_name");
                if (type.equals("name"))
                    tooltip.add(Text.literal("§7Bound to: " + name));
            }
        }
    }
    public static int getUnspentPoints(ServerPlayerEntity user) {

        List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(user);
        int unspentSkillPoints = 0;

        for (Category uc : unlockedCategories) {
            unspentSkillPoints += uc.getPointsLeft(user);
        }
        return unspentSkillPoints;
    }



}
