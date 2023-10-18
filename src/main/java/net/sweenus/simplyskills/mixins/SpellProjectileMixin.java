package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.abilities.AbilityLogic;
import net.sweenus.simplyskills.abilities.ClericAbilities;
import net.sweenus.simplyskills.abilities.RangerAbilities;
import net.sweenus.simplyskills.abilities.WizardAbilities;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(SpellProjectile.class)
public abstract class SpellProjectileMixin extends ProjectileEntity {

    @Shadow public abstract Spell getSpell();

    @Shadow private Spell.ProjectileData.Perks perks;

    @Shadow public abstract Spell.ProjectileData.Perks mutablePerks();

    @Shadow public float range;

    @Shadow protected Set<Integer> impactHistory;

    @Shadow public abstract SpellProjectile.Behaviour behaviour();

    @Shadow private Identifier spellId;

    @Shadow private SpellHelper.ImpactContext context;

    @Shadow public abstract Entity getFollowedTarget();

    @Shadow public abstract void setFollowedTarget(Entity target);

    @Shadow private Entity followedTarget;

    @Shadow protected abstract void followTarget();

    @Shadow public abstract void setVelocity(double x, double y, double z, float speed, float spread, float divergence);

    public SpellProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void simplyskills$tick(CallbackInfo ci) {

        if (!this.getWorld().isClient) {
            if ( this.getSpell() != null && this.getOwner() instanceof ServerPlayerEntity player) {
                SpellProjectile spellProjectile = (SpellProjectile) (Object)this;

                // Ranger Elemental Artillery
                RangerAbilities.signatureRangerElementalArtillery(player, spellProjectile, this.spellId, this.context, this.perks);

                //Wizard Lightning Ball
                WizardAbilities.signatureWizardStaticDischargeBall(player, spellProjectile, this.spellId, this.context, this.perks);
                //Wizard Lightning Orb
                WizardAbilities.signatureWizardLightningOrb(spellProjectile, this.followedTarget, this.spellId);
                //Cleric Sacred Orb
                ClericAbilities.signatureClericSacredOrbHoming(spellProjectile, this.spellId);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "onBlockHit", cancellable = true)
    protected void simplyskills$onBlockHit(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            if (this.spellId != null && this.getSpell() != null) {
                String[] spellList =  new String[] {
                        "simplyskills:lightning_ball_homing",
                        "simplyskills:physical_dagger_homing",
                        "simplyskills:sacred_orb_lesser"};
                if (HelperMethods.stringContainsAny(this.spellId.toString(), spellList))
                    ci.cancel();
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "onEntityHit", cancellable = true)
    protected void simplyskills$onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            if (this.spellId != null && this.getSpell() != null) {
                try {
                    SpellProjectile spellProjectile = (SpellProjectile) (Object) this;
                    ClericAbilities.signatureClericSacredOrbImpact(entityHitResult, spellId, getOwner(), spellProjectile);

                    String[] spellList = new String[]{"simplyskills:lightning_ball_homing", "simplyskills:physical_dagger_homing"};
                    if (HelperMethods.stringContainsAny(this.spellId.toString(), spellList) && this.getOwner() instanceof ServerPlayerEntity player) {

                        SpellHelper.projectileImpact(player, this, entityHitResult.getEntity(), this.getSpell(), context.position(entityHitResult.getPos()));

                        if (HelperMethods.isUnlocked("simplyskills:wizard",
                                SkillReferencePosition.wizardSpecialisationStaticDischargeLightningOrbOnHit, player)) {
                            List<Entity> targets = new ArrayList<Entity>();
                            if (entityHitResult.getEntity() != null) {
                                targets.add(entityHitResult.getEntity());
                                AbilityLogic.onSpellCastEffects(player, targets, this.spellId);
                            }
                        }

                        ci.cancel();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}