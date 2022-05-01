package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class GameStagesPacketHandler {

    private final String PROTOCOL = "8";
    private final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(GameStages.MOD_ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public GameStagesPacketHandler() {

        channel.registerMessage(0, MessageStages.class, this::encodeStageMessage, this::decodeStageMessage, this::processSyncStagesMessage);
    }

    public void syncPlayerStages(ServerPlayer player, MessageStages msg) {

        channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    private MessageStages decodeStageMessage(FriendlyByteBuf buffer) {

        return new MessageStages(Serializers.STRING.fromByteBufList(buffer));
    }

    private void processSyncStagesMessage(MessageStages message, Supplier<NetworkEvent.Context> ctx) {

        // Reset the client data to a new object.
        final IStageData clientData = new StageData();

        // Re-add all stages
        for (final String stageName : message.getStages()) {

            clientData.addStage(stageName);
        }

        GameStageSaveHandler.setClientData(clientData);

        // Alert all the listeners.
        MinecraftForge.EVENT_BUS.post(new StagesSyncedEvent(clientData));
    }

    private void encodeStageMessage (MessageStages packet, FriendlyByteBuf buffer) {

        Serializers.STRING.toByteBufList(buffer, packet.getStages());
    }
}