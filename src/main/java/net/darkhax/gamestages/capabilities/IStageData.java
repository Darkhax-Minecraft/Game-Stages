package net.darkhax.gamestages.capabilities;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class has been deprecated. It should not be used, because it will not work! This class
 * only exists to maintain compatibility with older worlds.
 * 
 * Please move to the new system in net.darkhax.gamestages.data.
 */
@Deprecated
public interface IStageData {
    
    /**
     * Gets the name of all unlocked stages.
     */
    Collection<String> getUnlockedStages ();
    
    /**
     * Checks if a player has a stage unlocked.
     *
     * @param stage The name of the stage to look for.
     * @return Whether or not the player has the stage unlocked.
     */
    boolean hasUnlockedStage (@Nonnull String stage);
    
    /**
     * Checks if a player has any of the passed stages. The player only needs one to be true.
     *
     * @param stages The stages to check for, the player only needs one of them.
     * @return Whether or not the player has any of the passed stages.
     */
    boolean hasUnlockedAnyOf (Collection<String> stages);
    
    /**
     * Checks if a player has all of the passed stages.
     *
     * @param stages The stages to check for, the player must have all of them.
     * @return Whether or not the player has all of the passed stages.
     */
    boolean hasUnlockedAll (Collection<String> stages);
    
    /**
     * Unlocks a stage for the player.
     *
     * @param stage The stage to unlock.
     */
    void unlockStage (@Nonnull String stage);
    
    /**
     * Locks a stage again, for the player.
     *
     * @param stage The stage to lock.
     */
    void lockStage (@Nonnull String stage);
    
    /**
     * Sets the player for the stage data. This is for internal use.
     *
     * @param player The player to set.
     */
    void setPlayer (@Nonnull EntityPlayer player);
    
    /**
     * Removes all stages for the player.
     */
    void clear ();
    
    /**
     * Checks if the player data has received a sync from the server.
     *
     * @return Whether or not the player data has received a sync from the server.
     */
    @SideOnly(Side.CLIENT)
    boolean hasBeenSynced ();
    
    /**
     * Sets the state for {@link #hasBeenSynced()}.
     *
     * @param synced Whether or not the player data has been synced.
     */
    @SideOnly(Side.CLIENT)
    void setSynced (boolean synced);
    
    /**
     * Gets the player for the stage data. Provided in this way for convenience.
     *
     * @return The player for the stage data.
     */
    @Nullable
    EntityPlayer getPlayer ();
}