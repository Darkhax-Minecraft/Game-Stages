// StageHelper is a collection of optional helper functions that make simple
// tasks like granting stages much easier. While it can be very beneficial to
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON ADVANCEMENT ###
// The following helpers will grant stages when a player smelts an item that
// matches a specified condition. An item is considered smelted when a player
// takes it out of the output slot of a furnace. You can optionally specify
// more than one stage to grant. You can also specify a hook that allows
// additional logic to be chained on when the stage(s) are granted.

// Grants a stage when a player smelts anything into iron ingots.
StageHelper.grantStageWhenSmelting(<item:minecraft:iron_ingot>, "smelted_iron");

// Grants several stages when a player smelts anything into glass. Multiple
// stage names are tacked on the end. This can be done for any of these methods.
StageHelper.grantStageWhenSmelting(<item:minecraft:glass>, "smelted_glass", "smelted_glass_2", "smelted_glass_3");

// Grants a stage when a player smelts anything into gold ingots. Additional code is
// also ran if the player didn't already have the stage.
StageHelper.grantStageWhenSmelting(<item:minecraft:gold_ingot>, (player, smelted) => {
    player.sendMessage("You unlocked the stage smelted_gold");
    player.give(<item:minecraft:stick> * 10);
}, "smelted_gold");

// Grants a stage when a player smelts anything as long as they have 5 or more EXP
// levels. This is an arbitrary condition and can check the player or the smelt result.
StageHelper.grantStageWhenSmelting((player, smelted) => player.experienceLevel >= 5, "smelted_lvl5");

// Grants a stage when the player smelts anything into smooth stone. This will also
// run some additional code. Basically custom condition, and a hook.
StageHelper.grantStageWhenSmelting((player, smelted) => <item:minecraft:stone>.matches(smelted), (player, smelted) => {
    player.sendMessage("You unlocked the stage smelted_stone");
    player.give(<item:minecraft:apple> * 2);
}, "smelted_stone");

// StageHelper.grandStageWhenSmelting(IIngredient stack, String... stages);
// StageHelper.grantStageWhenSmelting(IIngredient stack, BiConsumer<PlayerEntity, IItemStack> hook, String... stages);
// StageHelper.grantStageWhenSmelting(BiPredicate<PlayerEntity, IItemStack> predicate, String... stages);
// StageHelper.grantStageWhenSmelting(BiPredicate<PlayerEntity, IItemStack> predicate, BiConsumer<PlayerEntity, IItemStack> hook, String... stages);
