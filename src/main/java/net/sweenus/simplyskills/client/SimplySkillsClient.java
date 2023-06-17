package net.sweenus.simplyskills.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.SignatureAbilities;
import org.lwjgl.glfw.GLFW;

public class SimplySkillsClient implements ClientModInitializer {

    public int abilityCooldown = 10000;
    public long lastUseTime;

    @Override
    public void onInitializeClient() {

        //Keybindings
        KeyBinding bindingAbility1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.category.simplyskills"));
        //KeyBinding bindingAbility2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.simplyskills"));
        //KeyBinding bindingAbility3 = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding("key.simplyskills.ability3", GLFW.GLFW_KEY_V, "key.category.simplyskills", () -> true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (bindingAbility1.wasPressed()) {
                if (System.currentTimeMillis() > (lastUseTime + abilityCooldown)) {
                    //client.player.sendMessage(Text.literal("Ability #1 has been used!"), false);
                    //client.player.sendMessage(Text.literal("Used at system time: " + System.currentTimeMillis()), false);
                    SignatureAbilities.sendKeybindPacket();
                    client.player.world.playSoundFromEntity(null, client.player, SoundRegistry.FX_UI_UNLOCK3,
                            SoundCategory.PLAYERS, 1, 1);

                    lastUseTime = System.currentTimeMillis();
                    //client.player.sendMessage(Text.literal("Next use available at: " +(lastUseTime + abilityCooldown)), false);
                } else {
                    client.player.sendMessage(Text.literal("Ability can be used again in " + (((lastUseTime + abilityCooldown) - System.currentTimeMillis()) / 1000) + "s"), false);
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
