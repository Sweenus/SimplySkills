package net.sweenus.simplyskills.abilities.compat;

import immersive_melodies.Items;
import immersive_melodies.item.InstrumentItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProminenceInternalAbilities {

    public static void bardAbility(PlayerEntity player) {
        if (!FabricLoader.getInstance().isModLoaded("prominent"))
            return;
        if (!HelperMethods.isUnlocked("puffish_skills:prom", SkillReferencePosition.promBardPassive, player))
            return;

        ItemStack mainStack = player.getMainHandStack();
        Item mainStackItem = mainStack.getItem();
        ItemStack offHandStack = player.getOffHandStack();
        Item offHandStackItem = offHandStack.getItem();

        ItemStack stack;
        InstrumentItem instrument;
        if (mainStackItem instanceof InstrumentItem mainInstrument) {
            stack = mainStack;
            instrument = mainInstrument;
        } else if (offHandStackItem instanceof InstrumentItem offhandItem) {
            stack = offHandStack;
            instrument = offhandItem;
        } else return;

        if (!instrument.isPlaying(stack))
            return;

        int radius = 6;
        int frequency = 30;
        int duration = frequency + 10;

        if (stack.isOf(Items.BAGPIPE.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFWAR, 0, StatusEffects.REGENERATION, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.FLUTE.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFSWIFTNESS, 0, StatusEffects.DOLPHINS_GRACE, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.DIDGERIDOO.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFPROTECTION, 0, StatusEffects.STRENGTH, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.LUTE.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFSAFETY, 0, null, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.PIANO.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFCONCENTRATION, 0, null, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.TRIANGLE.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFBLOODLUST, 0, null, 0, null, 0, null, 0);
        } else if (stack.isOf(Items.TRUMPET.get())) {
            giveAreaBuffs(player, radius, frequency, duration, EffectRegistry.MELODYOFWAR, 0, StatusEffects.REGENERATION, 0, null, 0, null, 0);
        }

        if (player.age %30 ==0)
            HelperMethods.spawnOrbitParticles((ServerWorld) player.getWorld(), player.getPos(), ParticleTypes.NOTE, 0.5, 6);
    }

    public static void giveAreaBuffs(
            PlayerEntity player,
            int radius,
            int tickFrequency,
            int buffDuration,
            @Nullable StatusEffect buffOne,
            int buffOneAmp,
            @Nullable StatusEffect buffTwo,
            int buffTwoAmp,
            @Nullable StatusEffect debuffOne,
            int debuffOneAmp,
            @Nullable StatusEffect debuffTwo,
            int debuffTwoAmp) {


        if (player.age % tickFrequency != 0 && (debuffOne == null && debuffTwo == null)) {
            return;
        }

        if (buffOne != null && player.hasStatusEffect(buffOne)) {
            StatusEffectInstance statusEffectInstance = player.getStatusEffect(buffOne);
            if (statusEffectInstance != null) {
                int duration = statusEffectInstance.getDuration();
                buffDuration += duration;
                if (buffDuration > 200) buffDuration = 200;
            }
        }

        Box box = HelperMethods.createBox(player, radius);
        List<Entity> entities = player.getWorld().getOtherEntities(null, box, e -> e instanceof LivingEntity);


        // Apply buffs or debuffs to the entities
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity le) {
                boolean isFriendly = !HelperMethods.checkFriendlyFire(le, player);

                // Apply buffs
                if (isFriendly) {
                    if (buffOne != null) {
                        le.addStatusEffect(new StatusEffectInstance(buffOne, buffDuration, buffOneAmp, false, false, true));
                    }
                    if (buffTwo != null) {
                        le.addStatusEffect(new StatusEffectInstance(buffTwo, buffDuration, buffTwoAmp, false, false, true));
                    }
                }

                // Apply debuffs if they are not null
                if (!isFriendly) {
                    if (debuffOne != null) {
                        le.addStatusEffect(new StatusEffectInstance(debuffOne, buffDuration, debuffOneAmp, false, false, true));
                        HelperMethods.spawnOrbitParticles((ServerWorld) le.getWorld(), le.getPos(), ParticleTypes.CRIT, 0.5, 5);
                        HelperMethods.spawnOrbitParticles((ServerWorld) le.getWorld(), le.getPos(), ParticleTypes.NOTE, 0.5, 4);
                    }
                    if (debuffTwo != null) {
                        le.addStatusEffect(new StatusEffectInstance(debuffTwo, buffDuration, debuffTwoAmp, false, false, true));
                        HelperMethods.spawnOrbitParticles((ServerWorld) le.getWorld(), le.getPos(), ParticleTypes.CRIT, 0.5, 6);
                        HelperMethods.spawnOrbitParticles((ServerWorld) le.getWorld(), le.getPos(), ParticleTypes.NOTE, 0.5, 5);
                    }
                }
            }
        }
    }


}
