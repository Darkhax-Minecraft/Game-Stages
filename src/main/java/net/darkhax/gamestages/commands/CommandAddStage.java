package net.darkhax.gamestages.commands;

import java.util.List;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandAddStage extends Command {
    
    private final String command;
    private final boolean silent;
    
    public CommandAddStage (String command, boolean siletn) {
        
        this.command = command;
        this.silent = siletn;
    }
    
    @Override
    public String getName () {
        
        return this.command;
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 2;
    }
    
    @Override
    public String getUsage (ICommandSender sender) {
        
        return "/gamestage " + this.command + " <player> <stage>";
    }
    
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        if (args.length == 2) {
            
            final List<EntityPlayerMP> players = getPlayers(server, sender, args[0]);
            
            final String stageName = args[1];
            
            for (final EntityPlayerMP player : players) {
                
                GameStageHelper.addStage(player, stageName);
                GameStageHelper.syncPlayer(player);
                
                if (!this.silent) {
                    
                    player.sendMessage(new TextComponentTranslation("commands.gamestage.add.target", stageName));
                    
                    if (player != sender) {
                        sender.sendMessage(new TextComponentTranslation("commands.gamestage.add.sender", stageName, player.getDisplayNameString()));
                    }
                }
            }
        }
        else {
            throw new WrongUsageException(this.getUsage(sender));
        }
    }
    
    @Override
    public boolean isUsernameIndex (String[] args, int index) {
        
        return index == 0;
    }
}