// StageHelper is a collection of optional helper functions that make simple
// tasks like granting stages much easier. While it can be very beneficial to
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON FISHING ###
// The following helpers will grant stages when a player receives an item from
// fishing. You can optionally specify more than one stage to grant. You can
// also specify a hook that allows additional logic to be chained on when the
// stage(s) are granted.

// Grants a stage when a player receives string from fishing.
StageHelper.grantStageWhenFishing(<item:minecraft:string>, "fish_string");

// Grants a stage when a player receives an item from the tag from fishing.
StageHelper.grantStageWhenFishing(<tag:items:forge:gems>, "fish_tag_gems");

// Grants a stage when a player receives paper from fishing. Also runs the
// inner code if the player didn't already have the stage.
StageHelper.grantStageWhenFishing(<item:minecraft:paper>, (player, bobber, drops) => {
    player.sendMessage("You fished the soggy paper!");
    player.give(<item:minecraft:stick>.withDisplayName("Old Rod"));
}, "fish_paper");

// Grants a stage when a player fishes something up while being level 5 or
// greater. The inner code will also be executed if the player didn't have the
// stage already.
StageHelper.grantStageWhenFishing((player, bobber, drops) => player.experienceLevel >= 5, (player, bobber, drops) => {
    player.sendMessage("You fished at lvl 5");
    player.give(<item:minecraft:stick>.withDisplayName("wow"));
}, "fish_lvl_5");

// StageHelper.grantStageWhenFishing(IIngredient ingredient, String... stages);
// StageHelper.grantStageWhenFishing(IIngredient ingredient, FishingHook hook, String... stages);
// StageHelper.grantStageWhenFishing(FishingCondition condition, FishingHook hook, String... stages);