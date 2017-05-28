package net.darkhax.gamestages.commands;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandStage extends CommandBase {

    @Override
    public String getName () {

        return "gamestage";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 3) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);

            if (args[1].equalsIgnoreCase("add")) {

                PlayerDataHandler.getStageData(player).unlockStage(args[2]);
            }

            else if (args[1].equalsIgnoreCase("remove")) {

                PlayerDataHandler.getStageData(player).lockStage(args[2]);
            }
        }
    }
}