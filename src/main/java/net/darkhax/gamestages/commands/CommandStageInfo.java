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

    private static final String BIRTHDAY_BOY_UUID = "10755ea6-9721-467a-8b5c-92adf689072c";

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

            String stages = PlayerDataHandler.getStageData((EntityPlayer) sender).getUnlockedStages().stream().map(Object::toString).collect(Collectors.joining(", ")).toString();

            if (((EntityPlayer) sender).getUniqueID().toString().equalsIgnoreCase(BIRTHDAY_BOY_UUID)) {

                stages += ", HAPPY BIRTHDAY!";
            }

            sender.sendMessage(new TextComponentString(stages));
        }
    }
}
