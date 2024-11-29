package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

public class Level2 extends state {
    private Box2DDebugRenderer debugRenderer;
    private GameContactListener contactListener;

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
    private Obst1 obstacle1C;
    private Obst4 obstacle4;
    private Obst7 obstacle7;
    //private Obst4 obstacle4;
    private Obstst obstaclestC;
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


    public Level2(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new GameContactListener(world);
        world.setContactListener(contactListener);
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
        pig2.body.setUserData(pig2);

        pig3 = new Pig3(world,990, 310);
        pig3.body.setUserData(pig3);

        slingshot = new Texture("slingshot.png");
        obstacles = new ArrayList<>();

        obstacles.add(new Obst1(world, (985), (235), 130 / PPM, 18 / PPM)); // wooden horizontal (969, 230, 130, 20)
        obstacles.add(new Obst1(world, (985), (295), 130 / PPM, 18 / PPM)); // (969, 290, 130, 20)
        obstacles.add(new Obst1(world, (885), (235), 130 / PPM, 18 / PPM)); // (969, 290, 130, 20)
        obstacles.add(new Obst4(world, (200), (260), 55 / PPM, 55 / PPM));  // (970, 250, 40, 40)
        obstacles.add(new Obst7(world, (990), (265), 40 / PPM, 40 / PPM));  // (1050, 250, 38, 38)
        obstacles.add(new Obstst(world, (1035), (160), 20 / PPM, 130 / PPM));  // GLASS VERTICAL (1035, 100, 20, 130)
        obstacles.add(new Obstst(world, (940), (160), 20 / PPM, 130 / PPM));   // (970, 100, 20, 130)
        obstacles.add(new Obstst(world, (845), (160), 20 / PPM, 130 / PPM));   // (970, 100, 20, 130)
        obstacles.add(new Obstst(world, (800), (290), 20 / PPM, 130 / PPM));   // (970, 100, 20, 130)
        obstacles.add(new Obstst(world, (1000), (290), 20 / PPM, 130 / PPM));   // (970, 100, 20, 130)

// Initialize and set user data for all obstacles
        obstacle1A = (Obst1) obstacles.get(0);
        obstacle1A.body.setUserData(obstacle1A);

        obstacle1B = (Obst1) obstacles.get(1);
        obstacle1B.body.setUserData(obstacle1B);

        obstacle1C = (Obst1) obstacles.get(2);
        obstacle1C.body.setUserData(obstacle1C);

        obstacle4 = (Obst4) obstacles.get(3);
        obstacle4.body.setUserData(obstacle4);

        obstacle7 = (Obst7) obstacles.get(4);
        obstacle7.body.setUserData(obstacle7);

        obstaclestA = (Obstst) obstacles.get(5);
        obstaclestA.body.setUserData(obstaclestA);

        obstaclestB = (Obstst) obstacles.get(6);
        obstaclestB.body.setUserData(obstaclestB);

        obstaclestC = (Obstst) obstacles.get(7);
        obstaclestC.body.setUserData(obstaclestC);





        dot = new Texture("dots.png");

        trajectoryDots = new ArrayList<>();

        pigs = new ArrayList<>();
        pigs.add(pig2);
        pigs.add(pig3);

        createGround();
        createCeiling();
        createSideWalls();
    }



    @Override
    protected void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        final float speedMultiplier = 20.0f;
        final float slingshotCenterX = 150;
        final float slingshotCenterY = 195;

        if (Gdx.input.justTouched()) {

            if (touchX < 100 && touchY > Gdx.graphics.getHeight() - 100) {
                gsm.push(new PauseState2(gsm, this));
                return;
            }
            if (!isLaunched && currentBird.getBounds().contains(touchX, touchY)) {
                dragging = true;
                initialBirdX = currentBird.x;
                initialBirdY = currentBird.y;


            }
        }

        if (dragging && Gdx.input.isTouched()) {
            // Directly calculate drag using touch coordinates
            float dx = slingshotCenterX - touchX;  // Reversed to match slingshot mechanics
            float dy = slingshotCenterY - touchY;  // Reversed to match slingshot mechanics

            float dragDistance = (float) Math.sqrt(dx * dx + dy * dy);

            if (dragDistance > maxDragDistance) {
                float angle = (float) Math.atan2(dy, dx);
                dx = (float) (maxDragDistance * Math.cos(angle));
                dy = (float) (maxDragDistance * Math.sin(angle));
            }

            // Move bird based on drag
            currentBird.body.setTransform((slingshotCenterX - dx) / PPM, (slingshotCenterY - dy) / PPM, 0);
            updateTrajectory(slingshotCenterX, slingshotCenterY, -dx, -dy, speedMultiplier);
        }

        if (!Gdx.input.isTouched() && dragging) {
            dragging = false;
            float dx = slingshotCenterX - touchX;
            float dy = slingshotCenterY - touchY;

            // Calculate velocity
            velocity.x = dx * 0.5f * speedMultiplier / PPM;  // Remove negative sign
            velocity.y = dy * 0.5f * speedMultiplier / PPM;  // Remove negative sign
            isLaunched = true;

            // Launch the bird
            currentBird.launch(new Vector2(velocity.x, velocity.y));
            currentBird.x = initialBirdX;
            currentBird.y = initialBirdY;
        }
    }


    private void updateTrajectory(float originX, float originY, float dx, float dy, float speedMultiplier) {
        trajectoryDots.clear();
        // Calculate velocity the same way as in the launch code
        float velocityX = -dx * 0.5f * speedMultiplier / PPM;
        float velocityY = dy * 0.5f * speedMultiplier / PPM;
        float timeStep = 0.1f;  // Increased time step for more spread out dots
        float time = 0;
        int maxDots = 20;
        float gravity = -9.8f;  // Match the world gravity


        while (trajectoryDots.size() < maxDots) {
            // Convert the Box2D velocities back to screen coordinates for visualization
            float x = originX + (velocityX * PPM) * time;
            float y = originY + (velocityY * PPM) * time + (0.5f * gravity * time * time * PPM);

            if (x < 0 || x > Gdx.graphics.getWidth() || y < 0 || y > Gdx.graphics.getHeight()) {
                break;
            }

            trajectoryDots.add(new Vector2(x, y));
            time += timeStep;
        }

    }

    private void renderSlingshotBand(SpriteBatch sb) {
        float leftAnchorX = 175;
        float leftAnchorY = 200;
        float rightAnchorX = 200;
        float rightAnchorY = 205;
        float slingshotCenterX = 150;
        float slingshotCenterY = 195;

        if (dragging) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Calculate drag distance from slingshot center
            float dx = touchX - slingshotCenterX;
            float dy = slingshotCenterY - touchY;  // Invert dy calculation

            // Limit drag distance
            float dragDistance = (float) Math.sqrt(dx * dx + dy * dy);
            if (dragDistance > maxDragDistance) {
                float angle = (float) Math.atan2(dy, dx);
                dx = (float) (maxDragDistance * Math.cos(angle));
                dy = (float) (maxDragDistance * Math.sin(angle));
                touchX = slingshotCenterX + dx;
                touchY = slingshotCenterY - dy;
            }

            // Bird position is offset from slingshot center by drag
            float birdX = slingshotCenterX + dx;
            float birdY = slingshotCenterY - dy;

            // Draw elastic band
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(new Color(0.4f, 0.2f, 0.05f, 1.0f));

            // Draw bands to the bird's position
            sr.rectLine(leftAnchorX, leftAnchorY, birdX, birdY, 5);
            sr.rectLine(rightAnchorX, rightAnchorY, birdX, birdY, 5);
            sr.end();

            // Update trajectory
            updateTrajectory(slingshotCenterX, slingshotCenterY, dx, dy, 20.0f);

            // Render the bird
            sb.begin();
            currentBird.render(sb);
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

    private void createCeiling() {
        BodyDef ceilingBodyDef = new BodyDef();
        ceilingBodyDef.position.set(SCREEN_WIDTH / 2 / PPM, (SCREEN_HEIGHT) / PPM); // Centered on the screen
        Body ceilingBody = world.createBody(ceilingBodyDef);

        // Create a box shape for the ceiling
        PolygonShape ceilingBox = new PolygonShape();
        ceilingBox.setAsBox(SCREEN_WIDTH / 2 / PPM, 5 / PPM); // Width and height of the ceiling

        // Create fixture for the ceiling
        ceilingBody.createFixture(ceilingBox, 0.0f);
        ceilingBox.dispose(); // Dispose of the shape after use
    }

    private void createSideWalls() {
        // Create left wall
        BodyDef leftWallBodyDef = new BodyDef();
        leftWallBodyDef.position.set(0 / PPM, SCREEN_HEIGHT / 2 / PPM); // Left side
        Body leftWallBody = world.createBody(leftWallBodyDef);

        PolygonShape leftWallBox = new PolygonShape();
        leftWallBox.setAsBox(1 / PPM, SCREEN_HEIGHT / 2 / PPM); // Thin wall covering the height of the screen
        leftWallBody.createFixture(leftWallBox, 0.0f);
        leftWallBox.dispose(); // Dispose of the shape after use

        // Create right wall
        BodyDef rightWallBodyDef = new BodyDef();
        rightWallBodyDef.position.set(SCREEN_WIDTH / PPM, SCREEN_HEIGHT / 2 / PPM); // Right side
        Body rightWallBody = world.createBody(rightWallBodyDef);

        PolygonShape rightWallBox = new PolygonShape();
        rightWallBox.setAsBox(1 / PPM, SCREEN_HEIGHT / 2 / PPM); // Thin wall covering the height of the screen
        rightWallBody.createFixture(rightWallBox, 0.0f);
        rightWallBox.dispose(); // Dispose of the shape after use
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
                gsm.push(new LoseState2(gsm, this));
            }
        }
    }


    private boolean arePigsDestroyed() {
        return pigs.isEmpty();
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
                BirdsToDestroy = currentBird.reset();
                switchToNextBird();
            }
            world.step(1 / 60f, 6, 2); // Advance physics simulation
//            checkCollisions(); // Check for collisions after updating bird position
        }

//

        for (Body body : BirdsToDestroy) {
            world.destroyBody(body);
        }
        BirdsToDestroy.clear();

        // Update pigs
        for (Pigs pig : pigs) {
            pig.update(dt);
        }



        if (arePigsDestroyed()) {
            gsm.push(new WinState1(gsm, this)); // Transition to win state
        }

        world.step(dt, 6, 2);

        // Process bodies marked for removal
        List<Body> bodiesToRemove = contactListener.getBodiesToRemove();
        for (Body body : bodiesToRemove) {
            if (body != null && !world.isLocked()) {
                Object userData = body.getUserData();
                if (userData instanceof Pigs) {
                    Pigs pig = (Pigs) userData;
                    pigs.remove(pig);  // Remove from your game's pig list
                    pig.dispose();     // Dispose of the pig's resources
                } else if (userData instanceof Obstacles) {
                    Obstacles obstacle = (Obstacles) userData;
                    obstacles.remove(obstacle);  // Remove from your game's obstacle list
                    obstacle.dispose(world);          // Dispose of the obstacle's resources
                }
                world.destroyBody(body);
            }
        }
        contactListener.clearBodiesToRemove();
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
        for (Obstacles obstacle : obstacles) {
            obstacle.render(sb);
        }
        // Render trajectory dots if dragging
        if (dragging) {
            for (Vector2 dotPosition : trajectoryDots) {
                sb.draw(dot, dotPosition.x, dotPosition.y, 10, 10);
            }
        }
        sb.end();

        for (Bird bird : birdQueue) {
            if (bird instanceof BlackBird) {
                ((BlackBird) bird).renderShapes();
            }
        }

        debugRenderer.render(world, sb.getProjectionMatrix().cpy().scale(PPM, PPM, 1));

    }
}

