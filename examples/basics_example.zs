// GameStages is a framework for creating custom progression systems. These 
// progression systems are built around stages which are named flags that can
// be given to or taken from players. On their own stages have no meaning or 
// functionality. It is up to you to create this meaning by configuring various
// mods like CraftTweaker and the many GameStage addons.

// CraftTweaker scripts are able to grant stages to players or check which 
// stages they already have. Combining these simple actions with the rest of
// CraftTweakers extensive feature set allows you to create a fine tuned systems
// for your pack. The following example combines these ideas to prevent players
// from trading with villagers until they have crafted a chest.

import crafttweaker.forge.api.event.interact.EntityInteractEvent;
import crafttweaker.api.util.InteractionHand;
import mods.gamestages.StageHelper;

// The StageHelper class provides many helper methods for quickly giving stages
// to players for common tasks like crafting an item or going to a dimension.
StageHelper.grantStageWhenCrafting(<item:minecraft:chest>, "crafted_chest");

// Registers an event listener with CraftTweaker. The code inside here will be 
// ran every time the player interacts with another entity.
events.register<EntityInteractEvent>((event) => {

    // The first bit makes sure the code is running on the server/logic thread.
    // The second part makes sure we are listening to mainhand clicks.
    if (!event.entity.level.isClientSide && event.hand == InteractionHand.MAIN_HAND) {
    
        // Checks if the entity being interracted with is a villager.
        if (event.target.getType() == <entitytype:minecraft:villager>) {
        
            // Checks if the player does NOT have the crafted_chest stage.
            if (!event.entity.hasGameStage("crafted_chest")) {
            
                // Cancelling the event prevents it from happening. The player
                // can not trade with the villager if they can't click it.
                event.cancel();
                
                // Send the player a chat message telling them why they could
                // not trade with the villager.
                event.entity.sendMessage("You need to craft a chest first!");
            }
        }
    }
});

// The following methods are available on all instances of player.

// Grant a specific GameStage to a player.
// player.addGameStage(String stageName);
// player.addGameStage("stage_one");

// Removes a specific GameStage from a player.
// player.removeGameStage(String stageName);
// player.removeGameStage("stage_one");

// Removes all GameStages from a player.
// player.clearGameStages();

// Checks if a player has a specific GameStage.
// player.hasGameStage(String stageName);
// player.hasGameStage("stage_one");

// Checks if a player has at least one of the specified stages.
// player.hasAnyGameStages(String... stages);
// player.hasAnyGameStages("stage_one", "stage_two", "defeated_boss");

// Checks if a player has all of the specified stages.
// player.hasAllGameStages(String... stages);
// player.hasAllGameStages("stage_one", "stage_two", "stage_three");