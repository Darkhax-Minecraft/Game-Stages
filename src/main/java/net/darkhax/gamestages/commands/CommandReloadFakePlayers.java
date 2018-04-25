package net.darkhax.gamestages.commands;

import net.darkhax.bookshelf.command.Command;
import net.darkhax.gamestages.capabilities.FakePlayerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReloadFakePlayers extends Command {
    @Override
    public String getName() {
        return "reloadfakes";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getUsage(final ICommandSender sender) {
        return "/gamestage reloadfakes";
    }

    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        FakePlayerData.reloadFromFile();
    }
}
