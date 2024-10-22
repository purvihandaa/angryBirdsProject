package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class StartState extends state {
    private Texture background;
    private Texture playbt;
    private Rectangle playButtonBounds;

    public StartState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("play.png");
        playbt = new Texture("playbt.png");

        // Define the bounds of the play button (position and size)
        playButtonBounds = new Rectangle(560, 325, 100, 60);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            // Get the touch coordinates and convert the Y-coordinate
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Check if the touch is within the play button's rectangle bounds
            if (playButtonBounds.contains(touchX, touchY)) {
                gsm.set(new MenuState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(playbt, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height);
        sb.end();
    }


}
