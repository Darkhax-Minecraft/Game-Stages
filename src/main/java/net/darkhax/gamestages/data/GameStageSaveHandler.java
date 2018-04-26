package net.darkhax.gamestages.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        
        // When a player connects to the server, sync their client data with the server's data.
        if(event.player instanceof EntityPlayerMP) {
            
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
}