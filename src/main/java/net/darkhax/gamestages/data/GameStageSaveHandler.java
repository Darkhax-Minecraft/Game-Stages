package net.darkhax.gamestages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.io.Files;
import com.google.gson.Gson;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.GameStages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "gamestages")
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
     * A set of all the stages that are known to GameStages. This is used for things like validation, auto complete, and
     * give all commands.
     */
    private static final Set<String> KNOWN_STAGES = new HashSet<>();

    /**
     * The file to load fake player data from.
     */
    private static final File FAKE_PLAYER_STAGE_FILE = new File("config/gamestages/fake_players.json");

    /**
     * The file to load known stages from.
     */
    private static final File KNOWN_STAGES_FILE = new File("config/gamestages/known_stages.json");

    /**
     * Reusable instance of Gson for reading and writing json files.
     */
    private static final Gson GSON = new Gson();

    /**
     * A reference to the client's current stage data. This will be overridden every time the player joins a save
     * instance.
     */
    @OnlyIn(Dist.CLIENT)
    private static IStageData clientData;

    /**
     * Hook for the player LoadFromFile event. Allows game stage data to be loaded when the player's data is loaded.
     *
     * @param event The forge event.
     */
    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {

        final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
        final IStageData playerData = new StageData();

        if (playerFile.exists()) {

            try {

                final CompoundTag tag = NbtIo.readCompressed(playerFile);
                playerData.readFromNBT(tag);
                GameStages.LOG.debug("Loaded {} stages for {}.", playerData.getStages().size(), event.getEntity().getName());
            }

            catch (final IOException e) {

                GameStages.LOG.error("Could not read player data for {}.", event.getEntity().getName());
                GameStages.LOG.catching(e);
            }
        }

        GLOBAL_STAGE_DATA.put(event.getEntity().getUUID(), playerData);
    }

    /**
     * Hook for the player SaveToFile event. Allows game stage data to be saved when the player's data is saved.
     *
     * @param event The Forge event.
     */
    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {

        final UUID playerUUID = event.getEntity().getUUID();

        if (GLOBAL_STAGE_DATA.containsKey(playerUUID)) {

            final IStageData playerData = getPlayerData(playerUUID);
            final File playerFile = getPlayerFile(event.getPlayerDirectory(), event.getPlayerUUID());
            final CompoundTag tag = playerData.writeToNBT();

            if (tag != null) {

                try {

                    NbtIo.writeCompressed(tag, playerFile);
                    GameStages.LOG.debug("Saved {} stages for {}.", playerData.getStages().size(), event.getEntity().getName());
                }

                catch (final IOException e) {

                    GameStages.LOG.error("Could not write player data for {}.", playerFile.getName());
                    GameStages.LOG.catching(e);
                }
            }
        }
    }

    /**
     * Hook for the PlayerLoggedInEvent. If the player is a valid server side player, their data will be synced to the
     * client.
     *
     * @param event The Forge event.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {

        // When a player connects to the server, sync their client data with the
        // server's data.
        if (event.getEntity() instanceof ServerPlayer) {

            GameStageHelper.syncPlayer((ServerPlayer) event.getEntity());
        }
    }

    /**
     * Looks up a players stage data. This should only be used with real players, fake players use
     * {@link #getFakeData(String)}. Alternatively, the {@link GameStageHelper#getPlayerData(Player)} can be used to
     * automatically resolve a player.
     *
     * @param uuid The uuid of the player to lookup.
     * @return The stage data for the player. If one does not exist, it will be created.
     */
    @Nullable
    public static IStageData getPlayerData(UUID uuid) {

        return GLOBAL_STAGE_DATA.get(uuid);
    }

    /**
     * Gets a gamestage save file for a player.
     *
     * @param playerDir The instance specific save folder for player data.
     * @param uuid      The uuid of the player to get a file for.
     * @return The save file to use for the player.
     */
    private static File getPlayerFile(File playerDir, String uuid) {

        final File saveDir = new File(playerDir, "gamestages");

        if (!saveDir.exists()) {

            saveDir.mkdirs();
        }

        return new File(saveDir, uuid + ".dat");
    }

    /**
     * Reloads fake player data from the fake player json file.
     */
    public static void reloadFakePlayers() {

        GameStages.LOG.debug("Reloading fakeplayers stage data from {}.", FAKE_PLAYER_STAGE_FILE.getName());

        if (!FAKE_PLAYER_STAGE_FILE.getParentFile().exists()) {

            FAKE_PLAYER_STAGE_FILE.getParentFile().mkdirs();
        }

        FAKE_STAGE_DATA.clear();

        if (FAKE_PLAYER_STAGE_FILE.exists()) {

            try (BufferedReader reader = Files.newReader(FAKE_PLAYER_STAGE_FILE, StandardCharsets.UTF_8)) {

                final FakePlayerData[] fakePlayers = GSON.fromJson(reader, FakePlayerData[].class);

                if (fakePlayers != null) {

                    Arrays.stream(fakePlayers).forEach(GameStageSaveHandler::addFakePlayer);
                }

                else {

                    GameStages.LOG.error("Your fake player stages file is incorrect. Expected an array of fake player entries.");
                }
            }

            catch (final Exception e) {

                GameStages.LOG.error("Could not read {}.", FAKE_PLAYER_STAGE_FILE.getName());
                GameStages.LOG.catching(e);
            }
        }
    }

    /**
     * Reloads all the known player data.
     */
    public static void reloadKnownStages() {

        GameStages.LOG.debug("Reloading known stages data from {}.", KNOWN_STAGES_FILE.getName());

        if (!KNOWN_STAGES_FILE.getParentFile().exists()) {

            KNOWN_STAGES_FILE.getParentFile().mkdirs();
        }

        KNOWN_STAGES.clear();

        if (KNOWN_STAGES_FILE.exists()) {

            try (BufferedReader reader = Files.newReader(KNOWN_STAGES_FILE, StandardCharsets.UTF_8)) {

                final String[] knownStages = GSON.fromJson(reader, String[].class);

                if (knownStages != null) {

                    for (final String stageName : knownStages) {

                        if (GameStageHelper.isValidStageName(stageName)) {

                            KNOWN_STAGES.add(stageName);
                        }

                        else {

                            GameStages.LOG.error("Rejected an invalid stage name of {}. It will not be usable. Stage names must be under 64 characters and may only include alphanumeric characters, underscores, and colons.", stageName);
                        }
                    }
                }

                else {

                    GameStages.LOG.error("Your known stages file is incorrect! The file must be a string array!");
                }

                GameStages.LOG.debug("Loaded {} known stages.", KNOWN_STAGES.size());
            }

            catch (final Exception e) {

                GameStages.LOG.error("Could not read {}.", KNOWN_STAGES_FILE.getName());
                GameStages.LOG.catching(e);
            }
        }
    }

    /**
     * Checks if the fake player is in the fake stage map.
     *
     * @param fakePlayerName The fake player name to check for.
     * @return Whether or not the fake player exists.
     */
    public static boolean hasFakePlayer(String fakePlayerName) {

        return FAKE_STAGE_DATA.containsKey(fakePlayerName);
    }

    /**
     * WARNING: This method has the potential to cause all sorts of issues. Please do not use it unless you're
     * absolutely sure you know what you're doing.
     * <p>
     * Adds fake player data to the fake stage map.
     *
     * @param data The fake player data to take into account.
     */
    public static void addFakePlayer(FakePlayerData data) {

        FAKE_STAGE_DATA.put(data.getFakePlayerName(), data);
        GameStages.LOG.debug("Adding fakeplayer {} with gamestages {}", data.getFakePlayerName(), data.getStages());
    }

    /**
     * WARNING: This method has the potential to cause all sorts of issues. Please do not use it unless you're
     * absolutely sure you know what you're doing.
     * <p>
     * Removes fake player data to the fake stage map.
     *
     * @param fakePlayerName The fake player name to remove.
     */
    public static void removeFakePlayer(String fakePlayerName) {

        final FakePlayerData removedData = FAKE_STAGE_DATA.remove(fakePlayerName);

        if (removedData != null) {
            GameStages.LOG.debug("Removing fakeplayer {} with gamestages {}", fakePlayerName, removedData.getStages());
        }
    }

    /**
     * Gets data for a fake player. Real players should use {@link #getPlayerData(UUID)} Alternatively
     * {@link GameStageHelper#getPlayerData(Player)} can be used to automatically resolve players.
     *
     * @param fakePlayerName The name of the fake player.
     * @return The fake players stage data, or the default value if one does not exist.
     */
    public static IStageData getFakeData(String fakePlayerName) {

        return FAKE_STAGE_DATA.getOrDefault(fakePlayerName, FakePlayerData.DEFAULT);
    }

    /**
     * Gets all the stages that are known to GameStages.
     *
     * @return All the known stages.
     */
    public static Set<String> getKnownStages() {

        return KNOWN_STAGES;
    }

    /**
     * Checks if a stage is known to GameStages.
     *
     * @param stage The name of the stage to check.
     * @return Whether or not the stage is known.
     */
    public static boolean isStageKnown(String stage) {

        return KNOWN_STAGES.contains(stage);
    }

    /**
     * Gets the game stage data of the current player that has been synced to the client.
     *
     * @return The current client side stage data.
     */
    @OnlyIn(Dist.CLIENT)
    public static IStageData getClientData() {

        return clientData;
    }

    /**
     * Sets the client's synced stage data.
     *
     * @param stageData The stage data for the client.
     */
    @OnlyIn(Dist.CLIENT)
    public static void setClientData(IStageData stageData) {

        clientData = stageData;
    }
}
