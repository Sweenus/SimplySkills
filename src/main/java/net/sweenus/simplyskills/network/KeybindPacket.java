package net.sweenus.simplyskills.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.abilities.SignatureAbilities;

public class KeybindPacket {

    public static final Identifier ABILITY1_PACKET = new Identifier("simplyskills", "ability1");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ABILITY1_PACKET, (server, player, handler, buffer, sender) -> {
            server.execute(()->{

                SignatureAbilities.signatureAbilityManager(player);

            });
        });

    }

}
