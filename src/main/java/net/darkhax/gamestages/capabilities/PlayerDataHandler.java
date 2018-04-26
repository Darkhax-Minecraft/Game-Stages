package net.darkhax.gamestages.capabilities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.data.FakePlayerData;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.packet.PacketRequestClientSync;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerDataHandler {

    /**
     * A reference to the capability. Mods can call this, or use their own.
     */
    @CapabilityInject(IStageData.class)
    public static final Capability<IStageData> CAPABILITY = null;

    /**
     * Gets the stage data for a player.
     *
     * @param player The player to get stage data from.
     * @return The stage data for the player.
     */
    public static IStageData getStageData (EntityPlayer player) {
        if (player != null) {
            if (player.hasCapability(CAPABILITY, EnumFacing.DOWN)) return player.getCapability(CAPABILITY, EnumFacing.DOWN);
            if (player instanceof FakePlayer) return FakePlayerData.getDataFor(player.getName());
        }
        return null;
    }

    /**
     * This event is used to attach the data to the player.
     */
    @SubscribeEvent
    public void attachCapabilities (AttachCapabilitiesEvent<Entity> event) {

        if ((event.getObject() instanceof EntityPlayer) && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(new ResourceLocation("gamestages", "playerdata"), new GameStageCapProvider());
        }
    }

    /**
     * This event is used to sync stage data initially.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEntityJoinWorld (EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayerSP && !event.getEntity().isDead) {

            GameStages.NETWORK.sendToServer(new PacketRequestClientSync());
        }
    }

    /**
     * This event is used to make player data persist after death.
     */
    @SubscribeEvent
    public void clonePlayer (PlayerEvent.Clone event) {

        final long time = System.currentTimeMillis();
        final IStageData original = getStageData(event.getOriginal());
        final IStageData clone = getStageData((EntityPlayer) event.getEntity());

        if (original != null && clone != null) {

            for (final String stage : original.getStages()) {

                clone.addStage(stage);
            }

            GameStages.LOG.info("Preserving data for " + event.getOriginal().getName() + ". Took " + (System.currentTimeMillis() - time) + "ms.");
        }
    }
}