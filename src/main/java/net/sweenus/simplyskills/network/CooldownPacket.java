package net.sweenus.simplyskills.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.client.SimplySkillsClient;


public class CooldownPacket {

    public static final Identifier COOLDOWN_PACKET = new Identifier("simplyskills", "cooldown");

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(COOLDOWN_PACKET, (client, handler, buffer, sender) -> {

            int cooldown = buffer.readInt();

            client.execute(()->{

                SimplySkillsClient.abilityCooldown = cooldown;
                //System.out.println("cooldown is: " +cooldown +"ms");


            });
        });

    }

}
