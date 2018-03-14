package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.event.StageDataEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * A packet to request stages be synced to the client.
 */
public class PacketRequestClientSync extends SerializableMessage {

    public PacketRequestClientSync () {

        // Empty constructor for forge's system
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        final EntityPlayer player = context.getServerHandler().player;
        final IStageData info = PlayerDataHandler.getStageData(player);
        MinecraftForge.EVENT_BUS.post(new StageDataEvent.SyncRequested(player, info));
        GameStages.LOG.info("Syncing data for " + player.getName() + " requested.");
        return new PacketSyncClient(info.getUnlockedStages());
    }
}
