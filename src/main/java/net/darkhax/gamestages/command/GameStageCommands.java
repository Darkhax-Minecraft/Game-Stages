package net.darkhax.gamestages.command;

import java.util.Collection;
import java.util.stream.Collectors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.darkhax.bookshelf.Constants;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;

public class GameStageCommands {

    public static void initializeCommands () {
        
        MinecraftForge.EVENT_BUS.addListener(GameStageCommands::registerCommands);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(GameStageCommands::registerArgs);
    }

    private static void registerArgs(RegisterEvent event) {

        if (event.getRegistryKey().equals(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY)) {

            event.register(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, new ResourceLocation(Constants.MOD_ID, "stages"), () -> ArgumentTypeInfos.registerByClass(StageArgumentType.class, StageArgumentType.SERIALIZERS));
        }
    }

    private static void registerCommands (RegisterCommandsEvent event) {
        
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("gamestage");
        root.then(createSilentStageCommand("add", 2, true));
        root.then(createSilentStageCommand("remove", 2, false));
        root.then(createPlayerCommand("info", 0, ctx -> getStageInfo(ctx, true), ctx -> getStageInfo(ctx, false)));
        root.then(createPlayerCommand("clear", 2, ctx -> clearStages(ctx, true), ctx -> clearStages(ctx, false)));
        root.then(createPlayerCommand("all", 2, ctx -> grantAll(ctx, true), ctx -> grantAll(ctx, false)));
        root.then(Commands.literal("reload").requires(sender -> sender.hasPermission(2)).executes(GameStageCommands::reloadGameStages));
        root.then(createPlayerStageCommand("check", 2, ctx -> checkStage(ctx, true), ctx -> checkStage(ctx, false)));
        
        event.getDispatcher().register(root);
    }
    
    private static LiteralArgumentBuilder<CommandSourceStack> createPlayerCommand (String key, int permissions, Command<CommandSourceStack> command, Command<CommandSourceStack> commandNoPlayer) {
        
        return Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).executes(commandNoPlayer).then(Commands.argument("targets", EntityArgument.player()).executes(command));
    }
    
    private static LiteralCommandNode<CommandSourceStack> createSilentStageCommand (String key, int permissions, boolean adding) {

        final LiteralCommandNode<CommandSourceStack> baseCommand = Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).build();
        final ArgumentCommandNode<CommandSourceStack, EntitySelector> targetsArg = Commands.argument("targets", EntityArgument.player()).build();
        final ArgumentCommandNode<CommandSourceStack, String> stageArg = Commands.argument("stage", new StageArgumentType()).executes(ctx -> changeStagesBase(ctx, adding)).build();
        final ArgumentCommandNode<CommandSourceStack, Boolean> silentArg = Commands.argument("silent", BoolArgumentType.bool()).executes(ctx -> changeStagesSilent(ctx, adding)).build();
        final ArgumentCommandNode<CommandSourceStack, Component> targetMessageArg = Commands.argument("message", ComponentArgument.textComponent()).executes(ctx -> changeStagesWithMessage(ctx, adding)).build();

        baseCommand.addChild(targetsArg);
        targetsArg.addChild(stageArg);
        stageArg.addChild(silentArg);
        stageArg.addChild(targetMessageArg);

        return baseCommand;
    }
    
    private static LiteralArgumentBuilder<CommandSourceStack> createPlayerStageCommand (String key, int permissions, Command<CommandSourceStack> command, Command<CommandSourceStack> commandNoPlayer) {

        return Commands.literal(key).requires(sender -> sender.hasPermission(permissions)).then(Commands.argument("stage", new StageArgumentType()).executes(commandNoPlayer)).then(Commands.argument("targets", EntityArgument.player()).then(Commands.argument("stage", new StageArgumentType()).executes(command)));
    }
    
    private static int grantAll (CommandContext<CommandSourceStack> ctx, boolean hasPlayer) throws CommandSyntaxException {
        
        if (hasPlayer) {
            
            for (final ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
                
                grantAll(ctx, player);
            }
        }
        
        else {
            
            grantAll(ctx, ctx.getSource().getPlayerOrException());
        }
        
        return 0;
    }
    
    private static void grantAll (CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        
        for (final String knownStage : GameStageHelper.getKnownStages()) {
            
            GameStageHelper.addStage(player, knownStage);
        }

        player.sendSystemMessage(Component.translatable("commands.gamestage.all.target"), false);
        
        if (player != ctx.getSource().getEntity()) {
            
            ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.all.sender", player.getDisplayName()), true);
        }
    }
    
    private static int clearStages (CommandContext<CommandSourceStack> ctx, boolean hasPlayer) throws CommandSyntaxException {
        
        if (hasPlayer) {
            
            for (final ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
                
                clearStages(ctx, player);
            }
        }
        
        else {
            
            clearStages(ctx, ctx.getSource().getPlayerOrException());
        }
        
        return 0;
    }
    
    private static void clearStages (CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        
        final int removedStages = GameStageHelper.clearStages(player);

        player.sendSystemMessage(Component.translatable("commands.gamestage.clear.target", removedStages), false);
        
        if (player != ctx.getSource().getEntity()) {
            ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.clear.sender", removedStages, player.getDisplayName()), true);
        }
    }
    
    private static int checkStage (CommandContext<CommandSourceStack> ctx, boolean hasPlayer) throws CommandSyntaxException {
        
        if (hasPlayer) {
            
            for (final ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
                
                checkStage(ctx, player);
            }
        }
        
        else {
            
            checkStage(ctx, ctx.getSource().getPlayerOrException());
        }
        
        return 0;
    }
    
    private static boolean checkStage (CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        
        final String stage = StageArgumentType.getStage(ctx, "stage");
        final boolean hasStage = GameStageHelper.hasStage(player, stage);
        ctx.getSource().sendSuccess(Component.translatable(hasStage ? "commands.gamestage.check.success" : "commands.gamestage.check.failure", player.getDisplayName(), stage), false);
        
        return hasStage;
    }
    
    private static int reloadGameStages (CommandContext<CommandSourceStack> ctx) {
        
        GameStageSaveHandler.reloadFakePlayers();
        ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.reloadfakes.info"), true);
        
        GameStageSaveHandler.reloadKnownStages();
        ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.reloadknown.info", GameStageSaveHandler.getKnownStages().size()), true);
        return 0;
    }
    
    private static int getStageInfo (CommandContext<CommandSourceStack> ctx, boolean hasPlayer) throws CommandSyntaxException {
        
        if (hasPlayer) {
            
            for (final ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
                
                getStageInfo(ctx, player);
            }
        }
        
        else {
            
            getStageInfo(ctx, ctx.getSource().getPlayerOrException());
        }
        
        return 0;
    }
    
    private static void getStageInfo (CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        
        final String stageInfo = GameStageHelper.getPlayerData(player).getStages().stream().map(Object::toString).collect(Collectors.joining(", "));
        
        if (stageInfo.isEmpty()) {
            
            ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.info.empty", player.getDisplayName()), false);
        }
        
        else {
            
            ctx.getSource().sendSuccess(Component.translatable("commands.gamestage.info.stages", player.getDisplayName(), stageInfo), false);
        }
    }

    private static int changeStages2 (CommandContext<CommandSourceStack> ctx, boolean silent, boolean adding) throws CommandSyntaxException {

        final Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
        final String stageName = StageArgumentType.getStage(ctx, "stage");
        final String senderMessageKey = "commands.gamestage." + (adding ? "add" : "remove") + ".sender";
        final String targetMessageKey = "commands.gamestage." + (adding ? "add" : "remove") + ".target";

        for (final ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {

            if (adding) {

                GameStageHelper.addStage(player, stageName);
            }

            else {

                GameStageHelper.removeStage(player, stageName);
            }

            if (!silent || !BoolArgumentType.getBool(ctx, "silent")) {

                player.sendSystemMessage(Component.translatable(targetMessageKey, stageName), false);

                if (player != ctx.getSource().getEntity()) {

                    ctx.getSource().sendSuccess(Component.translatable(senderMessageKey, stageName, player.getDisplayName()), true);
                }
            }
        }

        return 0;
    }

    private static int changeStagesBase (CommandContext<CommandSourceStack> ctx, boolean adding) throws CommandSyntaxException {

        final Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
        final String stageName = StageArgumentType.getStage(ctx, "stage");
        return changeStages(ctx.getSource(), players, stageName, false, adding, null);
    }

    private static int changeStagesSilent (CommandContext<CommandSourceStack> ctx, boolean adding) throws CommandSyntaxException {

        final Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
        final String stageName = StageArgumentType.getStage(ctx, "stage");
        final boolean isSilent = BoolArgumentType.getBool(ctx, "silent");
        return changeStages(ctx.getSource(), players, stageName, isSilent, adding, null);
    }

    private static int changeStagesWithMessage (CommandContext<CommandSourceStack> ctx, boolean adding) throws CommandSyntaxException {

        final Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
        final String stageName = StageArgumentType.getStage(ctx, "stage");
        final Component message = ComponentArgument.getComponent(ctx, "message");
        return changeStages(ctx.getSource(), players, stageName, false, adding, message);
    }
    
    private static int changeStages (CommandSourceStack sender, Collection<ServerPlayer> targetPlayers, String stageName, boolean silent, boolean adding, @Nullable Component targetMessage) {

        final String senderMessageKey = "commands.gamestage." + (adding ? "add" : "remove") + ".sender";
        final String targetMessageKey = "commands.gamestage." + (adding ? "add" : "remove") + ".target";
        
        for (final ServerPlayer targetPlayer : targetPlayers) {
            
            if (adding) {
                
                GameStageHelper.addStage(targetPlayer, stageName);
            }
            
            else {
                
                GameStageHelper.removeStage(targetPlayer, stageName);
            }
            
            if (!silent) {

                targetPlayer.sendSystemMessage(targetMessage != null ? targetMessage : Component.translatable(targetMessageKey, stageName), false);
                
                if (sender.getPlayer() != targetPlayer) {

                    sender.sendSuccess(Component.translatable(senderMessageKey, stageName, targetPlayer.getDisplayName()), true);
                }
            }
        }
        
        return 0;
    }
}