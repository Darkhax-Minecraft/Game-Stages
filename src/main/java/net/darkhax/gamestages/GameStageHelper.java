package net.darkhax.gamestages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.packet.MessageStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.DistExecutor;

public class GameStageHelper {

    /**
     * A predicate that can be used to validate if a stage is valid.
     */
    private static final Predicate<String> STAGE_PATTERN = Pattern.compile("^[a-z0-9_:]*$").asPredicate();

    /**
     * Checks if a given string is valid as a stage name.
     *
     * @param stageName The name to test.
     * @return Whether or not the name is valid.
     */
    public static boolean isValidStageName (String stageName) {

        return stageName.length() <= 64 && STAGE_PATTERN.test(stageName);
    }

    /**
     * Gets all the stages that are known to GameStages.
     *
     * @return All the known stages.
     */
    public static Set<String> getKnownStages () {

        return GameStageSaveHandler.getKnownStages();
    }

    /**
     * Checks if a stage is known to GameStages.
     *
     * @param stage The name of the stage to check.
     * @return Whether or not the stage is known.
     */
    public static boolean isStageKnown (String stage) {

        return GameStageSaveHandler.isStageKnown(stage);
    }

    /**
     * Checks if a player has a stage. This will also fire the check event which can be used to
     * change the result.
     *
     * @param player The player to check the stages of.
     * @param stage The stage to look for.
     * @return Whether or not the player has access to this stage.
     */
    public static boolean hasStage (EntityPlayer player, String stage) {

        return hasStage(player, getPlayerData(player), stage);
    }

    /**
     * Checks if a player has any of the passed stages. This will also fire the check event for
     * each stage.
     *
     * @param player The player to check the stages of.
     * @param stages The stages to look for.
     * @return Whether or not the player has at least one of the stages.
     */
    public static boolean hasAnyOf (EntityPlayer player, String... stages) {

        return hasAnyOf(player, getPlayerData(player), stages);
    }

    /**
     * Checks if a player has any of the passed stages. This will also fire the check event for
     * each stage.
     *
     * @param player The player to check the stages of.
     * @param stages The stages to look for.
     * @return Whether or not the player has at least one of the stages.
     */
    public static boolean hasAnyOf (EntityPlayer player, Collection<String> stages) {

        return hasAnyOf(player, getPlayerData(player), stages);
    }

    /**
     * Checks if a player data has any of the passed stages. This will also fire the check
     * event for each stage.
     *
     * @param player The player to check the stages of.
     * @param playerData The player data to check.
     * @param stages The stages to look for.
     * @return Whether or not the player has at least one of the stages.
     */
    public static boolean hasAnyOf (EntityPlayer player, IStageData playerData, Collection<String> stages) {

        return stages.stream().anyMatch(stage -> hasStage(player, playerData, stage));
    }

    /**
     * Checks if a player data has any of the passed stages. This will also fire the check
     * event for each stage.
     *
     * @param player The player to check the stages of.
     * @param playerData The player data to check.
     * @param stages The stages to look for.
     * @return Whether or not the player has at least one of the stages.
     */
    public static boolean hasAnyOf (EntityPlayer player, IStageData playerData, String... stages) {

        return Arrays.stream(stages).anyMatch(stage -> hasStage(player, playerData, stage));
    }

    /**
     * Checks if a player has all of the passed stages. This will also fire the check event for
     * each stage.
     *
     * @param player The player to check the stages of.
     * @param stages The stages to look for.
     * @return Whether or not the player has all of the passed stages.
     */
    public static boolean hasAllOf (EntityPlayer player, String... stages) {

        return hasAllOf(player, getPlayerData(player), stages);
    }

    /**
     * Checks if a player has all of the passed stages. This will also fire the check event for
     * each stage.
     *
     * @param player The player to check the stages of.
     * @param stages The stages to look for.
     * @return Whether or not the player has all of the passed stages.
     */
    public static boolean hasAllOf (EntityPlayer player, Collection<String> stages) {

        return hasAllOf(player, getPlayerData(player), stages);
    }

    /**
     * Checks if a player data has all of the passed stages. This will also fire the check
     * event for each stage.
     *
     * @param player The player to check the stages of.
     * @param playerData The player data to check.
     * @param stages The stages to look for.
     * @return Whether or not the player has all of the passed stages.
     */
    public static boolean hasAllOf (EntityPlayer player, IStageData playerData, Collection<String> stages) {

        return stages.stream().allMatch(stage -> hasStage(player, playerData, stage));
    }

    /**
     * Checks if a player data has all of the passed stages. This will also fire the check
     * event for each stage.
     *
     * @param player The player to check the stages of.
     * @param playerData The player data to check.
     * @param stages The stages to look for.
     * @return Whether or not the player has all of the passed stages.
     */
    public static boolean hasAllOf (EntityPlayer player, IStageData playerData, String... stages) {

        return Arrays.stream(stages).allMatch(stage -> hasStage(player, playerData, stage));
    }

    /**
     * Internal helper method to fire a stage check. It's used to reduce the total amount of
     * code needed for the event firing, and improve performance for multiple stage lookups.
     *
     * @param player The player to check the stages of.
     * @param data The player's stage data. This is to allow multiple stage checks to look this
     *        up only once.
     * @param stage The stage to check for.
     * @return Whether or not the player has the stage.
     */
    public static boolean hasStage (EntityPlayer player, IStageData data, String stage) {

        if (data != null) {

            final GameStageEvent.Check event = new GameStageEvent.Check(player, stage, data.hasStage(stage));
            MinecraftForge.EVENT_BUS.post(event);
            return event.hasStage();
        }

        return false;
    }

    /**
     * Adds a stage to a player. This will fire the add event which can be canceled, and the
     * added event if the stage is added successfully.
     *
     * @param player The player to add the stage too.
     * @param stage The stage to add.
     */
    public static void addStage (EntityPlayer player, String stage) {

        if (!MinecraftForge.EVENT_BUS.post(new GameStageEvent.Add(player, stage))) {

            getPlayerData(player).addStage(stage);
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Added(player, stage));
        }
    }

    /**
     * Removes a stages from a player. This will fire the remove event which can be canceled,
     * and the removed event if the stage is removed successfully.
     *
     * @param player The player to remove the stage from.
     * @param stage The stage to remove.
     */
    public static void removeStage (EntityPlayer player, String stage) {

        if (!MinecraftForge.EVENT_BUS.post(new GameStageEvent.Remove(player, stage))) {

            getPlayerData(player).removeStage(stage);
            MinecraftForge.EVENT_BUS.post(new GameStageEvent.Removed(player, stage));
        }
    }

    /**
     * Removes all stages that a player has unlocked.
     *
     * @param player The player to remove stages from.
     * @return The amount of stages the player had before they were removed.
     */
    public static int clearStages (EntityPlayerMP player) {

        final IStageData stageInfo = GameStageHelper.getPlayerData(player);
        final int stageCount = stageInfo.getStages().size();
        stageInfo.clear();
        MinecraftForge.EVENT_BUS.post(new GameStageEvent.Cleared(player, stageInfo));
        return stageCount;
    }

    /**
     * Gets the player data for an EntityPlayer. This will resolve fake players to their
     * special data as well. If called on the client you will get the client data for the
     * player.
     *
     * @param player The player to get the data of.
     * @return The stage data for the player.
     */
    public static IStageData getPlayerData (EntityPlayer player) {

        if (player == null) {

            return GameStageSaveHandler.EMPTY_STAGE_DATA;
        }

        else if (player instanceof FakePlayer) {

            return GameStageSaveHandler.getFakeData(player.getName().getString());
        }

        else if (player instanceof EntityPlayerMP) {

            return GameStageSaveHandler.getPlayerData(player.getUniqueID());
        }

        return DistExecutor.callWhenOn(Dist.CLIENT, () -> GameStageSaveHandler::getClientData);
    }

    /**
     * A generic version of {@link #syncPlayer(EntityPlayerMP)}. It still only works for
     * multiplayer, but allows some code to be cleaner.
     *
     * @param player The player to sync.
     */
    public static void syncPlayer (EntityPlayer player) {

        if (player instanceof EntityPlayerMP) {

            syncPlayer((EntityPlayerMP) player);
        }
    }

    /**
     * Syncs a client's data with the data that is on the server. This can only be called
     * server side.
     *
     * @param player The player to sync.
     */
    public static void syncPlayer (EntityPlayerMP player) {

        final IStageData info = GameStageHelper.getPlayerData(player);

        if (info != null) {

            GameStages.LOG.debug("Syncing {} stages for {}.", info.getStages().size(), player.getName());
            GameStages.NETWORK.sendToPlayer(player, new MessageStages(info.getStages()));
        }
    }
}