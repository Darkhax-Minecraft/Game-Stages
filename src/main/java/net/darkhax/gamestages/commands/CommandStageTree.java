package net.darkhax.gamestages.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandStageTree extends CommandTreeBase {

    public CommandStageTree () {

        this.addSubcommand(new CommandAddStage());
        this.addSubcommand(new CommandRemoveStage());
        this.addSubcommand(new CommandStageInfo());
        this.addSubcommand(new CommandCheckStage());
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getName () {

        return "gamestage";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.gamestage.usage";
    }
}