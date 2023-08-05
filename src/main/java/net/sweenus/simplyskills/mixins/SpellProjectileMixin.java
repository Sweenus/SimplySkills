package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.spell_engine.entity.SpellProjectile;
import net.sweenus.simplyskills.abilities.WizardAbilities;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpellProjectile.class)
public abstract class SpellProjectileMixin extends ProjectileEntity {

    public SpellProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "onEntityHit")
    private void simplyskills$performSpell(
            EntityHitResult entityHitResult, CallbackInfo ci) {

        if (!this.getWorld().isClient) {
            Entity target = entityHitResult.getEntity();
            if (target != null && this.getOwner() != null) {
                Entity ownerEntity = this.getOwner();

                if (ownerEntity instanceof PlayerEntity player) {
                    if (HelperMethods.isUnlocked("simplyskills:wizard", SkillReferencePosition.wizardSpellEcho, player)) {
                        WizardAbilities.passiveWizardSpellEcho(player, target);
                    }
                }
            }
        }
    }
}