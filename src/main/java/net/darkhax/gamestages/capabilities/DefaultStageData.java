package net.darkhax.gamestages.capabilities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class has been deprecated. It should not be used, because it will not work! This class
 * only exists to maintain compatibility with older worlds.
 * 
 * Please move to the new system in net.darkhax.gamestages.data.
 */
@Deprecated
public class DefaultStageData implements IStageData {
    
    /**
     * A list of all unlocked stages.
     */
    private final Set<String> unlockedStages = new HashSet<>();
    
    /**
     * Whether or not the data has been synced on the client.
     */
    private boolean synced = false;
    
    /**
     * The player who owns this data.
     */
    private EntityPlayer player;
    
    @Override
    public Collection<String> getUnlockedStages () {
        
        return Collections.unmodifiableCollection(this.unlockedStages);
    }
    
    @Override
    public boolean hasUnlockedAnyOf (Collection<String> stages) {
        
        for (final String stage : stages) {
            if (this.hasUnlockedStage(stage)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean hasUnlockedAll (Collection<String> stages) {
        
        for (final String stage : stages) {
            if (!this.hasUnlockedStage(stage)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean hasUnlockedStage (String stage) {
        
        final GameStageEvent event = new GameStageEvent.Check(this.getPlayer(), stage);
        final boolean notCanceled = !MinecraftForge.EVENT_BUS.post(event);
        return event.getStageName() == null ? false : notCanceled && this.unlockedStages.contains(event.getStageName().toLowerCase());
    }
    
    @Override
    public void unlockStage (String stage) {
        
        final GameStageEvent event = new GameStageEvent.Add(this.getPlayer(), stage);
        MinecraftForge.EVENT_BUS.post(event);
        
        if (!event.isCanceled()) {
            this.unlockedStages.add(event.getStageName().toLowerCase());
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Added(this.getPlayer(), stage));
        }
    }
    
    @Override
    public void lockStage (String stage) {
        
        final GameStageEvent event = new GameStageEvent.Remove(this.getPlayer(), stage);
        MinecraftForge.EVENT_BUS.post(event);
        
        if (!event.isCanceled()) {
            this.unlockedStages.remove(stage.toLowerCase());
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Removed(this.getPlayer(), stage));
        }
    }
    
    @Override
    public void setPlayer (EntityPlayer player) {
        
        this.player = player;
    }
    
    @Override
    public EntityPlayer getPlayer () {
        
        return this.player;
    }
    
    @Override
    public void clear () {
        
        this.unlockedStages.clear();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasBeenSynced () {
        
        return this.synced;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setSynced (boolean synced) {
        
        this.synced = synced;
    }
}