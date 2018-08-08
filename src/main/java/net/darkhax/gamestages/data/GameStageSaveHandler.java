package net.darkhax.gamestages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import net.darkhax.bookshelf.util.NBTUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.config.Configuration;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class GameStageSaveHandler {
    
    /**
     * A map of player uuid to stage data.
     */
    private static final Map<UUID, IStageData> GLOBAL_STAGE_DATA = new HashMap<>();
    
    /**
     * A map of fake player names to fake stage data.
     */
    private static final Map<String, FakePlayerData> FAKE_STAGE_DATA = new HashMap<>();
    
    /**
     * The file to load fake player data from.
     */
    private static final File FAKE_PLAYER_STAGE_FILE = new File(new File("config"), "gameStagesFakePlayerData.json");
    
    /**
     * Reusable instance of Gson for reading and writing json files.
     */
    private static final Gson GSON = new Gson();
    
    /**
     * A reference to the client's current stage data. This will be overridden every time the
     * player joins a save instance.
     */
    @SideOnly(Side.CLIENT)
    public static IStageData clientData;
    
    /**
     * Hook for the player LoadFromFile event. Allows game stage data to be loaded when the
     * player's data is loaded.
     */
    @SubscribeEvent
    public static void onPlayerLoad (PlayerEvent.LoadFromFile event) {
        
        final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
        final IStageData playerData = new StageData();
        
        if (playerFile.exists()) {
            
            try {
                
                final NBTTagCompound tag = CompressedStreamTools.read(playerFile);
                playerData.readFromNBT(tag);
                
                if (Configuration.debug.logDebug) {
                    
                    GameStages.LOG.info("Loaded {} stages for {}.", playerData.getStages().size(), event.getEntityPlayer().getName());
                }
            }
            
            catch (final IOException e) {
                
                GameStages.LOG.error("Could not read player data for {}.", event.getEntityPlayer().getName());
                GameStages.LOG.catching(e);
            }
        }
        
        else {
            
            handleLegacyData(event.getPlayerDirectory(), event.getPlayerUUID(), playerData);
        }
        
        GLOBAL_STAGE_DATA.put(event.getEntityPlayer().getPersistentID(), playerData);
    }
    
    /**
     * Hook for the player SaveToFile event. Allows game stage data to be saved when the
     * player's data is saved.
     */
    @SubscribeEvent
    public static void onPlayerSave (PlayerEvent.SaveToFile event) {
        
        final UUID playerUUID = event.getEntityPlayer().getPersistentID();
        
        if (GLOBAL_STAGE_DATA.containsKey(playerUUID)) {
            
            final IStageData playerData = getPlayerData(playerUUID);
            final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
            final NBTTagCompound tag = playerData.writeToNBT();
            
            if (tag != null) {
                
                try {
                    
                    CompressedStreamTools.write(tag, playerFile);
                    
                    if (Configuration.debug.logDebug) {
                        GameStages.LOG.info("Saved {} stages for {}.", playerData.getStages().size(), event.getEntityPlayer().getName());
                    }
                }
                
                catch (final IOException e) {
                    
                    GameStages.LOG.error("Could not write player data for {}.", playerFile.getName());
                    GameStages.LOG.catching(e);
                }
            }
        }
    }
    
    /**
     * Hook for the PlayerLoggedInEvent. If the player is a valid server side player, their
     * data will be synced to the client.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {
        
        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {
            
            GameStageHelper.syncPlayer((EntityPlayerMP) event.player);
        }
    }
    
    /**
     * Hook for the PlayerLoggedInEvent. If the player is a valid server side player, their
     * data will be synced to the client.
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut (PlayerLoggedOutEvent event) {
        
        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {
            
            GLOBAL_STAGE_DATA.remove(event.player.getPersistentID());
        }
    }
    
    /**
     * Looks up a players stage data. This should only be used with real players, fake players
     * use {@link #getFakeData(String)}. Alternatively, the
     * {@link GameStageHelper#getPlayerData(net.minecraft.entity.player.EntityPlayer)} can be
     * used to automatically resolve an EntityPlayer.
     *
     * @param uuid The uuid of the player to lookup.
     * @return The stage data for the player. If one does not exist, it will be created.
     */
    public static IStageData getPlayerData (UUID uuid) {
        
        return GLOBAL_STAGE_DATA.computeIfAbsent(uuid, playerUUID -> new StageData());
    }
    
    /**
     * Gets a gamestage save file for a player.
     *
     * @param playerDir The instance specific save folder for player data.
     * @param uuid The uuid of the player to get a file for.
     * @return The save file to use for the player.
     */
    private static File getPlayerFile (File playerDir, String uuid) {
        
        final File saveDir = new File(playerDir, "gamestages");
        
        if (!saveDir.exists()) {
            
            saveDir.mkdirs();
        }
        
        return new File(saveDir, uuid + ".dat");
    }
    
    /**
     * Handles legacy data from the previous capability system if the player has any.
     *
     * @param playerDir The instance specific player save directory.
     * @param uuid The uuid of the player.
     * @param modern The modern IStageData to populate with restored data.
     */
    private static void handleLegacyData (File playerDir, String uuid, IStageData modern) {
        
        final File mainDataFile = new File(playerDir, uuid + ".dat");
        
        // Check if the vanilla player dat file exists
        if (mainDataFile.exists()) {
            
            try (FileInputStream mainDataStream = new FileInputStream(mainDataFile)) {
                
                // Read vanilla player dat file to nbt tag
                final NBTTagCompound mainData = CompressedStreamTools.readCompressed(mainDataStream);
                
                // Look for forge caps tag
                if (mainData != null && mainData.hasKey("ForgeCaps")) {
                    
                    final NBTTagCompound forgeCaps = mainData.getCompoundTag("ForgeCaps");
                    
                    // Look for GameStage's cap tag
                    if (forgeCaps != null && forgeCaps.hasKey("gamestages:playerdata")) {
                        
                        final NBTTagCompound legacyData = forgeCaps.getCompoundTag("gamestages:playerdata");
                        
                        // Look for unlocked stages
                        if (legacyData != null && legacyData.hasKey("UnlockedStages")) {
                            
                            final Collection<String> legacyStages = NBTUtils.readCollection(new ArrayList<>(), legacyData.getTagList("UnlockedStages", NBT.TAG_STRING), stage -> stage);
                            
                            // Restore the stages
                            for (final String stage : legacyStages) {
                                
                                GameStages.LOG.info("Restoring legacy stage {} for player {}.", stage, uuid);
                                modern.addStage(stage);
                            }
                        }
                    }
                }
            }
            
            catch (final IOException e) {
                
                GameStages.LOG.error("Could not read main player data for {}.", mainDataFile.getName());
                GameStages.LOG.catching(e);
            }
        }
        
        else {
            
            if (Configuration.debug.logDebug) {
                
                GameStages.LOG.info("Could not find legacy data for {}.", uuid);
            }
        }
    }
    
    /**
     * Reloads fake player data from the fake player json file.
     */
    public static void reloadFakePlayers () {
        
        if (Configuration.debug.logDebug) {
            GameStages.LOG.info("Reloading fakeplayers stage data from {}.", FAKE_PLAYER_STAGE_FILE.getName());
        }
        
        FAKE_STAGE_DATA.clear();
        
        if (!FAKE_PLAYER_STAGE_FILE.exists()) {
            return;
        }
        
        try (BufferedReader reader = Files.newReader(FAKE_PLAYER_STAGE_FILE, Charsets.UTF_8)) {
            
            final FakePlayerData[] fakePlayers = GSON.fromJson(reader, FakePlayerData[].class);
            Arrays.stream(fakePlayers).forEach(GameStageSaveHandler::addFakePlayer);
        }
        
        catch (final IOException e) {
            
            GameStages.LOG.error("Could not read {}.", FAKE_PLAYER_STAGE_FILE.getName());
            GameStages.LOG.catching(e);
        }
    }

    /**
     * Checks if the fake player is in the fake stage map.
     *
     * @param fakePlayerName The fake player name to check for.
     */
    public static boolean hasFakePlayer(String fakePlayerName) {

        return FAKE_STAGE_DATA.containsKey(fakePlayerName);
    }
    
    /**
     * Adds fake player data to the fake stage map.
     *
     * @param data The fake player data to take into account.
     */
    public static void addFakePlayer (FakePlayerData data) {
        
        FAKE_STAGE_DATA.put(data.getFakePlayerName(), data);
        if (Configuration.debug.logDebug) {
            GameStages.LOG.info("Adding fakeplayer {} with gamestages {}", data.getFakePlayerName(), data.getStages());
        }
    }

    /**
     * Removes fake player data to the fake stage map.
     *
     * @param fakePlayerName The fake player name to remove.
     */
    public static void removeFakePlayer (String fakePlayerName) {

        FakePlayerData removedData = FAKE_STAGE_DATA.remove(fakePlayerName);
        if (removedData != null && Configuration.debug.logDebug) {
            GameStages.LOG.info("Removing fakeplayer {} with gamestages {}", fakePlayerName, removedData.getStages());
        }
    }
    
    /**
     * Gets data for a fake player. Real players should use {@link #getPlayerData(String)}.
     * Alternatively
     * {@link GameStageHelper#getPlayerData(net.minecraft.entity.player.EntityPlayer)} can be
     * used to automatically resolve players.
     *
     * @param fakePlayerName The name of the fake player.
     * @return The fake players stage data, or the default value if one does not exist.
     */
    public static IStageData getFakeData (String fakePlayerName) {
        
        return FAKE_STAGE_DATA.getOrDefault(fakePlayerName, FakePlayerData.DEFAULT);
    }
}
