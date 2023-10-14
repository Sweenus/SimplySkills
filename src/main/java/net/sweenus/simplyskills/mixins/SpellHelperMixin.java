package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCast;
import net.sweenus.simplyskills.abilities.AbilityLogic;
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
            SpellCast.Action action,
            float progress,
            CallbackInfo ci) {

        AbilityLogic.onSpellCastEffects(player, targets, spellId);

    }

}
