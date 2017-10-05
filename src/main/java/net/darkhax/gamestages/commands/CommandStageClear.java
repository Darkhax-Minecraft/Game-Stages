package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.darkhax.gamestages.packet.PacketStage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandStageClear extends Command {

    @Override
    public String getName () {

        return "clear";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.clear.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 1) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);
            final IStageData stageInfo = PlayerDataHandler.getStageData(player);
            final int stageCount = stageInfo.getUnlockedStages().size();

            for (final String stage : stageInfo.getUnlockedStages()) {
                if (player instanceof EntityPlayerMP) {
                    GameStages.NETWORK.sendTo(new PacketStage(stage, false), (EntityPlayerMP) player);
                }
            }
            
            stageInfo.clear();

            player.sendMessage(new TextComponentTranslation("commands.gamestage.clear.target", stageCount));

            if (player != sender) {
                sender.sendMessage(new TextComponentTranslation("commands.gamestage.clear.sender", stageCount, player.getDisplayNameString()));
            }
        }
        else {
            throw new WrongUsageException("commands.gamestage.clear.usage", new Object[0]);
        }
    }

    @Override
    public boolean isUsernameIndex (String[] args, int index) {

        return index == 0;
    }
}