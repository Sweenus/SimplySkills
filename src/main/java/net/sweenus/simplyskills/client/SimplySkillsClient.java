package net.sweenus.simplyskills.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.sweenus.simplyskills.abilities.SignatureAbilities;
import net.sweenus.simplyskills.client.effects.*;
import net.sweenus.simplyskills.client.events.ClientEvents;
import net.sweenus.simplyskills.client.gui.CustomHud;
import net.sweenus.simplyskills.network.CooldownPacket;
import net.sweenus.simplyskills.network.ModPacketHandler;
import net.sweenus.simplyskills.registry.EffectRegistry;
import net.sweenus.simplyskills.registry.SoundRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SimplySkillsClient implements ClientModInitializer {

    public static int abilityCooldown = 500;
    public static long lastUseTime;
    public static int unspentPoints = 0;
    public static KeyBinding bindingAbility1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.category.simplyskills"));

    @Override
    public void onInitializeClient() {

        CustomModels.registerModelIds(List.of(
                BladestormRenderer.modelId_base,
                BladestormRenderer.modelId_overlay,
                ArcaneVolleyRenderer.modelId_base,
                ArcaneVolleyRenderer.modelId_overlay,
                FrostVolleyRenderer.modelId_base,
                FrostVolleyRenderer.modelId_overlay,
                VitalityBondRenderer.modelId_base,
                VitalityBondRenderer.modelId_overlay,
                UndyingRenderer.modelId_base,
                UndyingRenderer.modelId_overlay,
                BarrierRenderer.modelId_base,
                ImmobilizeRenderer.modelId_base,
                DeathMarkRenderer.modelId_overlay,
                TauntedRenderer.modelId_overlay
        ));


        CustomModelStatusEffect.register(EffectRegistry.BLADESTORM, new BladestormRenderer());
        CustomModelStatusEffect.register(EffectRegistry.ARCANEVOLLEY, new ArcaneVolleyRenderer());
        CustomModelStatusEffect.register(EffectRegistry.FROSTVOLLEY, new FrostVolleyRenderer());
        CustomModelStatusEffect.register(EffectRegistry.VITALITYBOND, new VitalityBondRenderer());
        CustomModelStatusEffect.register(EffectRegistry.UNDYING, new UndyingRenderer());
        CustomParticleStatusEffect.register(EffectRegistry.UNDYING, new UndyingParticles(2));
        CustomModelStatusEffect.register(EffectRegistry.BARRIER, new BarrierRenderer());
        CustomParticleStatusEffect.register(EffectRegistry.BARRIER, new BarrierParticles(1));
        CustomParticleStatusEffect.register(EffectRegistry.RAGE, new RageParticles(1));
        CustomParticleStatusEffect.register(EffectRegistry.EVASION, new EvasionParticles(1));
        CustomModelStatusEffect.register(EffectRegistry.IMMOBILIZE, new ImmobilizeRenderer());
        CustomModelStatusEffect.register(EffectRegistry.DEATHMARK, new DeathMarkRenderer());
        CustomModelStatusEffect.register(EffectRegistry.TAUNTED, new TauntedRenderer());

        CooldownPacket.init();
        ModPacketHandler.registerClient();
        ClientEvents.registerClientEvents();

        //Keybindings
        //KeyBinding bindingAbility2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.simplyskills"));
        //KeyBinding bindingAbility3 = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding("key.simplyskills.ability3", GLFW.GLFW_KEY_V, "key.category.simplyskills", () -> true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (bindingAbility1.wasPressed()) {
                if (System.currentTimeMillis() > (lastUseTime + abilityCooldown)) {

                    SignatureAbilities.sendKeybindPacket();
                    client.player.getWorld().playSoundFromEntity(null, client.player, SoundRegistry.FX_UI_UNLOCK3,
                            SoundCategory.PLAYERS, 1, 1);

                    lastUseTime = System.currentTimeMillis();
                    client.player.getWorld().playSound(client.player, client.player.getBlockPos(), SoundRegistry.SOUNDEFFECT7, SoundCategory.PLAYERS, 0.4f, 1.5f);

                } else {
                    client.player.sendMessage(Text.literal("Ability can be used again in " + (((lastUseTime + abilityCooldown) - System.currentTimeMillis()) / 1000) + "s"), true);
                }
            }

            /* Ability 2 and Toggle abilities disabled for now (To be implemented)

            while (bindingAbility2.wasPressed()) {
                client.player.sendMessage(Text.literal("Ability #2 has been used!"), false);
            }

            if (bindingAbility3.isPressed()) {
                client.player.sendMessage(Text.literal("Toggle Ability is active"), false);
            }
            */

        });
    }
}
