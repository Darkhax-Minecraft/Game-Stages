// GameStages adds several new events that allow scripts to listen to GameStage
// state changes. For example you can run a part of your script every time a 
// player unlocks a specific stage.

// To use the event system you need to import CraftTweaker's event manager. You
// should also import every event you plan on using. All GameStage events are
// listed below.
import mods.gamestages.events.GameStageAdded;
import mods.gamestages.events.GameStageRemoved;
import mods.gamestages.events.GameStageCleared;

// Registers a listener for when a game stage is added to a player.
events.register<GameStageAdded>((event) => {

    // The code in here will be ran every time a player is granted a stage.
    // You are given access to the player who unlocked the stage and the name
    // of the unlocked stage through the event context.
    
    // You can get the stage being added using event.stage. In this case we
    // check if the stage being added is event_example_one. 
    if (event.stage == "event_example_one") {
    
        // This code is only ran if the above condition is met. In this case
        // we send them a message and give them some diamonds.
        event.entity.sendMessage("You unlocked event_example_one! Have diamonds.");
        event.entity.give(<item:minecraft:diamond> * 4);
    }
});

// Registers a listener for when a game stage is removed from a player.
events.register<GameStageRemoved>((event) => {
    
    // The code in here will be ran every time a player loses a game stage.
    // You are given access to the player who lost the stage and the name of 
    // the stage that was lost through event context.
    
    // You can get the stage being removed using event.stage. In this case we
    // check if the stage lost was event_example_two.
    if (event.stage == "event_example_two") {
    
        // This code is only ran if the above condition is met. In this case
        // we send them a message and give them some sticks.
        event.entity.sendMessage("You lost event_example_two! You get sticks.");
        event.entity.give(<item:minecraft:stick> * 7);
    }
});

// Registers a listener for when a player has all their stages cleared.
events.register<GameStageCleared>((event) => {
    
    // The code in here will be ran every time a player has their game stages
    // cleared. You are given access to the player being cleared through the 
    // event context.
    
    // We get the player who lost their stages using event.entity and then we 
    // give them a chat message and some sugar.
    event.entity.sendMessage("You lost all your stages!");
    event.entity.give(<item:minecraft:sugar> * 2);
});