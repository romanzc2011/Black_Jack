
BLACK JACK by Roman Campbell

Basic game of Black Jack with UI. Game works well, but after extended use, there's a glitch somewhere that automatically deals cards until player or dealer losses. Restart game if happens. I will continue to search for the cause of this behavior.

- To start game press the "Deal" button and answer prompt about bet amount.
- Just use "Hit" or "Stand" during gameplay as dealer is programmed to execute its turn as soon as player executes their turn.
- Upon winning or losing player will be prompted if they want to play again, type 'N' for No and 'Y' for yes, without ' surronding them of course.
- The glitch that appears, has to do with how the Stand button is implemented.

Implementations outside of primary scope:
- I decided to use JavaFX to create a UI, it was quite challenging as some things in the game were new learning experience. Having said that may not have     been the best time as I believe gameplay may have been affected if the rogue glitch happens.
- Keeps amount of money the players wins/loses
- Keeps track of player Wins and Loses
- To properly run project JavaFX must be installed
