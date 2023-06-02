package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.api.serialization.Serializers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class GameStagesServerPacketHandler extends GameStagesPacketHandler {

    public static void syncPlayerStages(ServerPlayer player, MessageStages msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        encodeStageMessage(msg, buf);
        ServerPlayNetworking.send(player, CHANNEL, buf);
    }

    private static void encodeStageMessage (MessageStages packet, FriendlyByteBuf buffer) {
        Serializers.STRING.toByteBufList(buffer, packet.getStages());
    }
}