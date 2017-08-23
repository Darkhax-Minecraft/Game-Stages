package net.darkhax.gamestages.capabilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.packet.PacketRequestClientSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDataHandler {

    /**
     * A reference to the capability. Mods can call this, or use their own.
     */
    @CapabilityInject(IStageData.class)
    public static final Capability<IStageData> CAPABILITY = null;

    /**
     * A list containing all of the additional data handlers.
     */
    private static final Map<String, IAdditionalStageData> ADDITIONAL_DATA = new HashMap<>();

    /**
     * Gets the stage data for a player.
     *
     * @param player The player to get stage data from.
     * @return The stage data for the player.
     */
    public static IStageData getStageData (@Nonnull EntityPlayer player) {

        return player != null && player.hasCapability(CAPABILITY, EnumFacing.DOWN) ? player.getCapability(CAPABILITY, EnumFacing.DOWN) : null;
    }

    /**
     * Registers an additional data handler.
     *
     * @param id The id of the handler. Please make this unique.
     * @param data The data handler to register.
     */
    public static void registerDataHandler (@Nonnull String id, @Nonnull IAdditionalStageData data) {

        ADDITIONAL_DATA.put(id, data);
    }

    /**
     * Gets the additional data handler.
     *
     * @param id The id of the handler to get.
     * @return The data handler that was found.
     */
    public static IAdditionalStageData getDataHandler (@Nonnull String id) {

        return ADDITIONAL_DATA.get(id);
    }

    /**
     * Gets all the additional data handlers.
     *
     * @return The additional data handlers.
     */
    public static Collection<IAdditionalStageData> getDataHandlers () {

        return ADDITIONAL_DATA.values();
    }

    /**
     * This event is used to attach the data to the player.
     */
    @SubscribeEvent
    public void attachCapabilities (AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation("gamestages", "playerdata"), new Provider((EntityPlayer) event.getObject()));
        }
    }

    /**
     * This event is used to sync stage data initially.
     */
    @SubscribeEvent
    public void onEntityJoinWorld (EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayer && event.getWorld().isRemote) {

            GameStages.NETWORK.sendToServer(new PacketRequestClientSync());
        }
    }

    /**
     * This event is used to make player data persist after death.
     */
    @SubscribeEvent
    public void clonePlayer (PlayerEvent.Clone event) {

        final IStageData original = getStageData(event.getOriginal());
        final IStageData clone = getStageData((EntityPlayer) event.getEntity());

        for (final String stage : original.getUnlockedStages()) {
            clone.unlockStage(stage);
        }
    }

    /**
     * This is the backing interface for the custom player data. You don't need to do anything with
     * this class, although it does define which methods you can use when working with stage data.
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
         * The player who owns this data.
         */
        private EntityPlayer player;

        @Override
        public Collection<String> getUnlockedStages () {

            return this.unlockedStages;
        }

        @Override
        public boolean hasUnlockedStage (String stage) {

            return this.unlockedStages.contains(stage.toLowerCase());
        }

        @Override
        public void unlockStage (String stage) {

            this.unlockedStages.add(stage.toLowerCase());
        }

        @Override
        public void lockStage (String stage) {

            this.unlockedStages.remove(stage.toLowerCase());
        }

        @Override
        public void setPlayer (EntityPlayer player) {

            this.player = player;
        }

        @Override
        public EntityPlayer getPlayer () {

            return this.player;
        }
    }

    /**
     * This class handles the read/write of NBT in capabilities. Another internal class. It also
     * handles the IAdditionalStageData hooks which you can use!
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

            for (final Entry<String, IAdditionalStageData> handler : ADDITIONAL_DATA.entrySet()) {

                final NBTTagCompound handlerTag = new NBTTagCompound();
                handler.getValue().writeNBT(instance.getPlayer(), instance, handlerTag);
                tag.setTag(handler.getKey(), handlerTag);
            }

            return tag;
        }

        @Override
        public void readNBT (Capability<IStageData> capability, IStageData instance, EnumFacing side, NBTBase nbt) {

            final NBTTagCompound tag = (NBTTagCompound) nbt;

            final NBTTagList tagList = tag.getTagList("UnlockedStages", NBT.TAG_STRING);

            for (int index = 0; index < tagList.tagCount(); index++) {
                instance.unlockStage(tagList.getStringTagAt(index));
            }

            for (final Entry<String, IAdditionalStageData> handler : ADDITIONAL_DATA.entrySet()) {

                handler.getValue().readNBT(instance.getPlayer(), instance, tag.getCompoundTag(handler.getKey()));
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

    /**
     * The backing interface for additional stage data. Can be used, along with
     * {@link PlayerDataHandler#registerDataHandler(String, IAdditionalStageData)} and
     * {@link PlayerDataHandler#getDataHandler(String)} to have custom player nbt data.
     */
    public interface IAdditionalStageData {

        /**
         * Called when the player's data is being written.
         *
         * @param player The player whos's being saved.
         * @param stageData The stage data.
         * @param tag An NBTTagCompound which is specific to this handler.
         */
        void writeNBT (@Nonnull EntityPlayer player, @Nonnull IStageData stageData, @Nonnull NBTTagCompound tag);

        /**
         * Called when the player's data is being read.
         *
         * @param player The player who's being read.
         * @param stageData The stage data.
         * @param tag An NBTTagCompound which is specific to this handler. It can be null if
         *        {@link #writeNBT(EntityPlayer, IStageData, NBTTagCompound)} was not called yet.
         */
        void readNBT (@Nonnull EntityPlayer player, @Nonnull IStageData stageData, NBTTagCompound tag);

        /**
         * Called when the packet is sent to the client. Allows you to do packety stuff.
         *
         * @param player The player who's being synced.
         * @param stageName The name of the stage being synced. This can be empty!
         * @param isUnlocking Whether or not the stage is being unlocked.
         */
        void onClientSync (@Nonnull EntityPlayer player, @Nonnull String stageName, boolean isUnlocking);
    }
}