package net.darkhax.gamestages.commands;

import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.packet.PacketStage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandRemoveStage extends CommandBase {

    @Override
    public String getName () {

        return "remove";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.remove.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 2) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);
            final String stageName = args[1];

            PlayerDataHandler.getStageData(player).lockStage(stageName);

            if (player instanceof EntityPlayerMP) {

                GameStages.NETWORK.sendTo(new PacketStage(stageName, false), (EntityPlayerMP) player);
            }
        }

        else {

            throw new WrongUsageException("commands.gamestage.remove.usage", new Object[0]);
        }
    }
}