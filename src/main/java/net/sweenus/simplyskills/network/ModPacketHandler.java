package net.sweenus.simplyskills.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;

public class ModPacketHandler {
    private static final Identifier UPDATE_UNSPENT_POINTS_ID = new Identifier(SimplySkills.MOD_ID, "update_unspent_points");
    private static final Identifier STOP_SOUND_ID = new Identifier(SimplySkills.MOD_ID, "stop_sound");
    private static final Identifier SYNC_ITEM_STACK_ID = new Identifier(SimplySkills.MOD_ID, "sync_item_stack");

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

        ClientPlayNetworking.registerGlobalReceiver(STOP_SOUND_ID, (client, handler, buf, responseSender) -> {
            StopSoundPacket packet = StopSoundPacket.decode(buf);
            client.execute(() -> {
                MinecraftClient.getInstance().getSoundManager().stopSounds(packet.getSoundId(), SoundCategory.PLAYERS);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ModPacketHandler.SYNC_ITEM_STACK_ID, (client, handler, buf, responseSender) -> {
            int slot = buf.readInt(); // Read the slot index from the packet
            ItemStack stack = buf.readItemStack(); // Read the ItemStack from the packet

            client.execute(() -> {
                // Update the ItemStack in the specified slot
                if (client.player != null) {
                    client.player.getInventory().setStack(slot, stack);
                }
            });
        });
    }

    public static void sendTo(ServerPlayerEntity player, UpdateUnspentPointsPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        UpdateUnspentPointsPacket.encode(packet, buf);
        ServerPlayNetworking.send(player, UPDATE_UNSPENT_POINTS_ID, buf);
    }

    public static void sendStopSoundPacket(ServerPlayerEntity player, Identifier soundId) {
        StopSoundPacket packet = new StopSoundPacket(soundId);
        PacketByteBuf buf = PacketByteBufs.create();
        StopSoundPacket.encode(packet, buf);
        ServerPlayNetworking.send(player, STOP_SOUND_ID, buf);
    }

    public static void syncItemStackNbt(ServerPlayerEntity player, int slot, ItemStack stack) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(slot); // Write the slot index to the packet
        buf.writeItemStack(stack); // Write the ItemStack to the packet
        ServerPlayNetworking.send(player, SYNC_ITEM_STACK_ID, buf);
    }
}