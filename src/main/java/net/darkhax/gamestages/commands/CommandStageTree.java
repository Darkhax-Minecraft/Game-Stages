package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.CommandTree;
import net.minecraft.command.ICommandSender;

public class CommandStageTree extends CommandTree {
    
    public CommandStageTree () {
        
        this.addSubcommand(new CommandAddStage("add", false));
        this.addSubcommand(new CommandAddStage("silentadd", true));
        this.addSubcommand(new CommandRemoveStage("remove", false));
        this.addSubcommand(new CommandRemoveStage("silentremove", true));
        this.addSubcommand(new CommandStageInfo());
        this.addSubcommand(new CommandCheckStage());
        this.addSubcommand(new CommandStageClear());
        this.addSubcommand(new CommandReloadFakePlayers());
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
        
        return "/gamestage";
    }
}