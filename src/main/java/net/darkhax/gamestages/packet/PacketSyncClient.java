package net.darkhax.gamestages.packet;

import java.util.Collection;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.event.StageDataEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This packet is used to sync all of the stages from the server to the client. It must first
 * be requested by having the client send a request packet.
 */
public class PacketSyncClient extends SerializableMessage {

    /**
     * An array of all the stage names.
     */
    public String[] stages;

    public PacketSyncClient () {

        // Empty constructor for forge's system
    }

    public PacketSyncClient (Collection<String> stages) {

        this.stages = stages.toArray(new String[0]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        Minecraft.getMinecraft().addScheduledTask( () -> {

            final EntityPlayer player = PlayerUtils.getClientPlayer();
            final IStageData info = PlayerDataHandler.getStageData(player);

            info.setSynced(false);

            GameStages.LOG.info("Syncing recived for " + player.getName());

            // Remove all stages
            info.clear();

            // Re-add all stages
            for (final String stageName : this.stages) {
                info.unlockStage(stageName);
            }

            info.setSynced(true);
            MinecraftForge.EVENT_BUS.post(new StageDataEvent.SyncRecieved(player, info));
        });

        return null;
    }
}
