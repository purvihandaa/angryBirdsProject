package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Level1 extends state {
    private Texture bg;
    private Texture pauseButton;

    private RedBird redBird;
    private YellowBird birdYellow;
    private BlackBird birdBlack;
    private Bird currentBird;

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

    public Level1(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.jpg");
        pauseButton = new Texture("pause.png");
        redBird = new RedBird(150, 195);
        birdYellow = new YellowBird(40, 95);
        birdBlack = new BlackBird(100, 105);

        currentBird = redBird; // Start with the red bird

        pig2 = new Pig2(1000, 100);
        pig3 = new Pig3(995, 310);

        slingshot = new Texture("slingshot.png");
        obstacle1A = new Obst1(969, 230, 130, 20);
        obstacle1B = new Obst1(969, 290, 130, 20);
        obstacle4 = new Obst4(970, 250, 40, 40);
        obstacle7 = new Obst7(1050, 250, 38, 38);
        obstaclestA = new Obstst(1080, 98, 20, 130);
        obstaclestB = new Obstst(970, 100, 20, 130);

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

        final float speedMultiplier = 2.0f;

        if (Gdx.input.justTouched()) {
            if (touchX < 100 && touchY > Gdx.graphics.getHeight() - 100) {
                gsm.push(new PauseState(gsm, this));
                return;
            }
            if (currentBird.getBounds().contains(touchX, touchY)) {
                dragging = true;
            }
        }

        if (dragging && Gdx.input.isTouched()) {
            float dx = touchX - 150;
            float dy = 195 - touchY;
            updateTrajectory(150, 195, dx, dy, speedMultiplier);
        }

        if (!Gdx.input.isTouched() && dragging) {
            dragging = false;
            velocity.x = -(touchX - 150) * 0.5f * speedMultiplier;
            velocity.y = (195 - touchY) * 0.5f * speedMultiplier;
            isLaunched = true;
        }
    }

    private void updateTrajectory(float originX, float originY, float dx, float dy, float speedMultiplier) {
        trajectoryDots.clear();
        float velocityX = -dx * 0.5f * speedMultiplier;
        float velocityY = dy * 0.5f * speedMultiplier;
        float timeStep = 0.7f;
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

    private void checkCollisions() {
        Rectangle birdBounds = currentBird.getBounds();

        for (Pigs pig : pigs) {
            if (!pig.damaged && birdBounds.overlaps(pig.getBounds())) {
                pig.damage(); // Mark pig as damaged
                isLaunched = false; // Stop the bird after hitting the pig
                pigDamageTime = 1f; // Start timer to remove pig after 1 second
            }
        }
    }

    private void destroyPig(Pigs pig) {
        pig.setTexture(new Texture("pig_damaged.png")); // Change to destroyed pig texture
        // Add more logic here if required, like animations or score increment
    }

    private void switchToNextBird() {
        if (currentBird instanceof RedBird) {
            currentBird = birdYellow; // Switch to yellow bird
        } else if (currentBird instanceof YellowBird) {
            currentBird = birdBlack; // Switch to black bird
        } else {
            // Game Over or Next Level
            GameWon = true; // For now, consider the level won when all birds are used
        }
    }

    @Override
    public void update(float dt) {
        if (GameWon || GameLost) return;

        handleInput();

        if (isLaunched) {
            currentBird.x += velocity.x * dt;
            currentBird.y += velocity.y * dt;

            velocity.y -= GRAVITY * dt;

            if (currentBird.y < 0 || currentBird.x < 0 || currentBird.x > Gdx.graphics.getWidth()) {
                isLaunched = false;
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

        currentBird.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(pauseButton, 10, Gdx.graphics.getHeight() - 90, 80, 80);
        sb.draw(slingshot, 130, 100, 120, 120);

        currentBird.render(sb);

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
