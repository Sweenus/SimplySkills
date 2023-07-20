package net.sweenus.simplyskills.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.client.SimplySkillsClient;
import net.sweenus.simplyskills.util.SignatureAbilities;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ElementalSurgeEffect extends StatusEffect {
    public ElementalSurgeEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.world.isClient()) {
            int frequency = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalSurgeFrequency;

            if (livingEntity.age % frequency == 0 && (livingEntity instanceof PlayerEntity player)) {
                List<String> list = new ArrayList<>();
                list.add("simplyskills:frost_nova");
                list.add("simplyskills:fire_nova");
                list.add("simplyskills:lightning_nova");

                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalSurgeNoFrost))
                    list.remove("simplyskills:frost_nova");
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalSurgeNoFire))
                    list.remove("simplyskills:fire_nova");
                if (SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                        "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalSurgeNoLightning))
                    list.remove("simplyskills:lightning_nova");
                if (list.isEmpty())
                    list.add("simplyskills:arcane_nova");

                Random rand = new Random();
                String randomSpell = list.get(rand.nextInt(list.size()));
                int radius = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalSurgeRadius;
                int chance = SimplySkillsClient.spellbladeConfig.signatureSpellbladeElementalSurgeChance;

                SignatureAbilities.castSpellEngineAOE(player, randomSpell, radius, chance, false);
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
