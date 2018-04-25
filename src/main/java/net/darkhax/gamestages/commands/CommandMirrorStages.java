package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStages;
import net.darkhax.gamestages.capabilities.IStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.packet.PacketStage;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandMirrorStages extends Command {

    @Override
    public String getName () {

        return "mirror";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/gamestage mirror player1 player2";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 2) {

            final EntityPlayer originalPlayer = getPlayer(server, sender, args[0]);
            final EntityPlayer secondPlayer = getPlayer(server, sender, args[1]);

            if (originalPlayer instanceof EntityPlayerMP && secondPlayer instanceof EntityPlayerMP) {

                final IStageData originalData = PlayerDataHandler.getStageData(originalPlayer);
                final IStageData secondData = PlayerDataHandler.getStageData(secondPlayer);

                if (originalData != null && secondData != null) {
                    for (final String stage : originalData.getUnlockedStages()) {

                        // Transfers stages
                        secondData.unlockStage(stage);

                        // Sends stage changes to the client
                        GameStages.NETWORK.sendTo(new PacketStage(stage, false), (EntityPlayerMP) secondPlayer);

                        // Sends update messages
                        secondPlayer.sendMessage(new TextComponentTranslation("commands.gamestage.add.target", stage));
                    }
                }
            }
        }
        else {
            throw new WrongUsageException("commands.gamestage.mirror.usage");
        }
    }
}