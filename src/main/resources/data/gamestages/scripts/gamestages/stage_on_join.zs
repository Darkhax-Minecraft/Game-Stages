// This example will give players the stage "one" when they log in. In the 
// context of Minecraft logging in means the player joins a save or server.
// This is done by registering an event listener which is a bit of code that
// runs every time a certain event happens. In this case we are using the 
// MCPlayerLoggedInEvent. 

// Some classes require importing in order to use them.
import crafttweaker.api.events.CTEventManager;
import crafttweaker.api.event.entity.player.MCPlayerLoggedInEvent; 

// Registers an event listener for the MCPlayerLoggedInEvent.
CTEventManager.register<MCPlayerLoggedInEvent>((event) => {

     // Gets the player from the event context.
     var player = event.player;
     
     // Checks if the player already has the stage. It doesn't hurt to give the
     // player the stage twice but you may want need other conditions depending
     // on your use case.
     if (!player.hasGameStage("one")) {
     
         // Gives the stage "one" to the player.
         player.addGameStage("one");
     }
});