package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.particle.Particles;
import net.sweenus.simplyskills.abilities.ClericAbilities;
import net.sweenus.simplyskills.effects.instance.SimplyStatusEffectInstance;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

public class AnointedEffect extends StatusEffect {
    public AnointedEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        super.applyUpdateEffect(livingEntity, amplifier);

        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player) {

                //Cleric Signature Anoint Weapon Cleanse
                if (HelperMethods.isUnlocked("simplyskills:cleric",
                        SkillReferencePosition.clericSpecialisationAnointWeaponCleanse, player)
                        && FabricLoader.getInstance().isModLoaded("paladins")) {
                    ClericAbilities.signatureClericAnointWeaponCleanse(player);
                }

            }
        }
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
