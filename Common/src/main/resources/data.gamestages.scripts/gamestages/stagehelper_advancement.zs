// StageHelper is a collection of optional helper functions that make simple 
// tasks like granting stages much easier. While it can be very beneficial to 
// use these helpers they are designed with simple use cases in mind and using
// them limits your ability to fine tune. Fortunately every helper is optional
// and can be replicated using standard CraftTweaker features which can be used
// for the more advanced use cases.

// To directly use StageHelper in your script you need to import it!
import mods.gamestages.StageHelper;

// ### GIVE STAGE ON ADVANCEMENT ###
// The following helpers will grant stages when a player earns an advancement
// that matches a specified condition. You can optionally specify more than one
// stage to grant. You can also specify a hook that allows additional logic to 
// be chained on to when the stage(s) are granted to the player.

// Grants a stage when a player earns an advancement with a specific ID. In 
// this case the player must earn the "We Need to Go Deeper" advancement.
StageHelper.grantStageOnAdvancement("minecraft:story/enter_the_nether", "advancement_example_one");

// Grants a stage when a player earns one of many potential advancements. Only
// one is needed to grant the stages. In this example the player must earn
// either the "Ice Bucket Challenge" or "Diamonds!" advancement.
StageHelper.grantStageOnAdvancement(["minecraft:story/form_obsidian", "minecraft:story/mine_diamond"], "advancement_example_two");

// Grants a stage when a player earns an advancement that matches a custom 
// condition. In this case the player must earn any advancement with the owner
// ID "minecraft".
StageHelper.grantStageOnAdvancement((advancement) => advancement.namespace == "minecraft", "advancement_example_three");

// These helpers can also accept an optional hook as the second parameter. This
// hook will be ran when the conditions are met and the stage(s) are granted.
StageHelper.grantStageOnAdvancement("minecraft:story/enter_the_end", (player, advancement) => {

    player.sendMessage("You unlocked the stage advancement_example_four");
    player.give(<item:minecraft:diamond> * 10);
}, "advancement_example_four");

// All valid method descriptors for these helpers can be found below.
// StageHelper.grantStageOnAdvancement(String targetId, String... stagesToGrant);
// StageHelper.grantStageOnAdvancement(String targetId, BiConsumer<PlayerEntity, ResourceLocation> hook, String... stagesToGrant);
// StageHelper.grantStageOnAdvancement(String[] targetIds, String... stagesToGrant);
// StageHelper.grantStageOnAdvancement(String[] targetIds, BiConsumer<PlayerEntity, ResourceLocation> hook, String... stagesToGrant);
// StageHelper.grantStageOnAdvancement(Predicate<ResourceLocation> condition, String... stagesToGrant);
// StageHelper.grantStageOnAdvancement(Predicate<ResourceLocation> condition, BiConsumer<PlayerEntity, ResourceLocation> hook, String... stagesToGrant);
