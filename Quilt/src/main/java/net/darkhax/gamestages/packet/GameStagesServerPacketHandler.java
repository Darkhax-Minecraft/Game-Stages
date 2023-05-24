package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.api.serialization.Serializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

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