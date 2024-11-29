package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


import java.util.ArrayList;
import java.util.List;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Level1 extends state {
    private Box2DDebugRenderer debugRenderer;

    private ShapeRenderer sr;
    private Texture bg;
    private Texture pauseButton;

    private RedBird redBird;
    private YellowBird birdYellow;
    private BlackBird birdBlack;
    private Bird currentBird;
    private List<Bird> birdQueue;
    private Bird nextBird;

    private Pig2 pig2;
    private Pig3 pig3;
    private Texture slingshot;

    private Obst1 obstacle1A;
    private Obst1 obstacle1B;
    private Obst4 obstacle4;
    private Obst7 obstacle7;
    private Obstst obstaclestA;
    private Obstst obstaclestB;

    private Texture dot;

    private World world;

    private boolean dragging = false;
    private boolean GameWon = false;
    private boolean GameLost = false;

    private List<Vector2> trajectoryDots;
    private final float GRAVITY = 9.8f;

    private Vector2 velocity = new Vector2(0, 0);
    private boolean isLaunched = false;

    private List<Pigs> pigs; // List of pigs for easier collision checks
    private float pigDamageTime = 0f; // Timer for pig damage removal
    private float birdChangeTime = 1f; // Timer for switching to next bird after one second
    private float initialBirdX, initialBirdY;
    private float maxDragDistance = 100f;


    private List<Obstacles> obstacles;


    public Level1(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.8f), true);  // Gravity downwards, and the second parameter indicates whether bodies should be sleeping.
        debugRenderer = new Box2DDebugRenderer();

        sr = new ShapeRenderer();
        bg = new Texture("bg.jpg");
        pauseButton = new Texture("pause.png");
        redBird = new RedBird( world,190, 205);
        birdYellow = new YellowBird(world,130, 135);
        birdBlack = new BlackBird(world,70, 140);

        birdQueue = new ArrayList<>();
        birdQueue.add(redBird);
        birdQueue.add(birdYellow);
        birdQueue.add(birdBlack);

        currentBird = birdQueue.get(0);

        if (birdQueue.size() > 1) {
            nextBird = birdQueue.get(1);
        }

        pig2 = new Pig2(world,1000, 100);
        pig3 = new Pig3(world,995, 310);

        slingshot = new Texture("slingshot.png");
        obstacles = new ArrayList<>();
        // Positions adjusted considering center-based Box2D coordinate system
        obstacles.add(new Obst1(world, (969), (230), 130 / PPM, 20 / PPM)); // (969, 230, 130, 20)
        obstacles.add(new Obst1(world, (969), (290), 130 / PPM, 20 / PPM)); // (969, 290, 130, 20)
        obstacles.add(new Obst4(world, (970), (260), 55 / PPM, 55 / PPM));   // (970, 250, 40, 40)
        obstacles.add(new Obst7(world, (1050), (250), 20 / PPM, 20 /PPM)); // (1050, 250, 38, 38)
        obstacles.add(new Obstst(world, (1080), (98), 20 / PPM, 130 / PPM));  // (1080, 98, 20, 130)
        obstacles.add(new Obstst(world, (970), (100), 20 / PPM, 130 / PPM));  // (970, 100, 20, 130)


        obstacle1A = (Obst1) obstacles.get(0);  // Initialize obstacle1A from the list
        obstacle1B = (Obst1) obstacles.get(1);  // Initialize obstacle1B from the list
        obstacle4 = (Obst4) obstacles.get(2);   // Initialize obstacle4
        obstacle7 = (Obst7) obstacles.get(3);   // Initialize obstacle7
        obstaclestA = (Obstst) obstacles.get(4); // Initialize obstaclestA
        obstaclestB = (Obstst) obstacles.get(5); // Initialize obstaclestB

        dot = new Texture("dots.png");

        trajectoryDots = new ArrayList<>();

        pigs = new ArrayList<>();
        pigs.add(pig2);
        pigs.add(pig3);

        createGround();
    }



    @Override
    protected void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        final float speedMultiplier = 20.0f;

        if (Gdx.input.justTouched()) {
            if (touchX < 100 && touchY > Gdx.graphics.getHeight() - 100) {
                gsm.push(new PauseState(gsm, this));
                return;
            }
            if (!isLaunched && currentBird.getBounds().contains(touchX, touchY)) {
                dragging = true;
                initialBirdX = currentBird.x;
                initialBirdY = currentBird.y;
            }
        }

        if (dragging && Gdx.input.isTouched()) {
            float dx = 150 - touchX;  // Reversed direction
            float dy = touchY - 195;  // Reversed direction
            float dragDistance = (float) Math.sqrt(dx * dx + dy * dy);
            if (dragDistance > maxDragDistance) {
                float angle = (float) Math.atan2(dy, dx);
                dx = (float) (maxDragDistance * Math.cos(angle));
                dy = (float) (maxDragDistance * Math.sin(angle));
            }

            // Move bird back based on drag
            currentBird.body.setTransform((150 - dx) / PPM, (195 + dy) / PPM, 0); // Update bird's position
            updateTrajectory(150, 195, -dx, -dy, speedMultiplier);

        }

        if (!Gdx.input.isTouched() && dragging) {
            dragging = false;
            velocity.x = -(touchX - 150) * 0.5f * speedMultiplier/PPM;
            velocity.y = (195 - touchY) * 0.5f * speedMultiplier/PPM;
            isLaunched = true;

            // Launch the bird
            currentBird.launch(new Vector2(velocity.x, velocity.y));
            // Reset position to slingshot
            currentBird.x = initialBirdX;
            currentBird.y = initialBirdY;
        }
    }
    private void updateTrajectory(float originX, float originY, float dx, float dy, float speedMultiplier) {
        trajectoryDots.clear();

        // Initial velocities in Box2D units
        float velocityX = -dx * speedMultiplier / PPM;
        float velocityY = dy * speedMultiplier / PPM;

        // Convert origin to Box2D units
        float startX = originX / PPM;
        float startY = originY / PPM;

        float gravity = GRAVITY / PPM; // Gravity in Box2D units
        float timeStep = 0.1f;         // Time step for calculations
        float time = 0;

        for (int i = 0; i < 20; i++) {
            // Calculate trajectory point
            float x = startX + velocityX * time;
            float y = startY + velocityY * time - 0.5f * gravity * time * time;

            // Convert to screen coordinates
            float screenX = x * PPM;
            float screenY = y * PPM;

            if (screenX < 0 || screenX > Gdx.graphics.getWidth() || screenY < 0 || screenY > Gdx.graphics.getHeight()) {
                break;
            }

            trajectoryDots.add(new Vector2(screenX, screenY));
            time += timeStep; // Increment time
        }
    }





    private void renderSlingshotBand(SpriteBatch sb) {
        float leftAnchorX = 175;
        float leftAnchorY = 200;
        float rightAnchorX = 200;
        float rightAnchorY = 205;

        // If dragging, use current touch position
        if (dragging) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Update the bird's position based on the drag
            float birdX = 150 - (touchX - 150); // Adjusted to move with slingshot
            float birdY = 195 + (touchY - 195); // Adjusted to move with slingshot
            currentBird.x = birdX;
            currentBird.y = birdY;

            // Draw elastic band
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(0.4f, 0.2f, 0.05f, 1.0f));

            // Left band
            sr.rectLine(leftAnchorX, leftAnchorY, touchX, touchY, 5);

            // Right band
            sr.rectLine(rightAnchorX, rightAnchorY, touchX, touchY, 5);

            sr.end();

            // Render the bird at its updated position
            sb.begin();
            currentBird.render(sb); // Render the current bird at the updated position
            sb.end();
        }
    }

    private static final float GROUND_Y = 50f; // Define the ground level
    private static final float SCREEN_WIDTH = 1200f; // Define screen width
    private static final float SCREEN_HEIGHT = 750f; // Define screen height

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(SCREEN_WIDTH / 2 / PPM, GROUND_Y / PPM); // Centered on the screen
        Body groundBody = world.createBody(groundBodyDef);

        // Create a box shape for the ground
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(SCREEN_WIDTH / 2 / PPM, 50 / PPM); // Width and height of the ground

        // Create fixture for the ground
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose(); // Dispose of the shape after use
    }


    private void checkCollisions() {
        Rectangle birdBounds = currentBird.getBounds();

        // Check collision with pigs
        for (Pigs pig : pigs) {
            if (!pig.damaged && birdBounds.overlaps(pig.getBounds())) {
                pig.damage(); // Mark pig as damaged
                pigDamageTime = 1f;
                pig.isDisposed = true;
                // Start timer to remove pig after 1 second
            }
        }

        // Check collision with obstacles
        for (Obstacles obstacle : obstacles) {
            if (birdBounds.overlaps(obstacle.getBounds())) {
                obstacle.isDisposed = true;
                System.out.println("Collision detected with obstacle: " + obstacle);

                if (obstacle.body.getType() == BodyDef.BodyType.StaticBody) {
                    // Change the body type to dynamic
                    obstacle.body.setType(BodyDef.BodyType.DynamicBody);
                    System.out.println("Obstacle type after change: " + obstacle.body.getType());

                    float impulseX = 5f; // Adjusted impulse to the right
                    float impulseY = 5f; // Adjusted upward impulse
                    obstacle.body.applyLinearImpulse(new Vector2(impulseX, impulseY), obstacle.body.getWorldCenter(), true);
                }
            }

            // Check if the obstacle is below the ground level
            if (obstacle.body.getPosition().y < GROUND_Y / PPM) {
                // Set the obstacle's position to the ground level
                obstacle.body.setTransform(obstacle.body.getPosition().x, GROUND_Y / PPM, 0);
                obstacle.body.setLinearVelocity(0, 0); // Stop any further movement
            }

            // Prevent obstacles from going off-screen
            if (obstacle.body.getPosition().x < 0) {
                obstacle.body.setTransform(0, obstacle.body.getPosition().y, 0); // Reset to left boundary
            } else if (obstacle.body.getPosition().x > SCREEN_WIDTH / PPM) {
                obstacle.body.setTransform(SCREEN_WIDTH / PPM, obstacle.body.getPosition().y, 0); // Reset to right boundary
            }

            // Limit the velocity of the obstacle to prevent it from flying off-screen
            limitObstacleVelocity(obstacle);
        }

    }

    private void limitObstacleVelocity(Obstacles obstacle) {
        Vector2 velocity = obstacle.body.getLinearVelocity();
        float maxVelocity = 10f; // Set a maximum velocity

        if (velocity.len() > maxVelocity) {
            velocity.nor().scl(maxVelocity); // Normalize and scale to max velocity
            obstacle.body.setLinearVelocity(velocity);
        }
    }

    private void destroyPig(Pigs pig) {
        pig.setTexture(new Texture("pig_damaged.png")); // Change to destroyed pig texture
        // Add more logic here if required, like animations or score increment
    }

    private void switchToNextBird() {
        currentBird.body.setLinearVelocity(0, 0);
        birdQueue.remove(0);


        // Check if there are more birds
        if (!birdQueue.isEmpty()) {
            // Set the next bird to the slingshot
            currentBird = birdQueue.get(0);

            // Reset bird position to slingshot
            currentBird.body.setTransform(190 / PPM, 210 / PPM, 0);
            currentBird.body.setLinearVelocity(0, 0); // Ensure the bird is stationary

            // Reset the dragging and launched state
            dragging = false;
            isLaunched = false;

            // If more than one bird left, position the next bird beneath
            if (birdQueue.size() > 1) {
                nextBird = birdQueue.get(1);

            }
            else {
                nextBird = null; // No more birds left
            }
        } else {
            // No more birds
            if (!arePigsDestroyed()) {
                // If there are pigs left, transition to LoseState
                gsm.push(new LoseState(gsm, this));
            }
        }
    }


    private boolean arePigsDestroyed() {
        return pigs.isEmpty() || pigs.stream().allMatch(pig -> pig.isReadyToRemove());
    }

    private void disposeBodies() {
        // Dispose birds
        for (int i = birdQueue.size() - 1; i >= 0; i--) {
            Bird bird = birdQueue.get(i);
            if (bird.isDisposed) {
                world.destroyBody(bird.body); // Remove body from physics world
                birdQueue.remove(i);         // Remove bird from queue
            }
        }

        // Dispose pigs
        for (int i = pigs.size() - 1; i >= 0; i--) {
            Pigs pig = pigs.get(i);
            if (pig.isDisposed) {
                world.destroyBody(pig.body);
                pigs.remove(i);
            }
        }

        // Dispose obstacles
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacles obstacle = obstacles.get(i);
            if (obstacle.isDisposed) {
                world.destroyBody(obstacle.body);
                obstacles.remove(i);
            }
        }
    }



    @Override
    public void update(float dt) {
        world.step(dt, 6, 2);
        if (GameWon || GameLost) return;
        List<Body> BirdsToDestroy = new ArrayList<>();

        handleInput();

        if (isLaunched) {
            // Update the bird's position based on its physics body
            currentBird.update(dt);

            // Check if the bird stops (velocity approximately zero) or goes off-screen
            if (currentBird.body.getLinearVelocity().len2() < 0.01f || currentBird.isOutOfBounds()) {
                BirdsToDestroy=currentBird.reset();
                switchToNextBird();
            }
            world.step(1 / 60f, 6, 2); // Advance physics simulation
            checkCollisions(); // Check for collisions after updating bird position
        }

        // Update obstacles and check if they go off-screen
        for (Obstacles obstacle : obstacles) {
            if (obstacle.body.getPosition().y < 0) {
                obstacle.body.setTransform(obstacle.body.getPosition().x, 0, 0); // Reset to ground level
                obstacle.body.setLinearVelocity(0, 0); // Stop any further movement
            }
        }
        for(Body body:BirdsToDestroy) {
            world.destroyBody(body);
        }
        BirdsToDestroy.clear();
        for (Pigs pig : pigs) {
            pig.update(dt);
        }

        if (pigDamageTime > 0) {
            pigDamageTime -= dt;
            if (pigDamageTime <= 0) {
                pigs.removeIf(pig -> pig.damaged); // Remove damaged pigs after the delay
                // Check for win condition after removing pigs
                if (arePigsDestroyed()) {
                    gsm.push(new WinState(gsm, this));
                }
            }
        }

        obstacles.removeIf(obstacle -> obstacle.isToRemove());
        disposeBodies();
    }



    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(pauseButton, 10, Gdx.graphics.getHeight() - 90, 80, 80);
        sb.draw(slingshot, 130, 100, 120, 120);
        sb.end();

        // Render the slingshot band and update the bird's position if dragging
        renderSlingshotBand(sb);

        sb.begin();
        // Render the current bird at its updated position
        for (Bird bird : birdQueue) {
            bird.render(sb);
        }

        // Render pigs
        for (Pigs pig : pigs) {
            pig.render(sb); // Render only pigs that are not destroyed
        }

        // Render obstacles
        obstacle1A.render(sb);
        obstacle1B.render(sb);
        obstacle4.render(sb);
        obstacle7.render(sb);
        obstaclestA.render(sb);
        obstaclestB.render(sb);

        // Render trajectory dots if dragging
        if (dragging) {
            for (Vector2 dotPosition : trajectoryDots) {
                sb.draw(dot, dotPosition.x, dotPosition.y, 10, 10);
            }
        }
        sb.end();

        debugRenderer.render(world, sb.getProjectionMatrix().cpy().scale(PPM, PPM, 1));

    }
}
