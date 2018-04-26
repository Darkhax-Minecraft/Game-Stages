package net.darkhax.gamestages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import net.darkhax.bookshelf.util.NBTUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.GameStages;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class GameStageSaveHandler {

    private static final Map<String, IStageData> GLOBAL_STAGE_DATA = new HashMap<>();

    @SideOnly(Side.CLIENT)
    public static IStageData clientData;

    @SubscribeEvent
    public static void onPlayerLoad (PlayerEvent.LoadFromFile event) {

        final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
        final IStageData playerData = new StageData();

        if (playerFile.exists()) {

            try {

                final NBTTagCompound tag = CompressedStreamTools.read(playerFile);
                playerData.readFromNBT(tag);
            }

            catch (final IOException e) {

                GameStages.LOG.error("Could not read player data for {}.", playerFile.getName());
                GameStages.LOG.catching(e);
            }
        }

        handleLegacyData(event.getPlayerDirectory(), event.getPlayerUUID(), playerData);

        GLOBAL_STAGE_DATA.put(event.getPlayerUUID(), playerData);
    }

    @SubscribeEvent
    public static void onPlayerSave (PlayerEvent.SaveToFile event) {

        if (GLOBAL_STAGE_DATA.containsKey(event.getPlayerUUID())) {

            final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
            final NBTTagCompound tag = GLOBAL_STAGE_DATA.get(event.getPlayerUUID()).writeToNBT();

            if (tag != null) {

                try {

                    CompressedStreamTools.write(tag, playerFile);
                }

                catch (final IOException e) {

                    GameStages.LOG.error("Could not write player data for {}.", playerFile.getName());
                    GameStages.LOG.catching(e);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {

        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {

            GameStageHelper.syncPlayer((EntityPlayerMP) event.player);
        }
    }

    public static IStageData getPlayerData (String uuid) {

        return GLOBAL_STAGE_DATA.computeIfAbsent(uuid, playerUUID -> new StageData());
    }

    private static File getPlayerFile (File playerDir, String uuid) {

        final File saveDir = new File(playerDir, "gamestages");

        if (!saveDir.exists()) {

            saveDir.mkdirs();
        }

        return new File(saveDir, uuid + ".dat");
    }

    private static void handleLegacyData (File playerDir, String uuid, IStageData modern) {

        final File mainDataFile = new File(playerDir, uuid + ".dat");

        try (FileInputStream mainDataStream = new FileInputStream(mainDataFile)) {

            final NBTTagCompound mainData = CompressedStreamTools.readCompressed(mainDataStream);

            if (mainData != null && mainData.hasKey("ForgeCaps")) {

                final NBTTagCompound forgeCaps = mainData.getCompoundTag("ForgeCaps");

                if (forgeCaps != null && forgeCaps.hasKey("gamestages:playerdata")) {

                    final NBTTagCompound legacyData = forgeCaps.getCompoundTag("gamestages:playerdata");

                    if (legacyData != null && legacyData.hasKey("UnlockedStages")) {

                        final Collection<String> legacyStages = NBTUtils.readCollection(new ArrayList<>(), legacyData.getTagList("UnlockedStages", NBT.TAG_STRING), stage -> stage);

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
    
    private static final Map<String, FakePlayerData> fakePlayerData = new HashMap<>();
    private static final File fakePlayerDataFile = new File(new File("config"), "gameStagesFakePlayerData.json");
    private static final FakePlayerData DEFAULT = new FakePlayerData("DEFAULT", Collections.emptySet());
    private static final Gson gson = new Gson();

    public static void reloadFromFile () {

        GameStages.LOG.info("Reloading fakeplayers stage data from {}.", fakePlayerDataFile.getName());
        
        fakePlayerData.clear();
        
        if (!fakePlayerDataFile.exists()) {
            return;
        }

        try (BufferedReader reader = Files.newReader(fakePlayerDataFile, Charsets.UTF_8)){
            
            final FakePlayerData[] fakePlayers = gson.fromJson(reader, FakePlayerData[].class);
            Arrays.stream(fakePlayers).forEach(GameStageSaveHandler::addFakePlayer);
        }
        
        catch (final IOException e) {
            
            GameStages.LOG.error("Could not read {}.", fakePlayerDataFile.getName());
            GameStages.LOG.catching(e);
        }
    }

    private static void addFakePlayer (FakePlayerData data) {

        fakePlayerData.put(data.getFakePlayerName(), data);
        GameStages.LOG.info("Adding fakeplayer {} with gamestages {}", data.getFakePlayerName(), data.getStages());
    }

    public static IStageData getFakeData (String fakePlayerName) {

        return fakePlayerData.getOrDefault(fakePlayerName, DEFAULT);
    }
}