package net.darkhax.gamestages.capabilities;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.darkhax.gamestages.FakePlayerData;
import net.darkhax.gamestages.GameStages;
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
            event.addCapability(new ResourceLocation("gamestages", "playerdata"), new Provider((EntityPlayer) event.getObject()));
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

            for (final String stage : original.getUnlockedStages()) {

                clone.unlockStage(stage);
            }

            GameStages.LOG.info("Preserving data for " + event.getOriginal().getName() + ". Took " + (System.currentTimeMillis() - time) + "ms.");
        }
    }

    /**
     * This is the backing interface for the custom player data. You don't need to do anything
     * with this class, although it does define which methods you can use when working with
     * stage data.
     */
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
         * Checks if a player has any of the passed stages. The player only needs one to be
         * true.
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

    /**
     * The default implementation of IStageData.
     */
    public static class DefaultStageData implements IStageData {

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
            return (event.getStageName() == null) ? false : notCanceled && this.unlockedStages.contains(event.getStageName().toLowerCase());
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

    /**
     * This class handles the read/write of NBT in capabilities. Another internal class. It
     * also handles the IAdditionalStageData hooks which you can use!
     */
    public static class Storage implements Capability.IStorage<IStageData> {

        @Override
        public NBTBase writeNBT (Capability<IStageData> capability, IStageData instance, EnumFacing side) {

            final NBTTagCompound tag = new NBTTagCompound();

            final NBTTagList tagList = new NBTTagList();

            for (final String string : instance.getUnlockedStages()) {
                tagList.appendTag(new NBTTagString(string));
            }

            tag.setTag("UnlockedStages", tagList);

            return tag;
        }

        @Override
        public void readNBT (Capability<IStageData> capability, IStageData instance, EnumFacing side, NBTBase nbt) {

            final NBTTagCompound tag = (NBTTagCompound) nbt;

            final NBTTagList tagList = tag.getTagList("UnlockedStages", NBT.TAG_STRING);

            for (int index = 0; index < tagList.tagCount(); index++) {
                instance.unlockStage(tagList.getStringTagAt(index));
            }
        }
    }

    /**
     * This is the internal provider class.
     */
    private static class Provider implements ICapabilitySerializable<NBTTagCompound> {

        IStageData instance = CAPABILITY.getDefaultInstance();

        public Provider (EntityPlayer player) {

            this.instance.setPlayer(player);
        }

        @Override
        public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

            return capability == CAPABILITY;
        }

        @Override
        public <T> T getCapability (Capability<T> capability, EnumFacing facing) {

            return this.hasCapability(capability, facing) ? CAPABILITY.<T> cast(this.instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT () {

            return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, this.instance, null);
        }

        @Override
        public void deserializeNBT (NBTTagCompound nbt) {

            CAPABILITY.getStorage().readNBT(CAPABILITY, this.instance, null, nbt);
        }
    }
}