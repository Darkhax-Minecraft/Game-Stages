package net.darkhax.gamestages.packet;

import java.util.Collection;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.StageData;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
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

            GameStageSaveHandler.clientData = new StageData();

            // Re-add all stages
            for (final String stageName : this.stages) {
                
                GameStageSaveHandler.clientData.addStage(stageName);
            }

            MinecraftForge.EVENT_BUS.post(new StagesSyncedEvent(GameStageSaveHandler.clientData, PlayerUtils.getClientPlayer()));
        });

        return null;
    }
}
