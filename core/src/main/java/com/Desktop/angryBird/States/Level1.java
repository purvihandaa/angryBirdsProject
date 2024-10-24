package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Sprites.*;  // Import your RedBird class
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Level1 extends state {
    private Texture bg;
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
    private Obst10 obstacle10;
    private Obstst obstaclestA;
    private Obstst obstaclestB;

    private Texture overlay;
    private Texture pause;
    private Texture play;
    private Texture restart;
    private Texture backmenu;
    private Texture dot;  // Dot for trajectory

    private Rectangle pauseBounds;
    private Rectangle playBounds;
    private Rectangle restartBounds;
    private Rectangle backmenuBounds;
    private boolean isPaused = false;

    private Vector2[] trajectoryDots;
    private boolean dragging = false;

    private boolean GameWon = false;
    private WinState winState;

    private boolean GameLost = false;
    private LoseState loseState;

    public Level1(GameStateManager gsm) {
        super(gsm);
        bg = new Texture("bg.jpg");
        redBird = new RedBird(150, 195);
        birdYellow = new YellowBird(40,95);
        birdBlack = new BlackBird(100,105);
        pig2 = new Pig2(1000, 100);
        pig3 = new Pig3(1000 - 5, 310);
        pause = new Texture("pause.png");
        slingshot = new Texture("slingshot.png");
        obstacle1A = new Obst1(969, 230, 130, 20);
        obstacle1B = new Obst1(969, 290, 130, 20);
        obstacle4 = new Obst4(970, 250, 40, 40);
        obstacle7 = new Obst7(1050, 250, 38, 38);
        obstaclestA = new Obstst(1080, 98, 20, 130);
        obstaclestB = new Obstst(970, 100, 20, 130);


        overlay = new Texture("overlay.png");
        backmenu = new Texture("menub.png");
        play = new Texture("playb.png");
        restart = new Texture("replayb.png");
        dot = new Texture("dots.png");

        pauseBounds = new Rectangle(10, 650, 80, 80);
        playBounds = new Rectangle(440, 383, 95, 95);
        restartBounds = new Rectangle(560, 380, 95, 95);
        backmenuBounds = new Rectangle(680, 373, 100, 100);

        winState = new WinState(gsm);
        loseState = new LoseState(gsm);

        trajectoryDots = new Vector2[6];
        for (int i = 0; i < 6; i++) {
            trajectoryDots[i] = new Vector2();
        }
    }

    @Override
    protected void handleInput() {
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.justTouched()) {
            if (redBird.getBounds().contains(touchX, touchY)) {
                dragging = true;
            }
        }

        if (dragging && Gdx.input.isTouched()) {
            float dx = touchX - 150;
            float dy = 195- touchY;

            float slope = dy / dx;
            updateTrajectory(215, 215, slope);
        }

        if (!Gdx.input.isTouched()) {
            dragging = false;
        }

        if (Gdx.input.justTouched()) {
            if (pauseBounds.contains(touchX, touchY)) {
                isPaused = true;
            } else if (playBounds.contains(touchX, touchY)) {
                isPaused = false;
            } else if (restartBounds.contains(touchX, touchY)) {
                gsm.set(new Level1(gsm));
            } else if (backmenuBounds.contains(touchX, touchY)) {
                gsm.push(new MenuState(gsm));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            GameWon=true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)){
            GameLost=true;
        }
    }

    private void updateTrajectory(float originX, float originY, float slope) {
        float spacing = 30;
        float angle = (float) Math.atan(slope);

        for (int i = 0; i < trajectoryDots.length; i++) {
            float offsetX = i * spacing * (float) Math.cos(angle);
            float offsetY = i * spacing * (float) Math.sin(angle);

            trajectoryDots[i].set(originX + offsetX, originY + offsetY);
        }
    }

    @Override
    public void update(float dt) {
        if (GameWon) {
            winState.update(dt);
        } else if(GameLost) {
            loseState.update(dt);
        } else {
            handleInput();
            redBird.update(dt); // Update redBird
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(pause, pauseBounds.x, pauseBounds.y, pauseBounds.width, pauseBounds.height);
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

        if (isPaused) {
            sb.draw(overlay, 380, 330, 470, 200);
            sb.draw(play, playBounds.x, playBounds.y, playBounds.width, playBounds.height);
            sb.draw(restart, restartBounds.x, restartBounds.y, restartBounds.width, restartBounds.height);
            sb.draw(backmenu, backmenuBounds.x, backmenuBounds.y, backmenuBounds.width, backmenuBounds.height);
        }

        sb.end();

        if (GameWon) {
            winState.render(sb);
        }

        if (GameLost) {
            loseState.render(sb);
        }
    }
}
