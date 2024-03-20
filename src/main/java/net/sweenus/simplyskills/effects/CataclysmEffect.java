package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.abilities.AscendancyAbilities;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class CataclysmEffect extends StatusEffect {
    public CataclysmEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity instanceof ServerPlayerEntity player && player.hasStatusEffect(EffectRegistry.CATACLYSM)) {
                StatusEffectInstance cataclysmEffect = player.getStatusEffect(EffectRegistry.CATACLYSM);
                if (cataclysmEffect == null)
                    return;
                String spellId = "simplyskills:cataclysm_comet";
                if (player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute)
                        > player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FROST).attribute))
                    spellId = "simplyskills:cataclysm_meteor";

                int initialDuration = 90; // Initial max duration of the effect
                int distanceIncrement = 5; // The number of blocks to increment the distance each time
                int frequency = 20 - Math.min(12, (AscendancyAbilities.getAscendancyPoints(player) / 10));
                int currentDistance = (initialDuration - cataclysmEffect.getDuration()) / frequency * distanceIncrement;

                if (cataclysmEffect.getDuration() % frequency == 0) {
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SPELL_ENERGY,
                            SoundCategory.PLAYERS, 0.5f, 1.1f);

                    Vec3d lookVector = player.getRotationVec(1.0F);
                    Vec3d spellPosition = player.getPos().add(lookVector.multiply(currentDistance, 0, currentDistance));
                    BlockPos castPosition = new BlockPos((int)spellPosition.x, (int)spellPosition.y, (int)spellPosition.z);
                    SignatureAbilities.castSpellEngineIndirectTarget(player,
                            spellId,
                            45, null, castPosition);
                    if (AscendancyAbilities.getAscendancyPoints(player) > 29)
                        HelperMethods.incrementStatusEffect(player, EffectRegistry.SPELLFORGED, 60, 1, 10);
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (entity instanceof PlayerEntity player && FabricLoader.getInstance().isModLoaded("simplyswords"))
            SimplySwordsGemEffects.warStandard(player);

        super.onRemoved(entity, attributes, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
