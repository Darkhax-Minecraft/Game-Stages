# Game Stages

Game Stages provides a framework for creating modpack progression systems in Minecraft. These progression systems are built using stages which are named true/false flags which are stored on a per-player basis. Stages are not linear and have no implicit connection or dependency on each other.

Game stages only provides the framework for handling stage data. It saves the data, it synchronizes it with clients, and it provides commands that can be used to interact with the data. It does **not** alter game mechanics or interpret what stage flags are meant to do. This functionality is provided through Game Stage addon mods.

## Addons

Other mods can hook into Game Stages and use a player's stage data to change how certain mechanics work. These addons and their mechanics provide the tools that modpack authors use to build their progression systems.

## FaQ

**How do I give players a stage?**    
This mod provides the `/gamestage add @p stagename` command which will give a player a stage. These commands can be used in command blocks, functions, advancements, signs, books, etc. Many mods will also allow you to run a command when a player does something. Keep in mind the `@p` is substituted to be the current player.

**How do I stage a recipe/block/item?**    
Game Stages does not provide the actual progression mechanics. You will need to use addons such as ItemStages to do these things.