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

## Maven Dependency
If you are using [Gradle](https://gradle.org) to manage your dependencies, add the following into your `build.gradle` file. Make sure to replace the version with the correct one. All versions can be viewed [here](https://maven.blamejared.com/net/darkhax/gamestages/).
```
repositories {

    maven {
    
        url 'https://maven.blamejared.com'
    }
}

dependencies {

    // Example: compile "net.darkhax.gamestages:GameStages-1.16.4:6.0.4"
    compile "net.darkhax.gamestages:GameStages-MCVERSION:PUT_FILE_VERSION_HERE"
}
```

## Jar Signing

As of January 11th 2021 officially published builds will be signed. You can validate the integrity of these builds by comparing their signatures with the public fingerprints.

| Hash   | Fingerprint                                                        |
|--------|--------------------------------------------------------------------|
| MD5    | `12F89108EF8DCC223D6723275E87208F`                                 |
| SHA1   | `46D93AD2DC8ADED38A606D3C36A80CB33EFA69D1`                         |
| SHA256 | `EBC4B1678BF90CDBDC4F01B18E6164394C10850BA6C4C748F0FA95F2CB083AE5` |


## Sponsors
<img src="https://nodecraft.com/assets/images/logo-dark.png" width="384" height="90">

This project is sponsored by Nodecraft. Use code [Darkhax](https://nodecraft.com/r/darkhax) for 30% off your first month of service!