package com.Desktop.angryBird;

import com.Desktop.angryBird.States.GameStateManager;
import com.Desktop.angryBird.States.MenuState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Main extends ApplicationAdapter {
    public static final int WIDTH = 1200;
    public static final int HEIGHT= 700;


    public static final String TITLE = "Angry Birds";
    private GameStateManager gsm;
    private SpriteBatch batch;



    public void create(){
        batch=new SpriteBatch();
        gsm= new GameStateManager();
        Gdx.gl.glClearColor(1,0,0,1);
        gsm.push(new MenuState(gsm));

    }

    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);

    }
}
