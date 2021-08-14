// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON DIMENSION ###
// The following helpers will grant a stage when a player enters a dimension
// that matches some condition. You can optionally specify more than one stage
// to grant to the player. You can also specify a hook that allows additional
// logic to be chained on when the stage(s) are granted.

// Grants a stage to the player when they enter the specified dimension. More
// than one stage can be specified at the end. In this case we are granting 
// the stage when they enter the Nether.
StageHelper.grantStageOnDimension("minecraft:the_nether", "dimension_example_one");

// Grants a stage to the player when they enter one of several specified 
// dimensions. In this case we are granting the stage when they enter the
// Nether or the End dimension.
StageHelper.grantStageOnDimension(["minecraft:the_end", "minecraft:the_nether"], "dimension_example_two");

// Grants a stage to the player when they enter a dimension that matches the
// specified condition. In this case we grant the stage when they enter the
// overworld by leaving the nether. The parameter order is the player, the 
// ID of the dimension they are entering, and the dimension they are leaving.
StageHelper.grantStageOnDimension((player, to, from) => {
    return to.toString() == "minecraft:overworld" && from.toString() == "minecraft:the_nether";
}, "dimension_example_three");