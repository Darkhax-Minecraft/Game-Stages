// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON JOIN ###
// The following helpers will grant a stage when a player joins the world. More
// than one stage can be defined at the end. You can optionally specify a
// condition for the stage to be given. You can also optionally specify a hook 
// that allows additional logic to be chained on when the stage(s) are granted.

// Grants a stage to the player when they join the game. More than one stage
// can be specified at the end if you need to grant multiple stages.
StageHelper.grantStageOnJoin("join_example_one");

// Grants a stage to the player when they join the game if they match the 
// specified condition. In this case the stage is granted when the player
// is not in creative mode.
StageHelper.grantStageOnJoinWithCondition(player => !player.isCreative(), "join_example_two");

// Grants a stage to the player when they join the game. Also runs additional
// logic when the stages are granted. The hook will not be ran if they already
// have all of the stages being granted. In this case they are shown a chat
// message and given four emeralds.
StageHelper.grantStageOnJoinWithHook(player => {

    player.sendMessage("You were granted stage join_example_three.");
    player.give(<item:minecraft:emerald> * 4);
}, "join_example_three");

// Grants a stage to the player when they join the game if they match the 
// specified condition. Also runs additional logic when the stages are 
// granted. The hook will not be ran if the condition fails or the player
// already has every stage being granted. In this case they are given the
// stage when they join in creative mode. They will also be given some sticks
// and a message will be played in chat.
StageHelper.grantStageOnJoin(player => player.isCreative(), player => {

    player.give(<item:minecraft:stick> * 16);
}, "join_example_four");

// All valid method descriptors for these helpers can be found below.
// StageHelper.grantStageOnJoin(String... stages);
// StageHelper.grantStageOnJoinWithCondition(Predicate<PlayerEntity> condition, String... stages);
// StageHelper.grantStageOnJoinWithHook(Consumer<PlayerEntity> hook, String... stages);
// StageHelper.grantStageOnJoin(Predicate<PlayerEntity> condition, Consumer<PlayerEntity> hook, String... stages);