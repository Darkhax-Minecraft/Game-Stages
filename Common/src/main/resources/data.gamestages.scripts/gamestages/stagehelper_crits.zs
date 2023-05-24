#debug
// StageHelper is a collection of optional helper functions that make simple
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;


// ### GIVE STAGE ON CRIT ###
// The following helpers will grant a stage when a player performs a crit on
// another entity that matches some condition. You can optionally specify a
// more than one stage to grant to the player. You can also specify a hook that
// allows additional logic to be chained on when the stage(s) are granted.

// Grants a stage to a player when they crit a specific entity.
StageHelper.grantStageOnCrit(<entitytype:minecraft:creeper>, "crit_creeper");

// Grants a stage to a player when they crit a mob that belongs to a specific
// entity tag.
//StageHelper.grantStageOnCrit(<tag:entity_types:minecraft:raiders>, "crit_raider");

// Grants a stage to a player when they crit a mob and a special condition is
// met. In this case we check if the mob being critted was added by Minecraft.
StageHelper.grantStageOnCrit((player, target) => target.getType().registryName.namespace == "minecraft", "crit_minecraft");

// Grants a stage to a player when they crit a mob and a special condition is 
// met. The inner code will then be executed after the player receives their
// stages. In this case we grant the stages if the player level is 5 or higher
// when they crit any mob. We then send a chat message and give them an item.
StageHelper.grantStageOnCrit((player, target) => player.experienceLevel >= 5, (player, target) => {
    player.sendMessage("Nice crit! And you were level 5 or higher!");
    player.give(<item:minecraft:stick>.withDisplayName("Crit Stick"));
}, "crit_lvl5");

// The following methods are available.
// StageHelper.grantStageOnCrit(MCEntittyType type, String... stages);
// StageHelper.grantStageOnCrit(MCEntittyType type, BiConsumer<PlayerEntity, Entity> hook, String... stages);
// StageHelper.grantStageOnCrit(MCTag<MCEntityType> tag, String... stages);
// StageHelper.grantStageOnCrit(MCTag<MCEntityType> tag, BiConsumer<PlayerEntity, Entity> hook, String... stages);
// StageHelper.grantStageOnCrit(BiPredicate<PlayerEntity, Entity> condition, String... stages);
// StageHelper.grantStageOnCrit(BiPredicate<PlayerEntity, Entity> condition, BiConsumer<PlayerEntity, Entity> hook, String... stages);