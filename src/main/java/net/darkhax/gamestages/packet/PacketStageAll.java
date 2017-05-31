package net.darkhax.gamestages.packet;

import java.util.Collection;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IAdditionalStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This packet is used to sync all of the stages from the server to the client. It must first
 * be requested by having the client send a request packet.
 */
public class PacketStageAll extends SerializableMessage {

    /**
     * An array of all the stage names.
     */
    public String[] stages;

    public PacketStageAll () {

    }

    public PacketStageAll (Collection<String> sstages) {

        this.stages = sstages.toArray(new String[0]);
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        final EntityPlayer player = PlayerUtils.getClientPlayer();
        final IStageData info = PlayerDataHandler.getStageData(player);

        for (final String stageName : this.stages) {

            info.unlockStage(stageName);
        }

        for (final IAdditionalStageData handler : PlayerDataHandler.getDataHandlers()) {

            handler.onClientSync(player, "", false);
        }

        return null;
    }
}
