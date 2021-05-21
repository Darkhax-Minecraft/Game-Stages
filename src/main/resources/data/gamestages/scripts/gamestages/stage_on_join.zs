// This example will give all players the stage "one" when they join the world.
// This is done by registering an event listener which is a bit of code that
// is executed every time that event happens. In this case we are using the 
// MCPlayerLoggedInEvent which happens when a player joins the world or server.

// Some code requires importing classes to use them.
import crafttweaker.api.events.CTEventManager;
import crafttweaker.api.event.entity.player.MCPlayerLoggedInEvent; 

// Registers an event listener for the MCPlayerLoggedInEvent.
CTEventManager.register<MCPlayerLoggedInEvent>((event) => {

     // Gets the player from the event context.
     var player = event.player;
     
     // Only give the stage to the player if they don't have it already. This
     // is not required but it is considered best practice. 
     if (!player.hasGameStage("one")) {
     
         // Gives the stage "one" to the player.
         player.addGameStage("one");
         
         player.sendMessage("You logged in! Here is stage 'one'.");
     }
});