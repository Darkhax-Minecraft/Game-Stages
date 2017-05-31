package net.darkhax.gamestages.packet;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * A packet to request stages be synced to the client.
 */
public class PacketRequestClientSync extends SerializableMessage {

    public PacketRequestClientSync () {

    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        final EntityPlayer player = context.getServerHandler().playerEntity;
        final IStageData info = PlayerDataHandler.getStageData(player);
        return new PacketStageAll(info.getUnlockedStages());
    }
}
