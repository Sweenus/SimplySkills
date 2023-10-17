package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {


    @Shadow protected float lastDamageTaken;

    @Shadow private long lastDamageTime;

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable public abstract LivingEntity getAttacker();

    @Shadow public abstract float getHealth();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    //Prevent detection when stealthed
    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    public void simplyskills$canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target.hasStatusEffect(EffectRegistry.STEALTH))
            cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "isDead", cancellable = true)
    public void simplyskills$tick(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (livingEntity.getHealth() <= 0.0F && livingEntity.hasStatusEffect(EffectRegistry.UNDYING)) {
            livingEntity.setHealth(1.0F);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    protected void simplyskills$modifyAppliedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity attacker = this.getAttacker();
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (attacker != null && lastDamageTaken > 0 && SimplySkills.generalConfig.enableDDR) {
            if (!(livingEntity instanceof PlayerEntity)) {
                World world = attacker.getWorld();
                float maxHp = getMaxHealth();
                float thresholdCheck = amount / maxHp;
                float damage = cir.getReturnValue();
                float ddrAttackSpeedWeight = (float) SimplySkills.generalConfig.DDRAttackSpeedWeight / 100;
                float ddrAmount = (float) SimplySkills.generalConfig.DDRAmount / 100;
                float ddrHealthThreshold = (float) SimplySkills.generalConfig.DDRHealthThreshold / 100;
                float damageFrequency = 0.01f + (world.getTime() - lastDamageTime) / (damage * ddrAttackSpeedWeight);
                float healthPercent = Math.min((damage / maxHp) * damage, 0.9f * damage);
                float minimumHp = (float) SimplySkills.generalConfig.DDRHealthRequirement;

                if (thresholdCheck > ddrHealthThreshold && maxHp >= minimumHp) {
                    float damageReduction = Math.min((healthPercent * ddrAmount), (damage / 2));
                    float newAmount = Math.max((damage - damageReduction), 1) * Math.max(Math.min(damageFrequency, 1.0f), 0.3f);
                    if (SimplySkills.generalConfig.enableDDRDebugLog && attacker instanceof PlayerEntity)
                        attacker.sendMessage(Text.literal("§fDamage reduced from §6" + damage + " §fto§a " + newAmount + " §fusing DR: §6" + damageReduction + "§f & SDR: §b" + damageFrequency));
                        //System.out.println("Damage reduced from " + damage + " to " + newAmount + " using DR: " + damageReduction + " & SDR: " + damageFrequency);
                    cir.setReturnValue(newAmount);
                }
            }
        }
    }
}
