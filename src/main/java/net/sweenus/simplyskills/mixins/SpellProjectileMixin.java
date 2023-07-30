package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.render.FlyingSpellEntity;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellCast;
import net.spell_engine.internals.SpellHelper;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.util.Abilities;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SpellProjectile.class)
public abstract class SpellProjectileMixin extends ProjectileEntity {

    @Shadow public abstract Spell getSpell();

    public SpellProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "onEntityHit")
    private void simplyskills$performSpell(
            EntityHitResult entityHitResult, CallbackInfo ci) {

        if (!this.world.isClient) {
            Entity target = entityHitResult.getEntity();
            if (target != null && this.getOwner() != null) {
                Entity ownerEntity = this.getOwner();

                if (ownerEntity instanceof PlayerEntity player) {
                    if (HelperMethods.isUnlocked("simplyskills_wizard", SkillReferencePosition.wizardSpellEcho, player)) {
                        Abilities.passiveWizardSpellEcho(player, target);
                    }
                }
            }
        }
    }
}