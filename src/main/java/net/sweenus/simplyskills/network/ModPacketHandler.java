package net.sweenus.simplyskills.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;

public class ModPacketHandler {
    private static final Identifier UPDATE_UNSPENT_POINTS_ID = new Identifier(SimplySkills.MOD_ID, "update_unspent_points");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(UPDATE_UNSPENT_POINTS_ID, (server, player, handler, buf, responseSender) -> {
            UpdateUnspentPointsPacket packet = UpdateUnspentPointsPacket.decode(buf);
            server.execute(() -> {
                // update unspentPoints on the server side
                UpdateUnspentPointsPacket.handleServer(packet, handler, responseSender);
            });
        });
    }
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_UNSPENT_POINTS_ID, (client, handler, buf, responseSender) -> {
            UpdateUnspentPointsPacket packet = UpdateUnspentPointsPacket.decode(buf);
            client.execute(() -> {
                // update unspentPoints on the client side
                UpdateUnspentPointsPacket.handleClient(packet, handler, responseSender);
            });
        });
    }

    public static void sendTo(ServerPlayerEntity player, UpdateUnspentPointsPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        UpdateUnspentPointsPacket.encode(packet, buf);
        ServerPlayNetworking.send(player, UPDATE_UNSPENT_POINTS_ID, buf);
    }
}