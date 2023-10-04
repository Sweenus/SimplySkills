package net.sweenus.simplyskills.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.abilities.InitiateAbilities;
import net.sweenus.simplyskills.abilities.SpellbladeAbilities;
import net.sweenus.simplyskills.abilities.WizardAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
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
            SpellCast.Action action,
            float progress,
            CallbackInfo ci) {

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateEmpower, player))
            InitiateAbilities.passiveInitiateEmpower(player, spellId);

        if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.REVEALED, 180, 5));
            if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateWhisperedWizardry, player))
                HelperMethods.incrementStatusEffect(player, EffectRegistry.SPELLFORGED, 80, 2, 9);
        } else if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateSpellcloak, player)
                && !player.hasStatusEffect(EffectRegistry.REVEALED)) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 40));
        }

        if (FabricLoader.getInstance().isModLoaded("simplyswords")) {
            SimplySwordsGemEffects.spellshield(player);
            SimplySwordsGemEffects.spellStandard(player);
        }

        if (HelperMethods.isUnlocked("simplyskills:wizard", SkillReferencePosition.wizardSpellEcho, player)) {
            WizardAbilities.passiveWizardSpellEcho(player, targets);
        }

        if (HelperMethods.isUnlocked("simplyskills:spellblade", SkillReferencePosition.spellbladeWeaponExpert, player)) {
            SpellbladeAbilities.effectSpellbladeWeaponExpert(player);
        }

        if (HelperMethods.isUnlocked("simplyskills:tree", SkillReferencePosition.initiateOverload, player))
            HelperMethods.incrementStatusEffect(player, EffectRegistry.OVERLOAD, 160, 1, 9);


    }

}
