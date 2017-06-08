package net.darkhax.gamestages.commands;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandCheckStage extends CommandBase {

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

            if (!PlayerDataHandler.getStageData(player).hasUnlockedStage(stage)) {

                throw new CommandException("commands.gamestage.check.failure", new Object[] { player.getDisplayName(), stage });
            }

            notifyCommandListener(sender, this, "commands.gamestage.check.success", new Object[] { player.getDisplayName(), stage });
        }

        else {

            throw new WrongUsageException("commands.gamestage.check.usage", new Object[0]);
        }
    }
}