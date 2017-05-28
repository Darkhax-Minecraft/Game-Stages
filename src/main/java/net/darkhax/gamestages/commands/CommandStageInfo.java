package net.darkhax.gamestages.commands;

import java.util.stream.Collectors;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandStageInfo extends CommandBase {

    @Override
    public String getName () {

        return "stageinfo";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.stageinfo.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayer) {

            final String stages = PlayerDataHandler.getStageData((EntityPlayer) sender).getUnlockedStages().stream().map(Object::toString).collect(Collectors.joining(",")).toString();
            sender.sendMessage(new TextComponentString(stages));
        }
    }
}
