package net.sweenus.simplyskills.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sweenus.simplyskills.client.SimplySkillsClient;

public class UpdateUnspentPointsPacket {
    private final int unspentPoints;

    public UpdateUnspentPointsPacket(int unspentPoints) {
        this.unspentPoints = unspentPoints;
    }

    public static void encode(UpdateUnspentPointsPacket packet, PacketByteBuf buf) {
        buf.writeInt(packet.unspentPoints);
    }

    public static UpdateUnspentPointsPacket decode(PacketByteBuf buf) {
        return new UpdateUnspentPointsPacket(buf.readInt());
    }

    public static void handleServer(UpdateUnspentPointsPacket packet, ServerPlayNetworkHandler handler, PacketSender responseSender) {
        ServerPlayerEntity player = handler.player;
        player.getServer().execute(() -> {
            // update unspentPoints on the server side
        });
    }

    public static void handleClient(UpdateUnspentPointsPacket packet, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        MinecraftClient.getInstance().execute(() -> {
            // update unspentPoints on the client side
            SimplySkillsClient.unspentPoints = packet.unspentPoints;
        });
    }
}