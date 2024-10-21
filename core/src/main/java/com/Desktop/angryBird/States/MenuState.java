package com.Desktop.angryBird.States;

import com.Desktop.angryBird.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends state {
    private Texture background;
    private Texture Startgame;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background= new Texture("4.jpg");
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background,0,0, Main.WIDTH, Main.HEIGHT);
        sb.end();


    }
}
