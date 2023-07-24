package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.puffish.skillsmod.SkillsAPI;
import net.puffish.skillsmod.SkillsMod;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.Abilities;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SpellHelper.class)
public class SpellHelperMixin {

    @Inject(at = @At("TAIL"), method = "performSpell")
    private static void simplyskills$performSpell(
            World world,
            PlayerEntity player,
            Identifier spellId,
            List<Entity> targets,
            ItemStack itemStack,
            SpellCast.Action action,
            Hand hand,
            int remainingUseTicks,
            CallbackInfo ci) {

        if (HelperMethods.isUnlocked("simplyskills", SkillReferencePosition.initiateEmpower, player))
            Abilities.passiveInitiateEmpower(player, spellId);

        }
    }
