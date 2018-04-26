package net.darkhax.gamestages.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * This class holds all the various game stage events. The main class itself should not be
 * treated as an event.
 */
public class GameStageEvent extends PlayerEvent {

    /**
     * The stage the event is for. This stage can be changed by other mods listening to the
     * event!
     */
    private String stageName;

    /**
     * The base constructor for all game stage events.
     *
     * @param player The player the event is for.
     * @param stageName The stage the event is for.
     */
    public GameStageEvent (EntityPlayer player, String stageName) {

        super(player);
        this.stageName = stageName;
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
     * This event is fired every time a stage is going to be added to a player, or unlocked.
     * This event is fired on both the client and the server. This event is still fired, even
     * if the player has the stage. This event can be canceled, which will prevent it from
     * being added.
     */
    @Cancelable
    public static class Add extends GameStageEvent {

        public Add (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a stage is added to a player, or unlocked. This event is
     * fired on both the client and the server. This event is still fired, even if the player
     * has the stage. This event can not be canceled.
     */
    public static class Added extends GameStageEvent {

        public Added (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a stage is going to be removed from a player, or locked.
     * This event is fired on both the client and the server. This event is still fired, even
     * if the player does not have the stage being removed. This event can be canceled, which
     * will prevent the stage from being removed.
     */
    @Cancelable
    public static class Remove extends GameStageEvent {

        public Remove (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a stage is removed from a player, or locked. This event
     * is fired on both the client and the server. This event is still fired, even if the
     * player does not have the stage being removed. This event can not be canceled.
     */
    public static class Removed extends GameStageEvent {

        public Removed (EntityPlayer player, String stageName) {

            super(player, stageName);
        }
    }

    /**
     * This event is fired every time a check for a stage is made. This event can be canceled,
     * which will make the check fail.
     */
    public static class Check extends GameStageEvent {

        private boolean hasStage;

        public Check (EntityPlayer player, String stageName, boolean hasStage) {

            super(player, stageName);
            this.hasStage = hasStage;
        }

        public boolean hasStage () {

            return this.hasStage;
        }

        public void setHasStage (boolean hasStage) {

            this.hasStage = hasStage;
        }
    }
}