# Game Stages[![](http://cf.way2muchnoise.eu/268655.svg)](https://minecraft.curseforge.com/projects/game-stages) [![](http://cf.way2muchnoise.eu/versions/268655.svg)](https://minecraft.curseforge.com/projects/game-stages)
This mod provides a universal system for adding and handling game stages. It's primary use is to be an API for other staged mods to hook into. 

[![Nodecraft](https://i.imgur.com/sz9PUmK.png)](https://nodecraft.com/r/darkhax)    
This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!

# Usage
This mod adds two new commands. The first command is called gamestage. This command is used to add/remove a tier from a player. It has a few parameters. The first is the players name, you can use `@p` as a substitute. The second is the word `add` or `remove`, depending on what you want to do. The last parameter is the name of the stage. Trying to add a stage with a name that does not exist will create the stage. The second command is stageinfo. It has no parameters, and will just list out all the stages you have unlocked in chat. 

# Mod Support

## Getting Started
To get started, you need to add this mod to your `build.gradle` file dependencies. After this is done, re-run the `gradlew setupDecompWorkspace` command.
```
repositories {

    maven { url 'http://maven.epoxide.org' }
}

dependencies {

    compile "net.darkhax.gamestages:GameStages-${version_minecraft}:${version_gamestages}"
}
```

## Working With Stages
All the main hooks and methods are handled in the `IStageData` class. Each player has one of these attatched to them. You can get the stage data for a player by calling `PlayerDataHandler.getStageData(EntityPlayer player)`. Once you have the `IStageData` you can do things like `unlockStage(String stage)` and `lockStage(String stage)` and `hasUnlockedStage(String stage)`.