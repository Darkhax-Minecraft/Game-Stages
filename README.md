# Zalgo's Command
Zalgo's Command is a mod which allows modpacks and servers to group multiple commands together, easily. This makes game setups with multple stages, or tiers easier to maintain.

[![Nodecraft](https://i.imgur.com/sz9PUmK.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

#Usage
This mod adds the zalgo command. This command accepts two parameters, the first is the name of the player. `@p` is a valid substitute for the player's name. The second argument is the name of the command group to execute. You can execute multiple command groups with one command! Also, a pro tip, you can use `@p` in the commands which you add to the command group, and the player from the base command will be used. 

# Configuration
You can create new command groups by adding lines to the config files. Each entry should be added to the config file, on a new line. There are a few examples which are in the default config. Each entry has two components, the command group id, and the command. These two components are split using @SPLIT@. Here is an example, `test1@SPLIT@give @p minecraft:stone 1`

# CraftTweaker Support!
If you don't want to use the config options, you can use CraftTweaker! While CraftTweaker is optional, it is the recommended way to add command groups. To add a command group, use `mods.ZalgoCMD.addCommand(String commandGroup, String command);`. This will add the command to that command group. Here is an example. `mods.ZalgoCMD.addCommand("test4", "give @p minecraft:diamond 10");`