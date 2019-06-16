# Game Stages [![](http://cf.way2muchnoise.eu/268655.svg)](https://minecraft.curseforge.com/projects/game-stages) [![](http://cf.way2muchnoise.eu/versions/268655.svg)](https://minecraft.curseforge.com/projects/game-stages)
This mod provides a universal progression system for mods and modpacks to use. 

[![Nodecraft](https://nodecraft.com/assets/images/logo-dark.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

# Introduction
This mod introduces a new concept/mechanic to the game, which is called a game stage. A game stage is a named flag/variable that is tied to a player. Mods can use these stages to restrict access to certain things. Modpacks and servers can give or take stages from players using commands. For example, you can use the [Dimension Stages](https://minecraft.curseforge.com/projects/dimension-stages) mod to restrict access to the nether to players that have the `nether_access` stage. You can then create an advancement that awards the `nether_access` stage to the player that completes it. This setup will effectively prevent all access to the nether until the player has unlocked your advancement. There are many other mods that provide different ways to apply restrictions. 

## Command List
- `/gamestage add <player> <stage>` - Adds a stage to a player.
- `/gamestage silentadd <player> <stage>` - Adds a stage to a player without telling them.
- `/gamestage remove <player> <stage>` - Removes a stage from a player.
- `/gamestage silentremove <player> <stage>` - Removes a stage from a player without telling them.
- `/gamestage info` - Tells the player all the stages that they have. This is primarily for debug purposes. 
- `/gamestage check <player> <stage>` - Checks if a player has a stage. This can be used in a command block with a comparator to emit redstone when the player has a stage.
- `/gamestage clear <player>` - Clears all stages that a player has.
- `/gamestage reloadfakes` - Reloads the fake player data. This is mainly used for advanced modpack makers in rare circumstances.

## Stage Basics
Game stages have a few basic behaviors and mechanics that are important to understand. The first is that stage names must be all lower case, and have no spaces in them. It is okay to use things like underscore characters if you want to space things out better. It is also important to know that Game Stages do not need to be registered, and you can give or check any arbitrary stage name. The last thing to know about game stages is that in situations where a player can not be obtained, the default behavior is to assume that the player would not have the correct stage. For example, if you prevent diamond ore from dropping diamonds unless the player has the `diamonds` stage, a creeper blowing up a diamond ore will always fail to drop the diamond. 

## Loot Conditions
This mod provides a loot condition which can be used in loot tables to prevent access to certain entries unless a player has a given stage. For example, this can be used to prevent a mob from dropping an item until the player who kills it has unlocked a stage. This works for mob drop loot, and for chest loot. Here is an example of what this condition looks like. 

```json
	"conditions": [{
		"condition": "required_stage",
		"stage_name": "one"
	}]
```

## Fake Players
This mod allows for fake players to be given default stages for your pack. Mods will often use fake players to allow for certain things like auto-crafting or blocks that can kill mobs and get player-only drops. This is done by editing the `config/gameStagesFakePlayerData.json` file to include the name of the fake player and a list of stages to give them. Here are the contents of an example file.

```json
[
  {
    "fakePlayerName": "test123",
    "stages": [
      "stage1",
      "stage2",
      "stage3"
    ]
  },
  {
    "fakePlayerName": "fake456",
    "stages": [
      "fish",
      "pepper",
      "cheese"
    ]
  }
]
```

## CraftTweaker Support
This mod provides CraftTweaker scripts with the ability to check what stages a player has, and add/remove stages for the player. This allows for things like CraftTweaker events and functions to take advantage of the game stages mod. You can find more information about this feature [here](https://docs.blamejared.com/en/#Mods/GameStages/Player_Stages/)

# Mod Support
This section provides information about implementing GameStages into your mod. 

## Getting Started
To get started, you need to add this mod to your `build.gradle` file dependencies. After this is done, re-run the `gradlew setupDecompWorkspace` command.
```
repositories {

    maven { url 'https://maven.mcmoddev.com' }
}

dependencies {

    compile "net.darkhax.gamestages:GameStages-${version_minecraft}:${version_gamestages}"
}
```

## Working With Stages
This mod provides a `GameStageHelper` class which can be used to work with stage data in a safe way that ensures all hooks and events are fired. It is highly recommended to use this class instead of trying to work directly with stage data. 
