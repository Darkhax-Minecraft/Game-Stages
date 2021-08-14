// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON KILL ###
// The following helpers will grant a stage when a player kills an entity that
// matches some condition. You can optionally specify more than one stage to 
// grant to the player. You can also specify a hook that allows additional
// logic to be chained on when the stage(s) are granted.

// Grants a stage to a player when they kill a mob. In this case we grant the
// stage when a player kills kills a creeper.
StageHelper.grantStageOnKill(<entitytype:minecraft:creeper>, "kill_example_one");

// Grants a stage to a player when they kill a mob in a tag. In this case we 
// grant the stage whenever they kill any type of skeleton.
StageHelper.grantStageOnKill(<tag:entity_types:minecraft:raiders>, "kill_example_two");

// Grants a stage to a player when they kill a mob that matches an custom
// condition. In this case we check if the mob killed was added by Minecraft.
StageHelper.grantStageOnKill((player, target) => target.type.registryName.namespace == "minecraft", "kill_example_three");

// Each of these methods can support an optional hook as the second argument.
// This allows you to chain additional logic on when the stages are granted.
// In this case we are sending the player a chat message and give them a stick
// that is renamed to Magic Wand.
StageHelper.grantStageOnKill(<entitytype:minecraft:witch>, (player, target) => {

    player.sendMessage("You killed a witch!");
    player.give(<item:minecraft:stick>.withDisplayName("Magic Wand"));
}, "kill_example_four");

// The following methods are available.
// StageHelper.grantStageOnKill(MCEntittyType type, String... stages);
// StageHelper.grantStageOnKill(MCEntittyType type, BiConsumer<PlayerEntity, LivingEntity> hook, String... stages);
// StageHelper.grantStageOnKill(MCTag<MCEntityType> tag, String... stages);
// StageHelper.grantStageOnKill(MCTag<MCEntityType> tag, BiConsumer<PlayerEntity, LivingEntity> hook, String... stages);
// StageHelper.grantStageOnKill(BiPredicate<PlayerEntity, LivingEntity> condition, String... stages);
// StageHelper.grantStageOnKill(BiPredicate<PlayerEntity, LivingEntity> condition, BiConsumer<PlayerEntity, LivingEntity> hook, String... stages);