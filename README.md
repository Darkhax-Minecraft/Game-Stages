# Game Stages [![](http://cf.way2muchnoise.eu/268655.svg)](https://minecraft.curseforge.com/projects/game-stages) [![](http://cf.way2muchnoise.eu/versions/268655.svg)](https://minecraft.curseforge.com/projects/game-stages)
This mod provides a universal progression system for mods and modpacks to use. 

[![Nodecraft](https://nodecraft.com/assets/images/logo-dark.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

# Introduction
Note: This documentation is for GameStages 3 on Minecraft 1.13.2. The mod may behave differently on other versions. Please refer to the version of this documentation that specifically matches the version of GameStages that you are using!

This mod adds a new concept to the game which is called a game stage. A game stage is a named flag/variable that is tied to a player. These stages can be granted or revoked using commands that are ran by advancements, command blocks, quests, or other systems. Mods are able to detect the stages a player has, and react differently depending on whether or not a specific stage has been granted. For example the mod [Dimension Stages](https://minecraft.curseforge.com/projects/dimension-stages) allows you to restrict dimension access to players who have a specific stage. There are many mods and addons for GameStages which you can combine to create your own customozed progression system for the game.

## Command List
- `/gamestage add <player> <stage> (silent)` - Adds a stage to a player, or group of players. If silent is true, the player will not be told.
- `/gamestage remove <player> <stage> (silent)` - Removes a stage from a player, or group of players. If silent is true, the player will not be told.
- `/gamestage info (player)` - Provides the names of stages a player, or group of players have. If the player is not specified your own info will be shown.
- `/gamestage all (player)` - Gives all known stages to a player, or group of players. If no player is specified they will be given to yourself.
- `/gamestage clear (player)` - Removes all stages from a player or group of players. If no player is specified they will be taken from yourself.
- `/gamestage check <stage>` - Checks if you have a stage.
- `/gamestage check <player> <stage>` - Checks if a player or group of players have a given stage.
- `/gamestage reload` - Reloads the fake player and known stages data.

## Stage Basics
For a game stage to work properly you must define it in the known stages file. This file is found at `~/config/gamestages/known_stages.json`. If the json file does not exist you can create it yourself. This file holds an array of all the valid stage names. An example file would look like this. `["one", "two", "three", "five"]`. Stages must be defined in this file to allow the `/gamestage all (player)` command to work. It is also used by commands for suggestions and auto complete. Most importantly defining stages here will help you find typos if you ever type the name wrong somewhere else. You can use the `/gamestage reload` command to reload the known stages file without restarting your game.

There are a few requirements for stage names. Please make sure that all your stage names follow these rules, otherwise you will have issues. 
- Names must be 64 characters or less.
- Names must be completely lower case.
- Names may contain numbers, colons, and underscores.

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

# Mod Support
If you are a mod author, this section of the documentation is for you! 

## Getting Started
To get started, you need to add this mod to your dev environment. The most popular way to do this is by adding it to your dependencies in your `build.gradle` file, and then running the gradle setup commands again. This will not make GameStages a hard dependency, but it will allow your mod to reference and access GameStage code.
```
repositories {

    maven { url 'https://maven.mcmoddev.com' }
}

dependencies {

    compile "net.darkhax.gamestages:GameStages-${version_minecraft}:${version_gamestages}"
}
```

## Working With Stages
This mod provides a `GameStageHelper` class. It provides many helper tools and functions which can be used by your mod. This mod also offers some events which are fired on the Forge event bus. The helper class and these events are considered to ne the only public parts of the GameStage API. Please avoid accessing other systems as much as possible.

### Events List
- Add - Fired when a player gets a stage. You can cancel this to prevent the stage from being unlocked.
- Added - Fired after the stage has been added.
- Remove - Fired when a player loses a stage. You can cancel this to prevent the stage from being removed.
- Removed - Fired after the stage was removed.
- Cleared - Fired when the player has had their gamestage data cleared.
- Check - Fired when a check is made on a player's stage data. You can use this event to override the results with your own. 
- StagesSyncedEvent - Fired on the client when the stage data has been synched from the client.

### Important Notes
- Whenever you add or remove a stage, you need to manually call the sync method to sync the data to the client. 
- The client only knows their own stage data.
- Whenever a user specifies a stage with your mods configuration, you should test the name using `GameStageHelper#isStageKnown` to ensure the stage is known. If the stage is not known you should assume it's a typo and give an error.
- If you have any questions or requests please reach out to me on [Twitter](https://twitter.com/DarkhaxDev) or [Discord](https://discord.darkhax.net).
