package net.sweenus.simplyskills.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.*;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void simplyskills$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity serverPlayer) {

            //Effect Barrier
            if (player.hasStatusEffect(EffectRegistry.BARRIER)) {
                HelperMethods.decrementStatusEffect(player, EffectRegistry.BARRIER);
                cir.setReturnValue(false);
                player.getWorld().playSoundFromEntity(null, player, SoundRegistry.FX_SKILL_BACKSTAB,
                        SoundCategory.PLAYERS, 1, 1);
            }

            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorHeavyArmorMastery, serverPlayer)
                    || HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorMediumArmorMastery, serverPlayer)) {
                WarriorAbilities.passiveWarriorArmorMastery(player);
            }

            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorSpellbreaker, player)) {
                WarriorAbilities.passiveWarriorSpellbreaker(player);
            }

            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueSmokeBomb, serverPlayer)) {
                RogueAbilities.passiveRogueSmokeBomb(player);
            }

            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueEvasionMastery, serverPlayer)) {
                if (!RogueAbilities.passiveRogueEvasionMastery(player))
                    cir.setReturnValue(false);
            }

            //Passive Initiate Hasty
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.initiateHasty, player)) {
                InitiateAbilities.passiveInitiateHasty(player);
            }


            //Effect Rampage
            if (HelperMethods.isUnlocked("simplyskills:berserker",
                    SkillReferencePosition.berserkerSpecialisationRampage, serverPlayer)) {
                AbilityEffects.effectBerserkerRampage(player);
            }

            //Effect Stealth
            if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                WayfarerAbilities.passiveWayfarerBreakStealth(null, player, true, false);
            }

            //Passive Rage
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.berserkerPath, serverPlayer)) {
                HelperMethods.incrementStatusEffect(player, EffectRegistry.RAGE, 300, 1, 99);
            }

            //Cleric Signature Anoint Weapon Undying
            if (HelperMethods.isUnlocked("simplyskills:cleric",
                    SkillReferencePosition.clericSpecialisationAnointWeaponUndying, player)
                    && FabricLoader.getInstance().isModLoaded("paladins")) {
                ClericAbilities.signatureClericAnointWeaponUndying(player);
            }


        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float simplyskills$damageResult(float amount){
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.hasStatusEffect(EffectRegistry.RAGE)) {
            float damageModifier = (float) 1 + ((float) player.getStatusEffect(EffectRegistry.RAGE).getAmplifier() / 200);
            return amount * damageModifier;
        }
        return amount;
    }

    @Inject(at = @At("HEAD"), method = "tickFallStartPos")
    public void simplyskills$tickFallStartPos(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        float slowfallActivateDistance = SimplySkills.initiateConfig.passiveInitiateSlowFallDistanceToActivate;
        float goliathActivateDistance = SimplySkills.warriorConfig.passiveWarriorGoliathFallDistance;

        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.initiateSlowfall, player)
                && player.fallDistance > slowfallActivateDistance && !player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20, 0, false, false, true));
        }

        if (HelperMethods.isUnlocked("simplyskills:tree",
                SkillReferencePosition.warriorGoliath, player)
                && player.fallDistance > goliathActivateDistance && !player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
            WarriorAbilities.passiveWarriorGoliath(player);
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorBound, player))
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 80, 2, false, false, true));
        }

    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player instanceof ServerPlayerEntity) {

            if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                player.setInvisible(player.hasStatusEffect(EffectRegistry.STEALTH));
            }

            //Passive Wayfarer Stealth
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.wayfarerStealth, player)
                    && player.isSneaking() && player.age % 10 == 0
                    && !player.hasStatusEffect(EffectRegistry.REVEALED)) {
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STEALTH, 30, 0, false, false, true));
            }

            //Passive Warrior Death Defy
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorDeathDefy, player)) {
                WarriorAbilities.passiveWarriorDeathDefy(player);
            }

            //Passive Warrior Carnage
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.warriorCarnage, player)) {
                WarriorAbilities.passiveWarriorCarnage(player);
            }

            //Passive Wayfarer Sneak
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.wayfarerSneak, player)
                    && player.isSneaking() && player.age % 10 == 0) {
                int sneakSpeedAmplifier = SimplySkills.wayfarerConfig.passiveWayfarerSneakSpeedAmplifier;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15, sneakSpeedAmplifier, false, false, true));
                if (player.hasStatusEffect(EffectRegistry.STEALTH))
                    HelperMethods.incrementStatusEffect(player, EffectRegistry.MIGHT, 15, 1, 22);
            }
            //Passive Wayfarer Guarding
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.wayfarerGuarding, player)) {
                WayfarerAbilities.passiveWayfarerGuarding(player);
            }
            //Passive Area Strip
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.initiateNullification, player)) {
                InitiateAbilities.passiveInitiateNullification(player);
            }
            //Passive Initiate Lightning Rod
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.initiateLightningRod, player)) {
                InitiateAbilities.passiveInitiateLightningRod(player);
            }

            //Passive Ranger Reveal
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerReveal, player)) {
                RangerAbilities.passiveRangerReveal(player);
            }
            //Passive Ranger Bonded
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerBonded, player)) {
                RangerAbilities.passiveRangerBonded(player);
            }
            //Passive Ranger Tamer
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerTamer, player)) {
                RangerAbilities.passiveRangerTamer(player);
            }
            //Passive Ranger Trained
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerTrained, player)) {
                RangerAbilities.passiveRangerTrained(player);
            }
            //Passive Ranger Incognito
            if (HelperMethods.isUnlocked("simplyskills:ranger",
                    SkillReferencePosition.rangerIncognito, player)) {
                RangerAbilities.passiveRangerIncognito(player);
            }

            //Passive Berserker Sword Mastery
            if (HelperMethods.isUnlocked("simplyskills:berserker",
                    SkillReferencePosition.berserkerSwordMastery, player)) {
                BerserkerAbilities.passiveBerserkerSwordMastery(player);
            }
            //Passive Berserker Axe Mastery
            if (HelperMethods.isUnlocked("simplyskills:berserker",
                    SkillReferencePosition.berserkerAxeMastery, player)) {
                BerserkerAbilities.passiveBerserkerAxeMastery(player);
            }
            //Passive Berserker Ignore Pain
            if (HelperMethods.isUnlocked( "simplyskills:berserker",
                    SkillReferencePosition.berserkerIgnorePain, player)) {
                BerserkerAbilities.passiveBerserkerIgnorePain(player);
            }
            //Passive Berserker Recklessness
            if (HelperMethods.isUnlocked("simplyskills:berserker",
                    SkillReferencePosition.berserkerPath, player)) {
                BerserkerAbilities.passiveBerserkerRecklessness(player);
            }
            //Passive Berserker Challenge
            if (HelperMethods.isUnlocked("simplyskills:berserker",
                    SkillReferencePosition.berserkerChallenge, player)) {
                BerserkerAbilities.passiveBerserkerChallenge(player);
            }
            //Passive Bulwark Shield Mastery
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.bulwarkShieldMastery, player)) {
                WarriorAbilities.passiveWarriorShieldMastery(player);
            }
            //Passive Wayfarer Slender && Initiate Frail
            if (HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.roguePath, player)
                    || HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.rangerPath, player)
                    || HelperMethods.isUnlocked("simplyskills:tree",
                    SkillReferencePosition.wizardPath, player)) {
                WayfarerAbilities.passiveWayfarerSlender(player);
            }
            //Passive Backstab Stealth
            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueBackstab, player)) {
                RogueAbilities.passiveRogueBackstabStealth(player);
            }
            //Wizard Frost Volley Effect
            if (HelperMethods.isUnlocked("simplyskills:wizard",
                    SkillReferencePosition.wizardSpecialisationIceCometVolley, player)) {
                AbilityEffects.effectWizardFrostVolley(player);
            }
            //Wizard Arcane Volley Effect
            if (HelperMethods.isUnlocked("simplyskills:wizard",
            SkillReferencePosition.wizardSpecialisationArcaneBoltVolley, player)) {
                AbilityEffects.effectWizardArcaneVolley(player);
            }
            //Wizard Meteoric Wrath Effect
            if (HelperMethods.isUnlocked("simplyskills:wizard",
                    SkillReferencePosition.wizardSpecialisationMeteorShowerWrath, player)) {
                AbilityEffects.effectWizardMeteoricWrath(player);
            }
            //Rogue Fan of Blades Effect
            if (HelperMethods.isUnlocked("simplyskills:rogue",
                    SkillReferencePosition.rogueSpecialisationEvasionFanOfBlades, player)) {
                AbilityEffects.effectRogueFanOfBlades(player);
            }
            //Crusader Aegis
            if (HelperMethods.isUnlocked("simplyskills:crusader",
                    SkillReferencePosition.crusaderAegis, player)
                    && FabricLoader.getInstance().isModLoaded("paladins")) {
                CrusaderAbilities.passiveCrusaderAegis(player);
            }
            //Crusader Divine Adjudication
            if (FabricLoader.getInstance().isModLoaded("paladins")) {
                CrusaderAbilities.effectDivineAdjudication(player);
            }
            //Wizard Lightning Orb (Buff)
            if (HelperMethods.isUnlocked("simplyskills:wizard",
                    SkillReferencePosition.wizardSpecialisationStaticDischargeLightningOrb, player)) {
                WizardAbilities.signatureWizardLightningOrbBuff(player);
            }
            //Cleric Altruism
            if (HelperMethods.isUnlocked("simplyskills:cleric",
                    SkillReferencePosition.clericAltruism, player)
                    && FabricLoader.getInstance().isModLoaded("paladins")) {
                ClericAbilities.passiveClericAltruism(player);
            }




            // Tick Gem effects
            if (FabricLoader.getInstance().isModLoaded("simplyswords")) {
                SimplySwordsGemEffects.spellforged(player);
                SimplySwordsGemEffects.soulshock(player);
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
                    if (HelperMethods.isUnlocked("simplyskills:rogue",
                            SkillReferencePosition.rogueBackstab, player)) {
                        RogueAbilities.passiveRogueBackstab(target, player);
                    }

                    //Passive Rogue Opportunistic Mastery
                    if (HelperMethods.isUnlocked("simplyskills:rogue",
                            SkillReferencePosition.rogueOpportunisticMastery, player)) {
                        RogueAbilities.passiveRogueOpportunisticMastery(target, player);
                    }

                    //Effect Warrior Frenzy
                    if (HelperMethods.isUnlocked("simplyskills:tree",
                            SkillReferencePosition.warriorFrenzy, player)) {
                        WarriorAbilities.passiveWarriorFrenzy(player);
                    }

                    //Initiate Frail (weapon element)
                    if (HelperMethods.isUnlocked("simplyskills:tree",
                            SkillReferencePosition.initiateFrail, player)
                            && !HelperMethods.isUnlocked("simplyskills:spellblade",
                            SkillReferencePosition.spellbladeWeaponExpert, player)) {
                        InitiateAbilities.passiveInitiateFrail(player);
                    }

                    //Signature Passive Elemental Surge Renewal
                    if (HelperMethods.isUnlocked("simplyskills:spellblade",
                            SkillReferencePosition.spellbladeSpecialisationElementalSurgeRenewal, player)) {
                        if (player.hasStatusEffect(EffectRegistry.ELEMENTALSURGE)) {
                            int surgeDuration = player.getStatusEffect(EffectRegistry.ELEMENTALSURGE).getDuration();
                            player.removeStatusEffect(EffectRegistry.ELEMENTALSURGE);
                            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.ELEMENTALSURGE, surgeDuration+3, 0, false, false, true));
                        }
                    }

                    //Effect Bloodthirsty Tremor
                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerSpecialisationBloodthirstyTremor, player)) {
                        AbilityEffects.effectBerserkerBloodthirstyTremor(player);
                    }

                    //Effect Bloodthirsty Tireless
                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerSpecialisationBloodthirstyTireless, player)) {
                        AbilityEffects.effectBerserkerBloodthirstyTireless(player);
                    }

                    //Effect Berserking
                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerSpecialisationBerserking, player)) {
                        AbilityEffects.effectBerserkerBerserking(target, player);
                    }

                    //Effect Siphoning Strikes
                    if (HelperMethods.isUnlocked("simplyskills:rogue",
                            SkillReferencePosition.rogueSpecialisationSiphoningStrikes, player)) {
                        AbilityEffects.effectRogueSiphoningStrikes(target, player);
                    }

                    //Effect Rogue Vanish
                    if (HelperMethods.isUnlocked("simplyskills:rogue",
                            SkillReferencePosition.rogueSpecialisationSiphoningStrikesVanish, player)) {
                        AbilityEffects.effectRogueSiphoningStrikesVanish(player);
                    }

                    //Effect Spell Weaving
                    if (HelperMethods.isUnlocked("simplyskills:spellblade",
                            SkillReferencePosition.spellbladeSpellweaving, player)) {
                        AbilityEffects.effectSpellbladeSpellweaving(target, player);
                    }

                    //Effect Stealth
                    if (player.hasStatusEffect(EffectRegistry.STEALTH)) {
                        WayfarerAbilities.passiveWayfarerBreakStealth(target, player, false, true);
                    }

                    //Passive Rage
                    if (HelperMethods.isUnlocked("simplyskills:tree",
                            SkillReferencePosition.berserkerPath, serverPlayer)) {
                        HelperMethods.incrementStatusEffect(player, EffectRegistry.RAGE, 300, 1, 99);
                    }

                    //Passive Berserker Exploit
                    if (HelperMethods.isUnlocked("simplyskills:berserker",
                            SkillReferencePosition.berserkerExploit, serverPlayer)) {
                        BerserkerAbilities.passiveBerserkerExploit(target);
                    }

                    //Passive Warrior Twinstrike
                    if (HelperMethods.isUnlocked("simplyskills:tree",
                            SkillReferencePosition.warriorTwinstrike, serverPlayer)
                            && target instanceof LivingEntity livingTarget) {
                        WarriorAbilities.passiveWarriorTwinstrike(player, livingTarget);
                    }

                    //Passive Warrior Swordfall
                    if (HelperMethods.isUnlocked("simplyskills:tree",
                            SkillReferencePosition.warriorSwordfall, serverPlayer)
                            && target instanceof LivingEntity livingTarget) {
                        WarriorAbilities.passiveWarriorSwordfall(player, livingTarget);
                    }

                    //Signature Cleric Anoint Weapon
                    if (player.hasStatusEffect(EffectRegistry.ANOINTED)
                            && FabricLoader.getInstance().isModLoaded("paladins")) {
                        ClericAbilities.signatureClericAnointWeaponEffect(player);
                    }


                }
            }
        }
    }

}