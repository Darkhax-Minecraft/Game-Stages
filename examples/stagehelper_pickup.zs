// StageHelper is a collection of optional helper functions that make simple
// tasks like granting stages much easier. While it can be very beneficial to
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON PICKUP ###
// The following helpers will grant stages when a player picks up an item. You
// can optionally specify more than one stage to grant. You can also specify a
// hook that allows additional logic to be chained on when the stage(s) are
// granted to the player.

// Grants a stage when an item is picked up.
StageHelper.grantStageWhenPickedUp(<item:minecraft:stick>, "pickup_sticks");

// Grants a stage when any item from the tag is picked up.
StageHelper.grantStageWhenPickedUp(<tag:items:forge:gems>, "pickup_gems");

// Grants a stage when paper is picked up, and then runs additional code if the
// player did not already have the stages.
StageHelper.grantStageWhenPickedUp(<item:minecraft:paper>, (player, itemEntity) => {
    player.sendMessage("You picked up paper!!");
    player.give(<item:minecraft:stick>.withDisplayName("Paper Pickup Award"));
}, "pickup_paper");

// Grants a stage when a player picks something up that matches a custom
// condition defined in the script. In this case the player needs to be level
// five or higher and pickup an apple.
StageHelper.grantStageWhenPickedUp((player, item) => player.experienceLevel >= 5 && <item:minecraft:apple>.matches(item), "pickup_apple_lvl_5");

// Grants a stage when a player picks something up that matches a custom
// condition defined in the script. Then runs additional code if the player did
// not have the stage yet. In this case the player can pickup anything that is
// not a stick and they will get a chat message.
StageHelper.grantStageWhenPickedUp((player, item) => !<item:minecraft:stick>.matches(item), (player, itemEntity) => {
    player.sendMessage("Yep, that's not a stick!");
}, "pickup_not_a_stick");

// StageHelper.grantStageWhenPickedUp(IIngredient condition, String... stages);
// StageHelper.grantStageWhenPickedUp(IIngredient condition, BiConsumer<PlayerEntity, ItemEntity> hook, String... stages);
// StageHelper.grantStageWhenPickedUp(BiPredicate<PlayerEntity, IItemStack> condition, String... stages);
// StageHelper.grantStageWhenPickedUp(BiPredicate<PlayerEntity, IItemStack> condition, BiConsumer<PlayerEntity, ItemEntity> hook, String... stages);