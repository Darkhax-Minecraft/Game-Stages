package net.darkhax.gamestages.event;

import net.darkhax.gamestages.GameStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This class holds all the various game stage events. The main class itself should not be
 * treated as an event.
 */
public class GameStageEvent extends Event {

    /**
     * The player the event is for.
     */
    private final EntityPlayer player;

    /**
     * The stage the event is for. This stage can be changed by other mods listening to the event!
     */
    private String stageName;

    /**
     * The base constructor for all game stage events.
     *
     * @param player The player the event is for.
     * @param stageName The stage the event is for.
     */
    public GameStageEvent (EntityPlayer player, String stageName) {

        this.player = player;
        this.stageName = stageName;
    }

    /**
     * Gets the player the event is for.
     *
     * @return The player that the event was fired for.
     */
    public EntityPlayer getPlayer () {

        return this.player;
    }

    /**
     * Gets the stage name for the event. Note that other listeners can change this!
     *
     * @return The stage name for the event.
     */
    public String getStageName () {

        return this.stageName;
    }

    /**
     * Sets the stage for the event. This can be used to change or manipulate the stage name.
     *
     * @param stage The new name for the stage.
     */
    public void setStage (String stage) {

        this.stageName = stage;
    }

    /**
     * This event is fired every time a stage is added to a player, or unlocked. This event is
     * fired on both the client and the server. This event is still fired, even if the player has
     * the stage. This event can be canceled, which will prevent it from being added.
     */
    @Cancelable
    public static class Add extends GameStageEvent {

        public Add (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a stage is removed from a player, or locked. This event is
     * fired on both the client and the server. This event is still fired, even if the player does
     * not have the stage being removed. This event can be canceled, which will prevent the stage
     * from being removed.
     */
    @Cancelable
    public static class Remove extends GameStageEvent {

        public Remove (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a check for a stage is made. This event can be canceled,
     * which will make the check fail.
     *
     * Note: To ensure this event is reliable, all mods which add support for GameStages should
     * make use of
     * {@link net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData#hasUnlockedStage(String)}.
     * Both
     * {@link net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData#hasUnlockedAnyOf(String...)}
     * and
     * {@link net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData#hasUnlockedAll(String...)}
     * make use of this method, which makes them acceptable.
     */
    @Cancelable
    public static class Check extends GameStageEvent {

        public Check (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time there is a client side stage sync. It allows mods to hook
     * into the syncing, and perform various client side updates. It can not be canceled, and
     * {@link GameStageEvent#setStage(String)} has been disabled.
     */
    public static class ClientSync extends GameStageEvent {

        /**
         * Whether or not the stage is being unlocked. true = stage is added. false = stage is removed.
         */
        private final boolean isUnlocking;

        public ClientSync (EntityPlayer player, String stageName, boolean isUnlocking) {

            super(player, stageName);
            this.isUnlocking = isUnlocking;
        }

        /**
         * Checks if the stage is being unlocked (added/true) or locked (removed/false).
         *
         * @return Whether or not the stage is being unlocked.
         */
        public boolean isUnlocking () {

            return this.isUnlocking;
        }

        @Override
        public void setStage (String stage) {

            GameStages.LOG.warn("You can not change the stage during the ClientSync event! Stage:{}", this.getStageName());
        }
    }
}