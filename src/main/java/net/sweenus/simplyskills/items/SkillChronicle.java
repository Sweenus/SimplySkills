package net.sweenus.simplyskills.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Skill;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyswords.util.AbilityMethods;

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

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 80;
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
                        world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT12, SoundCategory.PLAYERS, 0.5f, 1.0f);
                        player.getItemCooldownManager().set(this, 100);
                    }
                }
                //APPLY
                else {
                    //System.out.println("Did not find skills. Trying to retrieve build.");
                    if (HelperMethods.applyBuildTemplate(serverUser, stack)) {
                        world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT11, SoundCategory.PLAYERS, 0.5f, 1.0f);
                        player.getItemCooldownManager().set(this, 100);
                    }
                }
            }
        }
    }


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {






        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("§6Specialisations"));
        HelperMethods.printNBT(itemStack, tooltip, "category");
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("§bTotal Skill Points"));
        HelperMethods.printNBT(itemStack, tooltip, "skill");
        tooltip.add(Text.literal(""));
        HelperMethods.printNBT(itemStack, tooltip, "name");



        tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip1"));
        tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip2"));
        tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip3"));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyskills.skill_chronicle.tooltip4").formatted(Formatting.RED).formatted(Formatting.UNDERLINE));
    }


}
