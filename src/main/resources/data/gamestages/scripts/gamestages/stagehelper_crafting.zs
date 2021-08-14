// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON CRAFTING ###
// The following helpers will grant a stage when the player crafts an item that
// matches a specified condition. The item must be crafted by an actual player
// in a crafting grid like the crafting table. You can optionally specify more
// than one stage to grant. You can also specify a hook that allows additional
// logic to be chained on when the stage(s) are granted to the player.

// Grants a stage when a player crafts a specific item with any size, nbt, etc.
// In this case the player must craft sticks with any recipe.
StageHelper.grantStageWhenCrafting(<item:minecraft:stick>, "craft_example_one");

// Grants a stage when a player crafts a specific item and stack size.
// In this case the player must craft a recipe that outputs 9 gold nuggets.
StageHelper.grantStageWhenCrafting(<item:minecraft:gold_nugget> * 9, "craft_example_two");

// Grants a stage when a player crafts any item from an item tag.
// In this case the player must craft any wooden fence.
StageHelper.grantStageWhenCrafting(<tag:items:minecraft:wooden_fences>, "craft_example_three");

// Grants a stage when a player crafts an item with NBT data.
// In this case the player must craft a stick with the name "Magic Stick o' Wood".
StageHelper.grantStageWhenCrafting(<item:minecraft:stick>.withTag({display: {Name: "{\"text\":\"Magic Stick o' Wood\"}" as string}}), "craft_example_four");

// Grants a stage when a player crafts an item that meets custom criteria.
// In this case the player must craft any item with a modid of "minecraft".
StageHelper.grantStageWhenCrafting((output) => output.owner == "minecraft", "craft_example_five");

// Grants a stage when a player crafts an item. When the stage is granted 
// additional logic (a hook) will be ran. In this case we send a custom message
// to the player and then give them ten diamonds.
StageHelper.grantStageWhenCrafting(<item:minecraft:stick>, (player, output) => {
    
    player.sendMessage("You unlocked the craft_example_six stage!");
    player.give(<item:minecraft:diamond> * 10);
}, "craft_example_six");