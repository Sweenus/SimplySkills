package net.sweenus.simplyskills.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.puffish.skillsmod.api.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.client.SimplySkillsClient;
import net.sweenus.simplyskills.network.ModPacketHandler;
import net.sweenus.simplyskills.network.UpdateUnspentPointsPacket;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.Collection;
import java.util.List;

public class SkillChronicle extends Item {
    public SkillChronicle(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() +10) {
            return TypedActionResult.fail(itemStack);
        }

        world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT6, SoundCategory.PLAYERS, 0.3f, 0.7f);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 60;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient && user.getEquippedStack(EquipmentSlot.MAINHAND) == stack) {
            if (remainingUseTicks < 35)
                user.stopUsingItem();
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient && (user instanceof PlayerEntity player)) {
            if ((user instanceof ServerPlayerEntity serverUser) && remainingUseTicks < 35) {

                List<Category> unlockedCategories = SkillsAPI.getUnlockedCategories(serverUser);
                int pointsRemaining = 0;
                boolean hasSpentPoints = false;
                boolean success = false;

                for (Category uc : unlockedCategories) {

                    //Check for points spent in base tree
                    if (uc.getId().toString().equals("simplyskills:tree")) {
                        pointsRemaining = uc.getPointsLeft(serverUser);
                        Collection<Skill> unlockedSkills = uc.getUnlockedSkills(serverUser);
                        hasSpentPoints = !unlockedSkills.isEmpty();
                        //System.out.println("Checking if we have skills unlocked");
                    }
                }

                //STORE
                if (hasSpentPoints) {
                    //System.out.println("Found skills. Trying to store build.");
                    if (HelperMethods.storeBuildTemplate(serverUser, stack)) {
                        world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT44, SoundCategory.PLAYERS, 0.6f, 1.0f);
                        player.getItemCooldownManager().set(this, SimplySkills.generalConfig.skillChronicleCooldown);
                        user.sendMessage(Text.literal("Build Stored Successfully"));
                        success = true;
                    }
                }
                //APPLY
                else {
                    //System.out.println("Did not find skills. Trying to retrieve build.");
                    if (HelperMethods.applyBuildTemplate(serverUser, stack)) {
                        world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT43, SoundCategory.PLAYERS, 0.7f, 1.0f);
                        player.getItemCooldownManager().set(this, SimplySkills.generalConfig.skillChronicleCooldown);
                        user.sendMessage(Text.literal("Build Retrieved Successfully"));
                        success = true;
                    }
                }
                if (!success) {
                    user.sendMessage(Text.literal("You do not meet the requirements"));
                    player.getItemCooldownManager().set(this, 60);
                }
            }
            if (player instanceof ServerPlayerEntity serverPlayer)
                ModPacketHandler.sendStopSoundPacket(serverPlayer, SoundRegistry.SOUNDEFFECT6.getId());
            if (!player.getItemCooldownManager().isCoolingDown(this))
                player.getItemCooldownManager().set(this, 60);
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity.age %60 == 0) {
            if (entity instanceof ServerPlayerEntity user) {
                int unspentPoints = HelperMethods.getUnspentPoints(user);
                ModPacketHandler.sendTo(user, new UpdateUnspentPointsPacket(unspentPoints));
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound nbt = itemStack.getOrCreateNbt();

        if (nbt != null) {
            if (!nbt.getString("player_uuid").isEmpty()) {
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip10"));
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip8"));
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip14"));
                HelperMethods.printNBT(itemStack, tooltip, "category");
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip15"));
                HelperMethods.printNBT(itemStack, tooltip, "skill");
                tooltip.add(Text.literal(""));
                HelperMethods.printNBT(itemStack, tooltip, "name");
                if (SimplySkillsClient.unspentPoints > 0) {
                    tooltip.add(Text.literal(""));
                    tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip11", SimplySkillsClient.unspentPoints));
                    tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip12"));
                }
                    tooltip.add(Text.literal(""));
            } else {
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip9"));
                tooltip.add(Text.literal(""));
                tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip7"));
                if (SimplySkillsClient.unspentPoints > 0) {
                    tooltip.add(Text.literal(""));
                    tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip11", SimplySkillsClient.unspentPoints));
                    tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip13"));
                }
                tooltip.add(Text.literal(""));
            }
        }
        tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip16"));
        if (Screen.hasAltDown()) {
            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip1"));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip2"));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip3"));
            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip4"));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip5"));
            tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip6"));
        }
    }


}
