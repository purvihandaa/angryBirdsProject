GameContactListener listens for collisions between objects in the game world (Birds, Pigs, Obstacles).
It processes Bird-Pig, Bird-Obstacle, and Pig-Obstacle collisions, applying damage to the involved objects.
When a collision happens, it checks the object types and triggers appropriate damage handling.
Bird-Pig collision triggers pig damage and removal if the damage threshold is reached.
Bird-Obstacle collision triggers obstacle damage and removal if the damage threshold is reached.
Pig-Obstacle collision triggers damage for both the pig and the obstacle.
The activeContacts set ensures duplicate contacts are not processed.
Damage is tracked by hit counts; when the threshold is exceeded, objects are flagged for removal.
The bodiesToRemove list stores bodies that need to be removed after processing damage.
endContact clears active contacts after a collision ends.
preSolve and postSolve methods are placeholders for more advanced collision processing, currently unused.
Impulse data can be used to adjust damage based on collision severity (optional).
The class interacts with the Box2D World to handle physics and object removal.
The GameObjectState class stores the state of individual game objects such as position, velocity, and type.
GameObjectState is serialized so that game objects' states can be saved and loaded.
GameState class stores the current level, solved levels, game progress, and the list of GameObjectState objects.
GameState provides methods to save and load the entire game state to/from a file.
The saveGameState method serializes the current game state into a file for later use.
The loadGameState method deserializes a saved game state from a file and restores the game progress.
GameState can be used to track the player's progress and restore the game to a specific state after closing or crashing.
Both GameObjectState and GameState ensure the game data is preserved across sessions.
