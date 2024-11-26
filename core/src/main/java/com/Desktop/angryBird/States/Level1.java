package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Level1 extends state {
    private Texture bg;
    private Texture pauseButton; // Pause button texture

    private RedBird redBird;
    private YellowBird birdYellow;
    private BlackBird birdBlack;

    private Pig1 pig1;
    private Pig2 pig2;
    private Pig3 pig3;
    private Texture slingshot;

    private Obst1 obstacle1A;
    private Obst1 obstacle1B;
    private Obst4 obstacle4;
    private Obst7 obstacle7;
    private Obstst obstaclestA;
    private Obstst obstaclestB;

    private Texture dot; // Dot for trajectory

    private boolean dragging = false;
    private boolean GameWon = false;
    private boolean GameLost = false;

    private List<Vector2> trajectoryDots; // Store trajectory points
    private final float GRAVITY = 9.8f; // Simulate gravity

    private Vector2 velocity = new Vector2(0, 0); // The bird's current velocity
    private boolean isLaunched = false;  // Track if the bird is launched

    public Level1(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.jpg");
        pauseButton = new Texture("pause.png"); // Load pause button texture
        redBird = new RedBird(150, 195);
        birdYellow = new YellowBird(40, 95);
        birdBlack = new BlackBird(100, 105);
        pig2 = new Pig2(1000, 100);
        pig3 = new Pig3(1000 - 5, 310);
        slingshot = new Texture("slingshot.png");
        obstacle1A = new Obst1(969, 230, 130, 20);
        obstacle1B = new Obst1(969, 290, 130, 20);
        obstacle4 = new Obst4(970, 250, 40, 40);
        obstacle7 = new Obst7(1050, 250, 38, 38);
        obstaclestA = new Obstst(1080, 98, 20, 130);
        obstaclestB = new Obstst(970, 100, 20, 130);

        dot = new Texture("dots.png");

        trajectoryDots = new ArrayList<>();
    }

    @Override
    protected void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Speed multiplier to increase the speed of the bird
        final float speedMultiplier = 2.0f;

        if (Gdx.input.justTouched()) {
            if (touchX < 100 && touchY > Gdx.graphics.getHeight() - 100) { // Top-left corner for pause
                gsm.push(new PauseState(gsm, this)); // Push the PauseState onto the stack
                return; // Pause state handled, exit further input
            }
            if (redBird.getBounds().contains(touchX, touchY)) {
                dragging = true;
            }
        }

        if (dragging && Gdx.input.isTouched()) {
            float dx = touchX - 150; // Calculate horizontal drag
            float dy = 195 - touchY; // Calculate vertical drag

            // Update the trajectory with speed multiplier applied
            updateTrajectory(150, 195, dx, dy, speedMultiplier);
        }

        if (!Gdx.input.isTouched() && dragging) { // Drag is released
            dragging = false;

            // Calculate the velocity vector for the bird from the last drag
            velocity.x = -(touchX - 150) * 0.5f * speedMultiplier; // Horizontal velocity (reverse and scale)
            velocity.y = (195 - touchY) * 0.5f * speedMultiplier;  // Vertical velocity (reverse and scale)

            isLaunched = true; // Bird is now launched
        }
    }

    private void updateTrajectory(float originX, float originY, float dx, float dy, float speedMultiplier) {
        trajectoryDots.clear(); // Clear previous trajectory

        float velocityX = -dx * 0.5f * speedMultiplier;
        float velocityY = dy * 0.5f * speedMultiplier;
        float timeStep = 0.7f; // Increase time intervals for more spaced-out dots
        float time = 0;
        int maxDots = 20;

        while (trajectoryDots.size() < maxDots) {
            float x = originX + velocityX * time; // Horizontal position
            float y = originY + velocityY * time - 0.5f * GRAVITY * time * time; // Vertical position with gravity applied

            // Stop adding dots if they go off-screen
            if (x < 0 || x > Gdx.graphics.getWidth() || y < 0 || y > Gdx.graphics.getHeight()) {
                break;
            }

            trajectoryDots.add(new Vector2(x, y)); // Store the trajectory point
            time += timeStep;
        }
    }

    @Override
    public void update(float dt) {
        if (GameWon || GameLost) return;

        handleInput();

        if (isLaunched) {
            redBird.x += velocity.x * dt;
            redBird.y += velocity.y * dt;

            // Apply gravity
            velocity.y -= GRAVITY * dt;

            if (redBird.y < 0 || redBird.x < 0 || redBird.x > Gdx.graphics.getWidth()) {
                isLaunched = false;
            }
        }

        redBird.update(dt); // Update RedBird's state
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(pauseButton, 10, Gdx.graphics.getHeight() - 90, 80, 80); // Render pause button at top-left
        sb.draw(slingshot, 130, 100, 120, 120);

        redBird.render(sb);
        birdBlack.render(sb);
        birdYellow.render(sb);

        pig3.render(sb);
        pig2.render(sb);

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
