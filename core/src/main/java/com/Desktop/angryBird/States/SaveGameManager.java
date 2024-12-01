package com.Desktop.angryBird.States;
import com.Desktop.angryBird.Sprites.*;
import com.Desktop.angryBird.States.BaseLevel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SaveGameManager {
    private static final String SAVE_FILE = "savedgame.json";

    // Serializable class to represent the game state
    public static class GameState {
        // Detailed Bird State
        public static class BirdState {
            public String type;
            public float x, y;
            public float width, height;
            public int power;
            public float angle;
            public float velocityX, velocityY;
            public boolean isLaunched;
            public boolean isDisposed;
        }

        // Updated Obstacle State to include more parameters
        public static class ObstacleState {
            public String type;
            public float x, y;
            public float width, height;
            public float angle;
            public float velocityX, velocityY;
            public boolean isDisposed;

            // Additional obstacle properties
            public int hitCounter;
            public int hitThreshold;
            public float density;
            public float restitution;
            public float friction;
        }

        // Detailed Pig State
        public static class PigState {
            public String type;
            public float x, y;
            public float width, height;
            public boolean damaged;
            public float damageTimer;
            public boolean isDisposed;

            // Additional pig properties
            public int hitCounter;
            public int hitThreshold;
        }

        public List<BirdState> birds = new ArrayList<>();
        public List<PigState> pigs = new ArrayList<>();
        public List<ObstacleState> obstacles = new ArrayList<>();
        public int currentBirdIndex;
    }

    // Method to save the current game state
    public static void saveGameState(Level1 level1) {
        GameState gameState = new GameState();

        // Save Birds (previous implementation remains the same)
        for (int i = 0; i < level1.getBirdQueue().size(); i++) {
            Bird bird = level1.getBirdQueue().get(i);
            GameState.BirdState birdState = new GameState.BirdState();

            birdState.type = bird.getClass().getSimpleName();
            birdState.x = bird.body.getPosition().x * BaseLevel.PPM;
            birdState.y = bird.body.getPosition().y * BaseLevel.PPM;
            birdState.width = bird.width * BaseLevel.PPM;
            birdState.height = bird.height * BaseLevel.PPM;
            birdState.power = bird.power;
            birdState.angle = bird.body.getAngle();
            birdState.velocityX = bird.body.getLinearVelocity().x;
            birdState.velocityY = bird.body.getLinearVelocity().y;
            birdState.isLaunched = bird.isLaunched;
            birdState.isDisposed = bird.isDisposed;

            gameState.birds.add(birdState);
        }

        // Save current bird index
        gameState.currentBirdIndex = level1.getBirdQueue().indexOf(level1.getCurrentBird());

        // Save Pigs (previous implementation remains the same)
        for (Pigs pig : level1.pigs) {
            GameState.PigState pigState = new GameState.PigState();
            pigState.type = pig.getClass().getSimpleName();
            pigState.x = pig.body.getPosition().x * BaseLevel.PPM;
            pigState.y = pig.body.getPosition().y * BaseLevel.PPM;
            pigState.width = pig.getWidth();
            pigState.height = pig.getHeight();
            pigState.damaged = pig.damaged;
            pigState.damageTimer = pig.getDamageTimer();
            pigState.isDisposed = pig.isDisposed;
            pigState.hitCounter = pig.getHitCounter();
            pigState.hitThreshold = pig.getHitThreshold();

            gameState.pigs.add(pigState);
        }

        // Save Obstacles with new details
        for (Obstacles obstacle : level1.obstacles) {
            GameState.ObstacleState obstacleState = new GameState.ObstacleState();
            obstacleState.type = obstacle.getClass().getSimpleName();
            obstacleState.x = obstacle.body.getPosition().x * BaseLevel.PPM;
            obstacleState.y = obstacle.body.getPosition().y * BaseLevel.PPM;
            obstacleState.width = obstacle.getWidth(); // Use stored width
            obstacleState.height = obstacle.getHeight(); // Use stored height
            obstacleState.angle = obstacle.body.getAngle();
            obstacleState.velocityX = obstacle.body.getLinearVelocity().x;
            obstacleState.velocityY = obstacle.body.getLinearVelocity().y;
            obstacleState.isDisposed = obstacle.isDisposed;

            // Include additional obstacle properties
            obstacleState.hitCounter = obstacle.getHitCounter();
            obstacleState.hitThreshold = obstacle.getHitThreshold();
            obstacleState.density = obstacle.getDensity();
            obstacleState.restitution = obstacle.getRestitution();
            obstacleState.friction = obstacle.getFriction();

            gameState.obstacles.add(obstacleState);
        }

        // Serialize and save to file (previous implementation remains the same)
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(gameState);

        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(json, false);

        System.out.println("Game state saved successfully!");
    }

    // Method to load the saved game state
    public static void loadGameState(Level1 level1, World world) {
        try {
            FileHandle file = Gdx.files.local(SAVE_FILE);
            if (!file.exists()) {
                System.out.println("No saved game found!");
                return;
            }

            String json = file.readString();
            Gson gson = new Gson();
            GameState gameState = gson.fromJson(json, GameState.class);

            // Clear existing objects
            level1.getBirdQueue().clear();
            level1.pigs.clear();
            level1.obstacles.clear();

            // Recreate Birds (previous implementation remains the same)
            for (GameState.BirdState birdState : gameState.birds) {
                Bird newBird;
                switch (birdState.type) {
                    case "RedBird":
                        newBird = new RedBird(world, birdState.x, birdState.y);
                        break;
                    case "YellowBird":
                        newBird = new YellowBird(world, birdState.x, birdState.y);
                        break;
                    case "BlackBird":
                        newBird = new BlackBird(world, birdState.x, birdState.y);
                        break;
                    default:
                        continue;
                }

                // Restore bird state (previous implementation remains the same)
                newBird.power = birdState.power;
                newBird.body.setTransform(birdState.x / BaseLevel.PPM, birdState.y / BaseLevel.PPM, birdState.angle);
                newBird.body.setLinearVelocity(birdState.velocityX, birdState.velocityY);
                newBird.isLaunched = birdState.isLaunched;
                newBird.isDisposed = birdState.isDisposed;

                level1.getBirdQueue().add(newBird);
            }

            // Set current bird
            level1.currentBird = level1.getBirdQueue().get(gameState.currentBirdIndex);

            // Recreate Pigs (previous implementation remains the same)
            for (GameState.PigState pigState : gameState.pigs) {
                Pigs newPig;
                switch (pigState.type) {
                    case "Pig2":
                        newPig = new Pig2(world, pigState.x, pigState.y);
                        break;
                    case "Pig3":
                        newPig = new Pig3(world, pigState.x, pigState.y);
                        break;
                    default:
                        continue;
                }

                // Restore pig state (previous implementation remains the same)
                newPig.damaged = pigState.damaged;
                newPig.setDamageTimer(pigState.damageTimer);
                newPig.isDisposed = pigState.isDisposed;
                newPig.setHitCounter(pigState.hitCounter);
                newPig.setHitThreshold(pigState.hitThreshold);

                level1.pigs.add(newPig);
            }

            // Recreate Obstacles with new constructor
            for (GameState.ObstacleState obstacleState : gameState.obstacles) {
                Obstacles newObstacle;
                switch (obstacleState.type) {
                    case "Obst1":
                        newObstacle = new Obst1(world, obstacleState.x, obstacleState.y,
                            obstacleState.width / BaseLevel.PPM,
                            obstacleState.height / BaseLevel.PPM);
                        break;
                    case "Obst7":
                        newObstacle = new Obst7(world, obstacleState.x, obstacleState.y,
                            obstacleState.width / BaseLevel.PPM,
                            obstacleState.height / BaseLevel.PPM);
                        break;
                    case "Obstst":
                        newObstacle = new Obstst(world, obstacleState.x, obstacleState.y,
                            obstacleState.width / BaseLevel.PPM,
                            obstacleState.height / BaseLevel.PPM);
                        break;
                    case "Obst4":
                        newObstacle = new Obst4(world, obstacleState.x, obstacleState.y,
                            obstacleState.width / BaseLevel.PPM,
                            obstacleState.height / BaseLevel.PPM);
                        break;
                    default:
                        continue;
                }

                newObstacle.body.setTransform(obstacleState.x / BaseLevel.PPM, obstacleState.y / BaseLevel.PPM, obstacleState.angle);
                newObstacle.body.setLinearVelocity(obstacleState.velocityX, obstacleState.velocityY);
                newObstacle.isDisposed = obstacleState.isDisposed;
                newObstacle.setHitCounter(obstacleState.hitCounter);

                level1.obstacles.add(newObstacle);
            }

            System.out.println("Game state loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void loadGameState(GameStateManager gsm) {
        try {
            World world = new World(new Vector2(0, -9.8f), true); // Create world if needed
            Level1 level1 = new Level1(gsm); // Create level instance
            loadGameState(level1, world);
        } catch (Exception e) {
            System.out.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
