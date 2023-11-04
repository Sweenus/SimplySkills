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
import net.puffish.skillsmod.api.Category;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.client.gui.CustomHud;
import net.sweenus.simplyskills.util.HelperMethods;
import net.sweenus.simplyskills.util.SkillReferencePosition;

import java.util.ArrayList;
import java.util.List;

public class ModPacketHandler {
    private static final Identifier UPDATE_UNSPENT_POINTS_ID = new Identifier(SimplySkills.MOD_ID, "update_unspent_points");
    private static final Identifier STOP_SOUND_ID = new Identifier(SimplySkills.MOD_ID, "stop_sound");
    private static final Identifier SYNC_ITEM_STACK_ID = new Identifier(SimplySkills.MOD_ID, "sync_item_stack");
    private static final Identifier SYNC_SIGNATURE_ABILITY = new Identifier(SimplySkills.MOD_ID, "sync_signature_ability");

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

        ClientPlayNetworking.registerGlobalReceiver(SYNC_SIGNATURE_ABILITY, (client, handler, buf, responseSender) -> {
            Identifier identifierPacket = buf.readIdentifier();
            String stringPacket = buf.readString().replace("simplyskills:","") + "_signature_";
            String spritePath = null;
            if (identifierPacket.toString().contains(SkillReferencePosition.rogueSpecialisationSiphoningStrikes))
                spritePath = "siphoning_strikes";
            else if (identifierPacket.toString().contains(SkillReferencePosition.rogueSpecialisationEvasion))
                spritePath = "evasion";
            else if (identifierPacket.toString().contains(SkillReferencePosition.rogueSpecialisationPreparation))
                spritePath = "preparation";

            Identifier newIdentifierPacket = new Identifier(SimplySkills.MOD_ID, "textures/icons/" + stringPacket + spritePath +".png");
            client.execute(() -> {
                CustomHud.setSprite(newIdentifierPacket);
            });
        });

    }

    public static void sendTo(ServerPlayerEntity player, UpdateUnspentPointsPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        UpdateUnspentPointsPacket.encode(packet, buf);
        ServerPlayNetworking.send(player, UPDATE_UNSPENT_POINTS_ID, buf);
    }

    public static void sendSignatureAbility(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        Identifier identifier = null;
        String stringSend = null;
        List<String> list = new ArrayList<>();
        List<String> specialisationList = SimplySkills.getSpecialisationsAsArray();
        list.add(SkillReferencePosition.rogueSpecialisationPreparation);
        list.add(SkillReferencePosition.rogueSpecialisationEvasion);
        list.add(SkillReferencePosition.rogueSpecialisationSiphoningStrikes);

        for (String string : list) {
            for (String specialisations : specialisationList) {
                System.out.println("checking: " + string + " " + specialisations);
                if (HelperMethods.isUnlocked(specialisations, string, player)) {
                    identifier = new Identifier(SimplySkills.MOD_ID, string);
                    stringSend = specialisations;
                    System.out.println("detected class: " + stringSend);
                    System.out.println("detected ability: " + identifier);
                    break;
                }
            }
        }
        if (identifier != null && stringSend != null) {
            System.out.println("sending to client: " + identifier + stringSend);
            buf.writeIdentifier(identifier);
            buf.writeString(stringSend);
            ServerPlayNetworking.send(player, SYNC_SIGNATURE_ABILITY, buf);
        }
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