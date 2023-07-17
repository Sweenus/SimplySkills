package net.sweenus.simplyskills.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.Abilities;
import net.sweenus.simplyskills.util.AbilityEffects;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "damage")
    public void simplyskills$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            if (SkillsAPI.getUnlockedSkills(serverPlayer,
                    "simplyskills").get().contains(SkillReferencePosition.warriorHeavyArmorMastery) ||
            SkillsAPI.getUnlockedSkills((ServerPlayerEntity) player,
                    "simplyskills").get().contains(SkillReferencePosition.warriorMediumArmorMastery)) {
                Abilities.passiveWarriorArmorMastery(player);
            }

            if (SkillsAPI.getUnlockedSkills(serverPlayer,
            "simplyskills").get().contains(SkillReferencePosition.rogueSmokeBomb)) {
                Abilities.passiveRogueSmokeBomb(player);
            }

            if (SkillsAPI.getUnlockedSkills(serverPlayer,
                    "simplyskills").get().contains(SkillReferencePosition.rogueEvasionMastery)) {
                if (Abilities.passiveRogueEvasionMastery(player))
                    cir.cancel();
            }

            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.hardy)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) amount * 20));
            }


            //Effects Rampage
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_berserker").get().contains(SkillReferencePosition.berserkerSpecialisationRampage)) {
                AbilityEffects.effectBerserkerRampage(player);
            }


        }
    }

    @Inject(at = @At("HEAD"), method = "tickFallStartPos")
    public void simplyskills$tickFallStartPos(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        if (SkillsAPI.getUnlockedSkills(player, "simplyskills").get().contains(SkillReferencePosition.initiateSlowfall)
                && player.fallDistance > 3.0F && !player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20));
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            //Print attributes to log for debug purposes (don't forget to disable this before shipping, or it's gonna be real awkward.)
            Abilities.debugPrintAttributes(player);

            //Passive Rogue Stealth
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.rogueStealth)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 15));
            }

            //Passive Wayfarer Sneak
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.wayfarerSneak)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15, 2));
            }

            //Passive Area Strip
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.initiateNullification)) {
                Abilities.passiveInitiateNullification(player);
            }

            //Passive Area Cleanse
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.cleric)) {
                Abilities.passiveAreaCleanse(player);
            }

            //Passive Self Cleanse
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.cleansing)) {
                Abilities.passiveSelfCleanse(player);
            }

            //Passive Area Reveal
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.rangerReveal)) {
                Abilities.passiveRangerReveal(player);
            }

            //Passive Berserker Sword Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.berserkerSwordMastery)) {
                Abilities.passiveBerserkerSwordMastery(player);
            }
            //Passive Berserker Axe Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.berserkerAxeMastery)) {
                Abilities.passiveBerserkerAxeMastery(player);
            }
            //Passive Berserker Ignore Pain
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.berserkerIgnorePain)) {
                Abilities.passiveBerserkerIgnorePain(player);
            }
            //Passive Berserker Recklessness
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.berserkerRecklessness)) {
                Abilities.passiveBerserkerRecklessness(player);
            }
            //Passive Berserker Challenge
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.berserkerChallenge)) {
                Abilities.passiveBerserkerChallenge(player);
            }
            //Passive Bulwark Shield Mastery
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.bulwarkShieldMastery)) {
                Abilities.passiveBulwarkShieldMastery(player);
            }
            //Passive Wayfarer Slender && Initiate Frail
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.wayfarerSlender)
            || SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.initiateFrail)) {
                Abilities.passiveWayfarerSlender(player);
            }
            //Initiate Frail (weapon element)
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.initiateFrail)
            && !SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeWeaponExpert)) {
                Abilities.passiveInitiateFrail(player);
            }
            //Wizard Frost Volley Effect
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_wizard").get().
                    contains(SkillReferencePosition.wizardSpecialisationIceCometVolley)) {
                AbilityEffects.effectWizardFrostVolley(player);
            }
            //Wizard Arcane Volley Effect
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_wizard").get().
                    contains(SkillReferencePosition.wizardSpecialisationArcaneBoltVolley)) {
                AbilityEffects.effectWizardArcaneVolley(player);
            }
            //Wizard Meteoric Wrath Effect
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_wizard").get().
                    contains(SkillReferencePosition.wizardSpecialisationMeteorShowerWrath)) {
                AbilityEffects.effectWizardMeteoricWrath(player);
            }
            //Rogue Fan of Blades Effect
            if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_rogue").get().
                    contains(SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades)) {
                AbilityEffects.effectRogueFanOfBlades(player);
            }




            //Debug - reset skills & gain exp
            if (player.isSneaking() && FabricLoader.getInstance().isDevelopmentEnvironment()) {
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills");
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills_wizard");
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills_berserker");
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills_rogue");
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills_ranger");
                SkillsAPI.resetSkills((ServerPlayerEntity)player, "simplyskills_spellblade");
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_wizard", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_berserker", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_rogue", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_ranger", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_spellblade", 60000);
            }



        }
    }


    @Inject(at = @At("HEAD"), method = "attack")
    public void simplyskills$attack(Entity target,CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (target.isAttackable()) {
                if (!target.handleAttack(player)) {

                    //Passive Rogue Backstab
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.rogueBackstab)) {
                        Abilities.passiveRogueBackstab(target, player);
                    }

                    //Passive Rogue Opportunistic Mastery
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills").get().contains(SkillReferencePosition.rogueOpportunisticMastery)) {
                        Abilities.passiveRogueOpportunisticMastery(target, player);
                    }


                    //Signature Passive Elemental Surge Renewal
                    if (SkillsAPI.getUnlockedSkills( serverPlayer,
                            "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpecialisationElementalSurgeRenewal)) {
                        if (player.hasStatusEffect(EffectRegistry.ELEMENTALSURGE)) {
                            int surgeDuration = player.getStatusEffect(EffectRegistry.ELEMENTALSURGE).getDuration();
                            player.removeStatusEffect(EffectRegistry.ELEMENTALSURGE);
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, surgeDuration+3));
                        }
                    }




                    //Effect Berserking
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_berserker").get().contains(SkillReferencePosition.berserkerSpecialisationBerserking)) {
                        AbilityEffects.effectBerserkerBerserking(target, player);
                    }

                    //Effect Siphoning Strikes
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_rogue").get().contains(SkillReferencePosition.rogueSpecialisationSiphoningStrikes)) {
                        AbilityEffects.effectRogueSiphoningStrikes(target, player);
                    }

                    //Effect Siphoning Strikes
                    if (SkillsAPI.getUnlockedSkills(serverPlayer, "simplyskills_spellblade").get().contains(SkillReferencePosition.spellbladeSpellweaving)) {
                        AbilityEffects.effectSpellbladeSpellweaving(target, player);
                    }



                }
            }
        }
    }

}