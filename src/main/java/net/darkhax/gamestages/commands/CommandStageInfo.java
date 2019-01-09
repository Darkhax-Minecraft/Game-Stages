package net.darkhax.gamestages.commands;

import java.util.stream.Collectors;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandStageInfo extends Command {
    
    @Override
    public String getName () {
        
        return "info";
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 0;
    }
    
    @Override
    public String getUsage (ICommandSender sender) {
        
        return "/gamestage info [player]";
    }
    
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        final EntityPlayer target = args.length == 1 ? getPlayer(server, sender, args[0]) : sender instanceof EntityPlayer ? (EntityPlayer) sender : null;
        
        if (target != null) {
            
            final String stages = getStages(target);
            
            if (!stages.isEmpty()) {
                
                sender.sendMessage(new TextComponentString(stages));
            }
            
            else {
                
                sender.sendMessage(new TextComponentTranslation("commands.gamestage.info.empty"));
            }
        }
        
        else {
            
            sender.sendMessage(new TextComponentTranslation("commands.gamestage.info.noplayer"));
        }
    }
    
    private static String getStages (EntityPlayer player) {
        
        String stages = GameStageHelper.getPlayerData(player).getStages().stream().map(Object::toString).collect(Collectors.joining(", "));
        
        if (PlayerUtils.isPlayersBirthdate(player)) {
            stages += ", HAPPY BIRTHDAY!";
        }
        
        return stages;
    }
}
