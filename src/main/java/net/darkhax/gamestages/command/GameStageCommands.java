package net.darkhax.gamestages.command;

import java.util.stream.Collectors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

public class GameStageCommands {

    public GameStageCommands (CommandDispatcher<CommandSource> dispatcher) {

        final LiteralArgumentBuilder<CommandSource> root = Commands.literal("gamestage");
        root.then(createSilentStageCommand("add", 2, ctx -> changeStages(ctx, false, true), ctx -> changeStages(ctx, true, true)));
        root.then(createSilentStageCommand("remove", 2, ctx -> changeStages(ctx, false, false), ctx -> changeStages(ctx, true, false)));
        root.then(createPlayerCommand("info", 0, ctx -> getStageInfo(ctx, true), ctx -> getStageInfo(ctx, false)));
        root.then(createPlayerCommand("clear", 2, ctx -> clearStages(ctx, true), ctx -> clearStages(ctx, false)));
        root.then(Commands.literal("reloadfakes").requires(sender -> sender.hasPermissionLevel(2)).executes(GameStageCommands::reloadFakePlayers));
        root.then(createPlayerStageCommand("check", 2, ctx -> checkStage(ctx, true), ctx -> checkStage(ctx, false)));
        dispatcher.register(root);
    }

    private static LiteralArgumentBuilder<CommandSource> createPlayerCommand (String key, int permissions, Command<CommandSource> command, Command<CommandSource> commandNoPlayer) {

        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).executes(commandNoPlayer).then(Commands.argument("targets", EntityArgument.player()).executes(command));
    }

    private static LiteralArgumentBuilder<CommandSource> createSilentStageCommand (String key, int permissions, Command<CommandSource> command, Command<CommandSource> silent) {

        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.word()).executes(command).then(Commands.argument("silent", BoolArgumentType.bool()).executes(silent))));
    }

    private static LiteralArgumentBuilder<CommandSource> createPlayerStageCommand (String key, int permissions, Command<CommandSource> command, Command<CommandSource> commandNoPlayer) {

        return Commands.literal(key).requires(sender -> sender.hasPermissionLevel(permissions)).then(Commands.argument("stage", StringArgumentType.word()).executes(commandNoPlayer)).then(Commands.argument("targets", EntityArgument.player()).then(Commands.argument("stage", StringArgumentType.word()).executes(command)));
    }

    private static int clearStages (CommandContext<CommandSource> ctx, boolean hasPlayer) throws CommandSyntaxException {

        if (hasPlayer) {

            for (final EntityPlayerMP player : EntityArgument.getPlayers(ctx, "targets")) {

                clearStages(ctx, player);
            }
        }

        else {

            clearStages(ctx, ctx.getSource().asPlayer());
        }

        return 0;
    }

    private static int checkStage (CommandContext<CommandSource> ctx, boolean hasPlayer) throws CommandSyntaxException {

        if (hasPlayer) {

            for (final EntityPlayerMP player : EntityArgument.getPlayers(ctx, "targets")) {

                checkStage(ctx, player);
            }
        }

        else {

            checkStage(ctx, ctx.getSource().asPlayer());
        }

        return 0;
    }

    private static boolean checkStage (CommandContext<CommandSource> ctx, EntityPlayerMP player) {

        final String stage = StringArgumentType.getString(ctx, "stage");
        final boolean hasStage = GameStageHelper.hasStage(player, stage);
        ctx.getSource().sendFeedback(new TextComponentTranslation(hasStage ? "commands.gamestage.check.success" : "commands.gamestage.check.failure", player.getDisplayName(), stage), false);

        return hasStage;
    }

    private static int reloadFakePlayers (CommandContext<CommandSource> ctx) {

        GameStageSaveHandler.reloadFakePlayers();
        ctx.getSource().sendFeedback(new TextComponentTranslation("commands.gamestage.reloadfakes.info"), true);
        return 0;
    }

    private static void clearStages (CommandContext<CommandSource> ctx, EntityPlayerMP player) {

        final int removedStages = GameStageHelper.clearStages(player);

        player.sendMessage(new TextComponentTranslation("commands.gamestage.clear.target", removedStages));

        if (player != ctx.getSource().getEntity()) {
            ctx.getSource().sendFeedback(new TextComponentTranslation("commands.gamestage.clear.sender", removedStages, player.getDisplayName()), true);
        }
    }

    private static int getStageInfo (CommandContext<CommandSource> ctx, boolean hasPlayer) throws CommandSyntaxException {

        if (hasPlayer) {

            for (final EntityPlayerMP player : EntityArgument.getPlayers(ctx, "targets")) {

                getStageInfo(ctx, player);
            }
        }

        else {

            getStageInfo(ctx, ctx.getSource().asPlayer());
        }

        return 0;
    }

    private static void getStageInfo (CommandContext<CommandSource> ctx, EntityPlayerMP player) {

        final String stageInfo = GameStageHelper.getPlayerData(player).getStages().stream().map(Object::toString).collect(Collectors.joining(", "));

        if (stageInfo.isEmpty()) {

            ctx.getSource().sendFeedback(new TextComponentTranslation("commands.gamestage.info.empty", player.getDisplayName()), false);
        }

        else {

            ctx.getSource().sendFeedback(new TextComponentTranslation("commands.gamestage.info.stages", player.getDisplayName(), stageInfo), false);
        }
    }

    private static int changeStages (CommandContext<CommandSource> ctx, boolean silent, boolean adding) throws CommandSyntaxException {

        final String stageName = StringArgumentType.getString(ctx, "stage");

        for (final EntityPlayerMP player : EntityArgument.getPlayers(ctx, "targets")) {

            if (adding) {

                GameStageHelper.addStage(player, stageName);
            }

            else {

                GameStageHelper.removeStage(player, stageName);
            }

            GameStageHelper.syncPlayer(player);

            if (!silent || !BoolArgumentType.getBool(ctx, "silent")) {

                player.sendMessage(new TextComponentTranslation(adding ? "commands.gamestage.add.target" : "commands.gamestage.remove.target", stageName));

                if (player != ctx.getSource().getEntity()) {
                    ctx.getSource().sendFeedback(new TextComponentTranslation(adding ? "commands.gamestage.add.sender" : "commands.gamestage.remove.sender", stageName, player.getDisplayName()), true);
                }
            }
        }

        return 0;
    }
}