package net.sweenus.simplyskills.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

import java.util.List;

public class MalevolentManuscript extends Item {
    public MalevolentManuscript(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if ((user instanceof ServerPlayerEntity serverUser)) {
            if (HelperMethods.respecialise(serverUser)) {
                user.swingHand(hand);
                world.playSound(null, user.getBlockPos(), SoundRegistry.SOUNDEFFECT12, SoundCategory.PLAYERS, 0.5f, 1.0f);
                user.getStackInHand(hand).decrement(1);
            }
        }
        return super.use(world,user,hand);
    }


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip1"));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip2"));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip3"));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip4"));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip5"));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip6"));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyskills.malevolent_manuscript.tooltip7").formatted(Formatting.RED).formatted(Formatting.UNDERLINE));
    }


}
