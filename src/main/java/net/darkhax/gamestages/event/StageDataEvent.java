package net.darkhax.gamestages.event;

import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class holds events for an entire player's stage data, and not just one stage.
 */
public class StageDataEvent extends PlayerEvent {

    /**
     * The player's stage data.
     */
    private final IStageData stageData;

    protected StageDataEvent (EntityPlayer player, IStageData stageData) {

        super(player);
        this.stageData = stageData;
    }

    /**
     * Gets the player the event is fired for.
     *
     * @return The player the event is for.
     */
    @Deprecated
    public EntityPlayer getPlayer () {

        return this.getEntityPlayer();
    }

    /**
     * Gets the stage data for the player.
     *
     * @return The player's stage data.
     */
    public IStageData getStageData () {

        return this.stageData;
    }

    /**
     * This event is fired on the server, when the client sends a request packet to get their
     * stage info from the server. This allows for stages to be added or removed before they
     * are synced to the client, or to handle your own syncing when the client requests stages.
     */
    public static class SyncRequested extends StageDataEvent {

        public SyncRequested (EntityPlayer player, IStageData stageData) {

            super(player, stageData);
        }
    }

    /**
     * This event is fired on the client after they have recieved the sync packet with all
     * their stages from the server. This allows you to respond to the client being synced, and
     * change your own related systems to reflect the new data.
     */
    @SideOnly(Side.CLIENT)
    public static class SyncRecieved extends StageDataEvent {

        public SyncRecieved (EntityPlayer player, IStageData stageData) {

            super(player, stageData);
        }
    }
}