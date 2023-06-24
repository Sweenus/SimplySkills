package net.sweenus.simplyskills.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.sweenus.simplyskills.util.SignatureAbilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElementalImpactEffect extends StatusEffect {
    public ElementalImpactEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {
                player.setVelocity(livingEntity.getRotationVector().multiply(+2));
                player.setVelocity(livingEntity.getVelocity().x, 0, livingEntity.getVelocity().z);
                player.velocityModified = true;
                List<String> list = new ArrayList<>();
                list.add("simplyskills:frost_arrow_rain");
                list.add("simplyskills:fire_arrow_rain");
                list.add("simplyskills:lightning_arrow_rain");
                Random rand = new Random();
                String randomSpell = list.get(rand.nextInt(list.size()));
                int radius = 3;
                int chance = 100;

                SignatureAbilities.castSpellEngineAOE(player, randomSpell, radius, chance, true);
            }

        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
