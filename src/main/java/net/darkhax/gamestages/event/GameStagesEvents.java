package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.qsl.base.api.event.Event;
import org.quiltmc.qsl.base.api.event.EventAwareListener;

/**
 * This class holds all the various game stage events. The main class itself should not be
 * treated as an event.
 */
public class GameStagesEvents {
    public static final Event<Add> ADD_EVENT = Event.create(Add.class, callbacks -> (player, stageName) -> {
        boolean result = true;
        for (var callback: callbacks) {
            if (!callback.onAdd(player, stageName)) {
                result = false;
            }
        }
        return result;
    });

    public static final Event<Added> ADDED_EVENT = Event.create(Added.class, callbacks -> (player, stageName) -> {
        for (var callback: callbacks) {
            callback.onAdded(player, stageName);
        }
    });

    public static final Event<Remove> REMOVE_EVENT = Event.create(Remove.class, callbacks -> (player, stageName) -> {
        boolean result = true;
        for (var callback: callbacks) {
            if (!callback.onRemove(player, stageName)) {
                result = false;
            }
        }
        return result;
    });

    public static final Event<Removed> REMOVED_EVENT = Event.create(Removed.class, callbacks -> (player, stageName) -> {
        for (var callback: callbacks) {
            callback.onRemoved(player, stageName);
        }
    });

    public static final Event<Cleared> CLEARED_EVENT = Event.create(Cleared.class, callbacks -> (player, stageName) -> {
        for (var callback: callbacks) {
            callback.onCleared(player, stageName);
        }
    });

    public static final Event<Check> CHECK_EVENT = Event.create(Check.class, callbacks -> (player, stageName, hasStage) -> {
        boolean result = hasStage;
        for (var callback: callbacks) {
            if (!callback.onCheck(player, stageName, hasStage)) {
                result = false;
            }
        }
        return result;
    });

    @FunctionalInterface
    public interface Add extends EventAwareListener {

        /**
         * This event is fired every time a stage is added to the player via
         * {@link net.darkhax.gamestages.GameStageHelper#addStage(ServerPlayer, String)}.
         * returning {@code false} will prevent the stage from being added.
         */
        boolean onAdd(Player player, String stageName);
    }
    
    @FunctionalInterface
    public interface Added extends EventAwareListener{

        /**
         * This event is fired after a stage has been successfully added using
         * {@link net.darkhax.gamestages.GameStageHelper#addStage(ServerPlayer, String)}.
         * This can not be canceled.
         */
        void onAdded(Player player, String stageName);
    }
    
    @FunctionalInterface
    public interface Remove extends EventAwareListener {

        /**
         * This event is fired when a stage is removed from a player via
         * {@link net.darkhax.gamestages.GameStageHelper#removeStage(ServerPlayer, String)}.
         * returning {@code false} will prevent the stage from being added.
         */
        boolean onRemove(Player player, String stageName);
    }

    @FunctionalInterface
    public interface Removed extends EventAwareListener{

        /**
         * This event is fired after a stage has been successfully removed using
         * {@link net.darkhax.gamestages.GameStageHelper#removeStage(ServerPlayer, String)}.
         * This can not be canceled.
         */
        void onRemoved(Player player, String stageName);
    }

    @FunctionalInterface
    public interface Cleared extends EventAwareListener {
        /**
         * This event is fired after the stages have been cleared from a player.
         */
        void onCleared(Player player, IStageData stageData);
    }
    
    @FunctionalInterface
    public interface Check extends EventAwareListener {
        /**
         * This event is fired when a stage check is done on a player using
         * {@link net.darkhax.gamestages.GameStageHelper#hasStage(Player, net.darkhax.gamestages.data.IStageData, String)}.
         * @return if the player has the stage
         */
        boolean onCheck (Player player, String stageName, boolean hasStageOriginal);
    }
}