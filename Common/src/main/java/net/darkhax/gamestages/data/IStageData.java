package net.darkhax.gamestages.data;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface IStageData {
    
    /**
     * Gets a collection of all unlocked stages.
     *
     * @return A collection containing all of the unlocked stages.
     */
    Collection<String> getStages ();
    
    /**
     * Checks if a stage is unlocked.
     *
     * @param stage The stage to check.
     * @return Whether or not the stage has been unlocked.
     */
    boolean hasStage (@Nonnull String stage);
    
    /**
     * Adds a stage to the unlocked stages collection.
     *
     * @param stage The stage to unlock.
     */
    void addStage (@Nonnull String stage);
    
    /**
     * Removes a stage from the unlocked stages collection.
     *
     * @param stage The stage to remove.
     */
    void removeStage (@Nonnull String stage);
    
    /**
     * Clears all of the unlocked stages.
     */
    void clear ();
    
    void readFromNBT (CompoundTag tag);
    
    CompoundTag writeToNBT ();
}