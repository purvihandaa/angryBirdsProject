package com.Desktop.angryBird.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartState extends state {
    private Texture background;
    private Texture playbt;

    public StartState(GameStateManager gsm) {
        super(gsm);
        background= new Texture("play.png");
        playbt= new Texture("playbt.png");

    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new MenuState(gsm));
        }

    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background,150,100, 300, 300);
        sb.draw(playbt,250,185, 100, 60);
        sb.end();


    }
}
