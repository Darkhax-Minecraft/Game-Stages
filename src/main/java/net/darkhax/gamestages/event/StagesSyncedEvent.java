package net.darkhax.gamestages.event;

import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StagesSyncedEvent extends PlayerEvent {

    private final IStageData data;

    public StagesSyncedEvent (IStageData data, EntityPlayer player) {

        super(player);
        this.data = data;
    }

    public IStageData getData () {

        return this.data;
    }
}
