package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

@Environment(EnvType.CLIENT)
public class GameStagesClientPacketHandler extends GameStagesPacketHandler {
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(CHANNEL, GameStagesClientPacketHandler::receivePacket);
    }

    public static void receivePacket(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        var message = decodeStageMessage(buf);
        processSyncStagesMessage(message);
    }

    private static MessageStages decodeStageMessage(FriendlyByteBuf buffer) {
        return new MessageStages(Serializers.STRING.fromByteBufList(buffer));
    }

    private static void processSyncStagesMessage(MessageStages message) {

        // Reset the client data to a new object.
        final IStageData clientData = new StageData();

        // Re-add all stages
        for (final String stageName : message.getStages()) {

            clientData.addStage(stageName);
        }

        GameStageSaveHandler.setClientData(clientData);

        StagesSyncedEvent.STAGES_SYNCED_EVENT.invoker().onStagesSynced(clientData);
    }
}