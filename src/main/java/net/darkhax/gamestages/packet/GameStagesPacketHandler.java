package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.api.data.bytebuf.BookshelfByteBufs;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.List;

public class GameStagesPacketHandler {

    private final int PROTOCOL = 9;
    private final ResourceLocation CHANNEL_ID = new ResourceLocation(GameStages.MOD_ID, "main");
    private final SimpleChannel channel = ChannelBuilder.named(CHANNEL_ID).networkProtocolVersion(PROTOCOL).clientAcceptedVersions((status, version) -> true).serverAcceptedVersions((status, version) -> true).simpleChannel();

    public GameStagesPacketHandler() {

        channel.messageBuilder(MessageStages.class, 0, NetworkDirection.PLAY_TO_CLIENT).encoder(this::encodeStageMessage).decoder(this::decodeStageMessage).consumerMainThread(this::processSyncStagesMessage).add();
    }

    public void syncPlayerStages(ServerPlayer player, MessageStages msg) {

        if (msg != null) {

            channel.send(msg, PacketDistributor.PLAYER.with(player));
        }
    }

    private void encodeStageMessage(MessageStages packet, FriendlyByteBuf buffer) {

        BookshelfByteBufs.STRING.writeList(buffer, packet.getStages());
    }

    private MessageStages decodeStageMessage(FriendlyByteBuf buffer) {

        final List<String> stages = BookshelfByteBufs.STRING.readList(buffer);
        return new MessageStages(stages);
    }

    private void processSyncStagesMessage(MessageStages message, CustomPayloadEvent.Context ctx) {

        // Reset the client data to a new object.
        final IStageData clientData = new StageData();

        // Re-add all stages
        for (final String stageName : message.getStages()) {

            clientData.addStage(stageName);
        }

        GameStageSaveHandler.setClientData(clientData);

        // Alert all the listeners.
        MinecraftForge.EVENT_BUS.post(new StagesSyncedEvent(clientData));
        ctx.setPacketHandled(true);
    }
}