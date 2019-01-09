package net.darkhax.gamestages.commands;

import java.util.List;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

public class CommandStageClear extends Command {
    
    @Override
    public String getName () {
        
        return "clear";
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 2;
    }
    
    @Override
    public String getUsage (ICommandSender sender) {
        
        return "/gamestage clear <player>";
    }
    
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        if (args.length == 1) {
            
            final List<EntityPlayerMP> players = getPlayers(server, sender, args[0]);
            
            for (final EntityPlayerMP player : players) {
                
                final IStageData stageInfo = GameStageHelper.getPlayerData(player);
                final int stageCount = stageInfo.getStages().size();
                
                stageInfo.clear();
                GameStageHelper.syncPlayer(player);
                
                MinecraftForge.EVENT_BUS.post(new GameStageEvent.Cleared(player, stageInfo));
                
                player.sendMessage(new TextComponentTranslation("commands.gamestage.clear.target", stageCount));
                
                if (player != sender) {
                    sender.sendMessage(new TextComponentTranslation("commands.gamestage.clear.sender", stageCount, player.getDisplayNameString()));
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