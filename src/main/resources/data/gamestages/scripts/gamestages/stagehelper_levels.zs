// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON LEVEL ###
// The following helpers will grant a stage when the player's EXP level changes
// to a value that matches a specified condition. You can optionally specify
// more than one stage to grant. You can also specify a hook that allows
// additional logic to be chained on when the stage(s) are granted. The hook
// will not run if the player already has all of the stages to grant.

// Grants a stage when a player reaches a specific EXP level. In this case they
// get the stage when they reach level 4.
StageHelper.grantStageOnExactLevel(4, "level_example_one");

// Grants a stage when a player reaches an EXP level greater than or equal to 
// a specified level. In this case they must reach level 2 or higher.
StageHelper.grantStageOnLevel(2, "level_example_two");

// Grants a stage when a player reaches an EXP level that matches a custom 
// condition. In this case we grant the stage if the EXP level is even.
StageHelper.grantStageOnLevel(level => level % 2 == 0, "level_example_three");

// Each of these helpers can specify an optional hook which allows you to 
// chain additional logic when the stage is granted. This hook will give you
// the player and their current exp level. In this case we give the player a
// chat message and some diamonds. Hooks are only run if the player does not
// have all of the stages being granted already.
StageHelper.grantStageOnLevel(level => true, (player, level) => {
    
    player.sendMessage("You unlocked the level_example_four stage!");
    player.give(<item:minecraft:diamond> * 10);
}, "level_example_four");

// All valid method descriptors for these helpers can be found below.
// StageHelper.grantStageOnExactLevel(int level, String... stages);
// StageHelper.grantStageOnExactLevel(int level, BiConsumer<PlayerEntity, int> hook, String... stages);
// StageHelper.grantStageOnLevel(int level, String... stages);
// StageHelper.grantStageOnLevel(int level, BiConsumer<PlayerEntity, int> hook, String... stages);
// StageHelper.grantStageOnLevel(Predicate<int> condition, String... stages);
// StageHelper.grantStageOnLevel(Predicate<int> condition, BiConsumer<PlayerEntity, int> hook, String... stages);