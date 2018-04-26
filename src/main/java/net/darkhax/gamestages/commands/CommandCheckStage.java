package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandCheckStage extends Command {

    @Override
    public String getName () {

        return "check";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.check.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 2) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);
            final String stage = args[1];

            if (!GameStageHelper.hasStage(player, stage)) {
                throw new CommandException("commands.gamestage.check.failure", player.getDisplayName(), stage);
            }

            notifyCommandListener(sender, this, "commands.gamestage.check.success", player.getDisplayName(), stage);
        }
        else {
            throw new WrongUsageException("commands.gamestage.check.usage");
        }
    }
}