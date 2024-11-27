package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;


import java.util.ArrayList;
import java.util.List;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Level1 extends state {

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
        sr = new ShapeRenderer();
        bg = new Texture("bg.jpg");
        pauseButton = new Texture("pause.png");
        redBird = new RedBird(150, 195);
        birdYellow = new YellowBird(50, 95);
        birdBlack = new BlackBird(100, 105);

        birdQueue = new ArrayList<>();
        birdQueue.add(redBird);
        birdQueue.add(birdYellow);
        birdQueue.add(birdBlack);

        currentBird = birdQueue.get(0);

        if (birdQueue.size() > 1) {
            nextBird = birdQueue.get(1);
            nextBird.x = 40; // Position beneath the slingshot
            nextBird.y = 95;
        }

        pig2 = new Pig2(1000, 100);
        pig3 = new Pig3(995, 310);

        world = new World(new Vector2(0, -9.8f), true);  // Gravity downwards, and the second parameter indicates whether bodies should be sleeping.


        slingshot = new Texture("slingshot.png");
        obstacles = new ArrayList<>();
        // Positions adjusted considering center-based Box2D coordinate system
        obstacles.add(new Obst1(world, (969), (230), 130 / 50f, 20 / 50f)); // (969, 230, 130, 20)
        obstacles.add(new Obst1(world, (969), (290), 130 / 50f, 20 / 50f)); // (969, 290, 130, 20)
        obstacles.add(new Obst4(world, (970), (260), 55 / 50f, 55 / 50f));   // (970, 250, 40, 40)
        obstacles.add(new Obst7(world, (1050), (250), 20 / 50f, 20 / 50f)); // (1050, 250, 38, 38)
        obstacles.add(new Obstst(world, (1080), (98), 20 / 50f, 130 / 50f));  // (1080, 98, 20, 130)
        obstacles.add(new Obstst(world, (970), (100), 20 / 50f, 130 / 50f));  // (970, 100, 20, 130)


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
            currentBird.x = 150 -dx;
            currentBird.y = 195 +dy;
            updateTrajectory(150, 195, -dx, -dy, speedMultiplier);
        }

        if (!Gdx.input.isTouched() && dragging) {
            dragging = false;
            velocity.x = -(touchX - 150) * 0.5f * speedMultiplier;
            velocity.y = (195 - touchY) * 0.5f * speedMultiplier;
            isLaunched = true;
            currentBird.x = initialBirdX;
            currentBird.y = initialBirdY;
        }
    }

    private void updateTrajectory(float originX, float originY, float dx, float dy, float speedMultiplier) {
        trajectoryDots.clear();
        float velocityX = -dx * speedMultiplier;
        float velocityY = dy * speedMultiplier;
        float timeStep = 0.03f;
        float time = 0;
        int maxDots = 20;

        while (trajectoryDots.size() < maxDots) {
            float x = originX + velocityX * time;
            float y = originY + velocityY * time - 0.5f * GRAVITY * time * time;

            if (x < 0 || x > Gdx.graphics.getWidth() || y < 0 || y > Gdx.graphics.getHeight()) {
                break;
            }

            trajectoryDots.add(new Vector2(x, y));
            time += timeStep;
        }
    }

    private void renderSlingshotBand(SpriteBatch sb) {
        // Slingshot band anchor points (adjust these coordinates to match your slingshot position)
        float leftAnchorX = 175;
        float leftAnchorY = 200;
        float rightAnchorX = 200;
        float rightAnchorY = 205;

        // If dragging, use current touch position
        if (dragging) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            float birdX = 150 + (touchX - 175) * 0.5f;
            float birdY = 195 - (200 - touchY) * 0.99f;
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
        }
    }


    private void checkCollisions() {
        Rectangle birdBounds = currentBird.getBounds();

        // Check collision with pigs
        for (Pigs pig : pigs) {
            if (!pig.damaged && birdBounds.overlaps(pig.getBounds())) {
                pig.damage(); // Mark pig as damaged
                isLaunched = false; // Stop the bird after hitting the pig
                pigDamageTime = 1f; // Start timer to remove pig after 1 second
            }
        }

        // Check collision with obstacles
        for (Obstacles obstacle : obstacles) {
            if (birdBounds.overlaps(obstacle.getBounds())) {
                System.out.println("Collision detected with obstacle: " + obstacle); // Debug output

                // Check if the obstacle is currently static
                if (obstacle.body.getType() == BodyDef.BodyType.StaticBody) {
                    // Change the body type to dynamic to allow it to fall
                    obstacle.body.setType(BodyDef.BodyType.DynamicBody);

                    // Debug output to confirm the change
                    System.out.println("Obstacle type after change: " + obstacle.body.getType());
                }

                // Optionally, reset the bird's position or state after collision
                isLaunched = false;
                currentBird.x = initialBirdX; // Reset position if needed
                currentBird.y = initialBirdY;
            }
        }
    }
    private void destroyPig(Pigs pig) {
        pig.setTexture(new Texture("pig_damaged.png")); // Change to destroyed pig texture
        // Add more logic here if required, like animations or score increment
    }

    private void switchToNextBird() {
        isLaunched = false;
        velocity.set(0, 0);

        // Remove the current bird from the queue
        birdQueue.remove(0);

        // Check if there are more birds
        if (!birdQueue.isEmpty()) {
            // Set the next bird to the slingshot
            currentBird = birdQueue.get(0);
            currentBird.x = 150;  // Slingshot primary position
            currentBird.y = 195;

            // If more than one bird left, position the next bird beneath
            if (birdQueue.size() > 1) {
                nextBird = birdQueue.get(1);
                nextBird.x = 40;
                nextBird.y = 95;
            } else {
                nextBird = null;
            }
        } else {
            // No more birds
            if (!arePigsDestroyed()) {
                GameLost = true;
            } else {
                GameWon = true;
            }
        }
    }

    private boolean arePigsDestroyed() {
        return pigs.isEmpty() || pigs.stream().allMatch(pig -> pig.damaged);
    }

    @Override
    public void update(float dt) {
        world.step(dt, 6, 2);
        if (GameWon || GameLost) return;

        handleInput();

        if (isLaunched) {
            currentBird.x += velocity.x * dt;
            currentBird.y += velocity.y * dt;

            velocity.y -= GRAVITY * dt;


            if (currentBird.y < 0 || currentBird.x < 0 || currentBird.x > Gdx.graphics.getWidth()) {
                switchToNextBird();
            }

            checkCollisions(); // Check for collisions after updating bird position
        }

        if (pigDamageTime > 0) {
            pigDamageTime -= dt;
            if (pigDamageTime <= 0) {
                pigs.removeIf(pig -> pig.damaged); // Remove damaged pigs after the delay
                switchToNextBird(); // Switch to the next bird after removing the pig
            }
        }

        obstacles.removeIf(obstacle -> obstacle.isToRemove());

        currentBird.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(pauseButton, 10, Gdx.graphics.getHeight() - 90, 80, 80);
        sb.draw(slingshot, 130, 100, 120, 120);
        sb.end();
        renderSlingshotBand(sb);

        sb.begin();
        currentBird.render(sb);

        if (nextBird != null) {
            nextBird.render(sb);
        }

        for (Pigs pig : pigs) {
            pig.render(sb); // Render only pigs that are not destroyed
        }

        obstacle1A.render(sb);
        obstacle1B.render(sb);
        obstacle4.render(sb);
        obstacle7.render(sb);
        obstaclestA.render(sb);
        obstaclestB.render(sb);

        if (dragging) {
            for (Vector2 dotPosition : trajectoryDots) {
                sb.draw(dot, dotPosition.x, dotPosition.y, 10, 10);
            }
        }
        sb.end();
    }
}
