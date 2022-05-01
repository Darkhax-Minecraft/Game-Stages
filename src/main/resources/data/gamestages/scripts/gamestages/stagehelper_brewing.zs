// StageHelper is a collection of optional helper functions that make simple
// tasks like granting stages much easier. While it can be very beneficial to
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON BREWING ###
// The following helpers will grant a stage when the player brews an item in
// the brewing stand. An item is considered brewed when then player takes the
// item out of the output slot. You can optionally specify more than one stage
// to grant. You can also specify a hook that allows additional logic to be
// chained on when the stage(s) are granted. Be advised that even items placed
// in the output slot by a player are considered brewed by the game. This means
// that a player placing an empty bottle in the output and then immediately
// removing it is treated as them brewing an empty bottle.

// Grants a stage when the player brews an awkward potion.
StageHelper.grantStageWhenBrewing(<potion:minecraft:awkward>, "brew_awkward");

// Grants a stage when the player brews a long leaping potion. This is the
// leaping potion with an upgraded duration. If the player hasn't unlocked the
// stage yet the code inside will be executed.
StageHelper.grantStageWhenBrewing(<potion:minecraft:long_leaping>, (player, brewed) => {
    player.sendMessage("You brewed the long leaping potion!");
    player.give(<item:minecraft:stick>.withDisplayName("Pogo Stick"));
}, "brew_long_leaping");

// Grants a stage when the player brews any potion that gives night vision when
// consumed by the player.
StageHelper.grantStageWhenBrewing(<mobeffect:minecraft:night_vision>, "brew_night_vision");

// Grants a stage when the player brews a potion that gives poison when
// consumed by the player. If the player hasn't unlocked the stages yet the
// code inside will be executed.
StageHelper.grantStageWhenBrewing(<mobeffect:minecraft:poison>, (player, brewed) => {
    player.sendMessage("You brewed a poison potion!");
    player.give(<item:minecraft:stick>.withDisplayName("Milk Bucket"));
}, "brew_poison");

// Grants a stage when the player brews an item that matches an ingredient.
StageHelper.grantStageWhenBrewing(<item:minecraft:glass_bottle>, "brew_empty_bottle");

// Grants a stage when the player brews an item that matches our custom
// condition. In this case we grant the stage if the player is level 5 or
// greater when brewing anything.
StageHelper.grantStageWhenBrewing((player, brewed) => player.experienceLevel >= 5, "brew_lvl_5");

// Grants a stage when the player brews an item that matches our custom
// condition. In this case we grant the stage if the player is in creative
// mode. If the player did not have the stage previously the inner code is also
// executed.
StageHelper.grantStageWhenBrewing((player, brewed) => player.isCreative(), (player, brewed) => {
    player.sendMessage("You brewed creatively!");
    player.give(<item:minecraft:stick>.withDisplayName("CREATIVE"));
}, "brew_creative");