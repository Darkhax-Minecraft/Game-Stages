package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.packet.PacketStage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandAddStage extends Command {

    @Override
    public String getName () {

        return "add";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.add.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 2) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);
            final String stageName = args[1];

            GameStageHelper.addStage(player, stageName);

            if (player instanceof EntityPlayerMP) {
                GameStages.NETWORK.sendTo(new PacketStage(stageName, true), (EntityPlayerMP) player);
            }

            player.sendMessage(new TextComponentTranslation("commands.gamestage.add.target", stageName));

            if (player != sender) {
                sender.sendMessage(new TextComponentTranslation("commands.gamestage.add.sender", stageName, player.getDisplayNameString()));
            }
        }
        else {
            throw new WrongUsageException("commands.gamestage.add.usage");
        }
    }

    @Override
    public boolean isUsernameIndex (String[] args, int index) {

        return index == 0;
    }
}