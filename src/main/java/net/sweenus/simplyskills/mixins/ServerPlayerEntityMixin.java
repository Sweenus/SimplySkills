package net.sweenus.simplyskills.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.puffish.skillsmod.SkillsAPI;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.Abilities;
import net.sweenus.simplyskills.util.AbilityEffects;
import net.sweenus.simplyskills.util.HelperMethods;
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

            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorHeavyArmorMastery, serverPlayer)
                    || HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorMediumArmorMastery, serverPlayer)) {
                Abilities.passiveWarriorArmorMastery(player);
            }

            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorSpellbreaker, player)) {
                Abilities.passiveWarriorSpellbreaker(player);
            }

            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rogueSmokeBomb, serverPlayer)) {
                Abilities.passiveRogueSmokeBomb(player);
            }

            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rogueEvasionMastery, serverPlayer)) {
                Abilities.passiveRogueEvasionMastery(player);
            }

            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.hardy, serverPlayer)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (int) amount * 20));
            }

            //Passive Initiate Hasty
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateHasty, player)) {
                Abilities.passiveInitiateHasty(player);
            }


            //Effect Rampage
            if (HelperMethods.isUnlocked("simplyskills_berserker",
                    SkillReferencePosition.berserkerSpecialisationRampage, serverPlayer)) {
                AbilityEffects.effectBerserkerRampage(player);
            }

            //Effect Stealth
            if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                Abilities.passiveWayfarerBreakStealth(null, player, true, false);
            }


        }
    }

    @Inject(at = @At("HEAD"), method = "tickFallStartPos")
    public void simplyskills$tickFallStartPos(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        float slowfallActivateDistance = SimplySkills.initiateConfig.passiveInitiateSlowFallDistanceToActivate;
        float goliathActivateDistance = SimplySkills.warriorConfig.passiveWarriorGoliathFallDistance;

        if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.initiateSlowfall, player)
                && player.fallDistance > slowfallActivateDistance && !player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20));
        }

        if (HelperMethods.isUnlocked("simplyskills",
                SkillReferencePosition.warriorGoliath, player)
                && player.fallDistance > goliathActivateDistance && !player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            Abilities.passiveWarriorGoliath(player);
        }

    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            //Passive Rogue Stealth
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.wayfarerStealth, player)
                    && player.isSneaking() && player.age % 10 == 0) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 30));
            }

            //Passive Warrior Death Defy
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.warriorDeathDefy, player)) {
                Abilities.passiveWarriorDeathDefy(player);
            }

            //Passive Wayfarer Sneak
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.wayfarerSneak, player)
                    && player.isSneaking() && player.age % 10 == 0) {
                int sneakSpeedAmplifier = SimplySkills.wayfarerConfig.passiveWayfarerSneakSpeedAmplifier;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15, sneakSpeedAmplifier));
            }

            //Passive Area Strip
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateNullification, player)) {
                Abilities.passiveInitiateNullification(player);
            }
            //Passive Initiate Lightning Rod
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateLightningRod, player)) {
                Abilities.passiveInitiateLightningRod(player);
            }

            //Passive Area Cleanse
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.cleric, player)) {
                Abilities.passiveAreaCleanse(player);
            }

            //Passive Self Cleanse
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.cleansing, player)) {
                Abilities.passiveSelfCleanse(player);
            }

            //Passive Ranger Reveal
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rangerReveal, player)) {
                Abilities.passiveRangerReveal(player);
            }
            //Passive Ranger Tamer
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rangerTamer, player)) {
                Abilities.passiveRangerBonded(player);
            }
            //Passive Ranger Bonded
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rangerBonded, player)) {
                Abilities.passiveRangerTamer(player);
            }
            //Passive Ranger Trained
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rangerTrained, player)) {
                Abilities.passiveRangerTrained(player);
            }
            //Passive Ranger Incognito
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rangerIncognito, player)) {
                Abilities.passiveRangerIncognito(player);
            }

            //Passive Berserker Sword Mastery
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.berserkerSwordMastery, player)) {
                Abilities.passiveBerserkerSwordMastery(player);
            }
            //Passive Berserker Axe Mastery
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.berserkerAxeMastery, player)) {
                Abilities.passiveBerserkerAxeMastery(player);
            }
            //Passive Berserker Ignore Pain
            if (HelperMethods.isUnlocked( "simplyskills",
                    SkillReferencePosition.berserkerIgnorePain, player)) {
                Abilities.passiveBerserkerIgnorePain(player);
            }
            //Passive Berserker Recklessness
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.berserkerRecklessness, player)) {
                Abilities.passiveBerserkerRecklessness(player);
            }
            //Passive Berserker Challenge
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.berserkerChallenge, player)) {
                Abilities.passiveBerserkerChallenge(player);
            }
            //Passive Bulwark Shield Mastery
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.bulwarkShieldMastery, player)) {
                Abilities.passiveBulwarkShieldMastery(player);
            }
            //Passive Wayfarer Slender && Initiate Frail
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.wayfarerSlender, player)
            || HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.initiateFrail, player)) {
                Abilities.passiveWayfarerSlender(player);
            }
            //Passive Backstab Stealth
            if (HelperMethods.isUnlocked("simplyskills",
                    SkillReferencePosition.rogueBackstab, player)) {
                Abilities.passiveRogueBackstabStealth(player);
            }
            //Wizard Frost Volley Effect
            if (HelperMethods.isUnlocked("simplyskills_wizard",
                    SkillReferencePosition.wizardSpecialisationIceCometVolley, player)) {
                AbilityEffects.effectWizardFrostVolley(player);
            }
            //Wizard Arcane Volley Effect
            if (HelperMethods.isUnlocked("simplyskills_wizard",
            SkillReferencePosition.wizardSpecialisationArcaneBoltVolley, player)) {
                AbilityEffects.effectWizardArcaneVolley(player);
            }
            //Wizard Meteoric Wrath Effect
            if (HelperMethods.isUnlocked("simplyskills_wizard",
                    SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player)) {
                AbilityEffects.effectWizardMeteoricWrath(player);
            }
            //Rogue Fan of Blades Effect
            if (HelperMethods.isUnlocked("simplyskills_rogue",
                    SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades, player)) {
                AbilityEffects.effectRogueFanOfBlades(player);
            }




            //Debug - reset skills & gain exp
            if (player.isSneaking() && FabricLoader.getInstance().isDevelopmentEnvironment()) {
                Abilities.debugPrintAttributes(player);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_wizard", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_berserker", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_rogue", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_ranger", 60000);
                SkillsAPI.addExperience((ServerPlayerEntity) player, "simplyskills_spellblade", 60000);
            }



        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void simplyskills$onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        HelperMethods.treeResetOnDeath(player);
    }


    @Inject(at = @At("HEAD"), method = "attack")
    public void simplyskills$attack(Entity target,CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (target.isAttackable()) {
                if (!target.handleAttack(player)) {

                    //Passive Rogue Backstab
                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.rogueBackstab, player)) {
                        Abilities.passiveRogueBackstab(target, player);
                    }

                    //Passive Rogue Opportunistic Mastery
                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.rogueOpportunisticMastery, player)) {
                        Abilities.passiveRogueOpportunisticMastery(target, player);
                    }

                    //Effect Warrior Frenzy
                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.warriorFrenzy, player)) {
                        Abilities.passiveWarriorFrenzy(player);
                    }

                    //Initiate Frail (weapon element)
                    if (HelperMethods.isUnlocked("simplyskills",
                            SkillReferencePosition.initiateFrail, player)
                            && !HelperMethods.isUnlocked("simplyskills_spellblade",
                            SkillReferencePosition.spellbladeWeaponExpert, player)) {
                        Abilities.passiveInitiateFrail(player);
                    }

                    //Signature Passive Elemental Surge Renewal
                    if (HelperMethods.isUnlocked("simplyskills_spellblade",
                            SkillReferencePosition.spellbladeSpecialisationElementalSurgeRenewal, player)) {
                        if (player.hasStatusEffect(EffectRegistry.ELEMENTALSURGE)) {
                            int surgeDuration = player.getStatusEffect(EffectRegistry.ELEMENTALSURGE).getDuration();
                            player.removeStatusEffect(EffectRegistry.ELEMENTALSURGE);
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, surgeDuration+3));
                        }
                    }


                    //Effect Bloodthirsty Tremor
                    if (HelperMethods.isUnlocked("simplyskills_berserker",
                            SkillReferencePosition.berserkerSpecialisationBloodthirstyTremor, player)) {
                        AbilityEffects.effectBerserkerBloodthirstyTremor(player);
                    }

                    //Effect Bloodthirsty Tireless
                    if (HelperMethods.isUnlocked("simplyskills_berserker",
                            SkillReferencePosition.berserkerSpecialisationBloodthirstyTireless, player)) {
                        AbilityEffects.effectBerserkerBloodthirstyTireless(player);
                    }

                    //Effect Berserking
                    if (HelperMethods.isUnlocked("simplyskills_berserker",
                            SkillReferencePosition.berserkerSpecialisationBerserking, player)) {
                        AbilityEffects.effectBerserkerBerserking(target, player);
                    }

                    //Effect Siphoning Strikes
                    if (HelperMethods.isUnlocked("simplyskills_rogue",
                            SkillReferencePosition.rogueSpecialisationSiphoningStrikes, player)) {
                        AbilityEffects.effectRogueSiphoningStrikes(target, player);
                    }

                    //Effect Rogue Vanish
                    if (HelperMethods.isUnlocked("simplyskills_rogue",
                            SkillReferencePosition.rogueSpecialisationSiphoningStrikesVanish, player)) {
                        AbilityEffects.effectRogueSiphoningStrikesVanish(player);
                    }

                    //Effect Spell Weaving
                    if (HelperMethods.isUnlocked("simplyskills_spellblade",
                            SkillReferencePosition.spellbladeSpellweaving, player)) {
                        AbilityEffects.effectSpellbladeSpellweaving(target, player);
                    }

                    //Effect Stealth
                    if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                        Abilities.passiveWayfarerBreakStealth(target, player, false, true);
                    }



                }
            }
        }
    }

}