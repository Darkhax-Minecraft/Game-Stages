package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * This class holds all the various game stage events. The main class itself should not be
 * treated as an event.
 */
public class GameStageEvent extends PlayerEvent {
    
    /**
     * The stage the event is for.
     */
    private final String stageName;
    
    /**
     * The base constructor for all game stage events.
     *
     * @param player The player the event is for.
     * @param stageName The stage the event is for.
     */
    public GameStageEvent(PlayerEntity player, String stageName) {
        
        super(player);
        this.stageName = stageName;
    }
    
    /**
     * Gets the stage name for the event.
     *
     * @return The stage name for the event.
     */
    public String getStageName () {
        
        return this.stageName;
    }
    
    /**
     * This event is fired every time a stage is added to the player via
     * {@link net.darkhax.gamestages.GameStageHelper#addStage(ServerPlayerEntity, String)}.
     * Canceling this event will prevent the stage from being added.
     */
    @Cancelable
    public static class Add extends GameStageEvent {
        
        public Add(PlayerEntity player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    /**
     * This event is fired after a stage has been successfully added using
     * {@link net.darkhax.gamestages.GameStageHelper#addStage(ServerPlayerEntity, String)}.
     * This can not be canceled.
     */
    public static class Added extends GameStageEvent {
        
        public Added(PlayerEntity player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    /**
     * This event is fired when a stage is removed from a player via
     * {@link net.darkhax.gamestages.GameStageHelper#removeStage(ServerPlayerEntity, String)}.
     * Canceling this event will prevent it from being added.
     */
    @Cancelable
    public static class Remove extends GameStageEvent {
        
        public Remove(PlayerEntity player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    /**
     * This event is fired after a stage has been successfully removed using
     * {@link net.darkhax.gamestages.GameStageHelper#removeStage(ServerPlayerEntity, String)}.
     * This can not be canceled.
     */
    public static class Removed extends GameStageEvent {
        
        public Removed(PlayerEntity player, String stageName) {
            
            super(player, stageName);
        }
    }
    
    /**
     * This event is fired after the stages have been cleared from a player.
     */
    public static class Cleared extends PlayerEvent {
        private final IStageData stageData;
        
        public Cleared(PlayerEntity player, IStageData stageData) {
            
            super(player);
            
            this.stageData = stageData;
        }
        
        public IStageData getStageData () {
            
            return this.stageData;
        }
    }
    
    /**
     * This event is fired when a stage check is done on a player using
     * {@link net.darkhax.gamestages.GameStageHelper#hasStage(ServerPlayerEntity, net.darkhax.gamestages.data.IStageData, String)}.
     */
    public static class Check extends GameStageEvent {
        
        /**
         * Whether or not the player originally had the stage.
         */
        private final boolean hasStageOriginal;
        
        /**
         * Whether or not the player has the stage.
         */
        private boolean hasStage;
        
        public Check(PlayerEntity player, String stageName, boolean hasStage) {
            
            super(player, stageName);
            this.hasStageOriginal = hasStage;
            this.hasStage = hasStage;
        }
        
        /**
         * Checks if the player originally had the stage.
         *
         * @return Whether or not the player originally had this stage.
         */
        public boolean hadStageOriginally () {
            
            return this.hasStageOriginal;
        }
        
        /**
         * Checks if the player has the stage according to the event.
         *
         * @return Whether or not the event says they have the stage.
         */
        public boolean hasStage () {
            
            return this.hasStage;
        }
        
        /**
         * Sets the result of the event.
         *
         * @param hasStage Whether or not the player should have this event.
         */
        public void setHasStage (boolean hasStage) {
            
            this.hasStage = hasStage;
        }
    }
}