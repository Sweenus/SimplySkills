package net.sweenus.simplyskills.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class StopSoundPacket {
    private final Identifier soundId;

    public StopSoundPacket(Identifier soundId) {
        this.soundId = soundId;
    }

    public Identifier getSoundId() {
        return soundId;
    }

    public static void encode(StopSoundPacket packet, PacketByteBuf buf) {
        buf.writeIdentifier(packet.getSoundId());
    }

    public static StopSoundPacket decode(PacketByteBuf buf) {
        Identifier soundId = buf.readIdentifier();
        return new StopSoundPacket(soundId);
    }
}