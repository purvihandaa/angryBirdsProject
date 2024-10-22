package com.Desktop.angryBird;

import com.Desktop.angryBird.States.GameStateManager;
import com.Desktop.angryBird.States.StartState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
//    public static final int WIDTH = 1200;
//    public static final int HEIGHT= 750;

    public static final String TITLE = "Angry Birds";
    private GameStateManager gsm;
    private SpriteBatch batch;

    public void create(){
        batch=new SpriteBatch();
        gsm= new GameStateManager();
        Gdx.gl.glClearColor(1,1,1,1);
        gsm.push(new StartState(gsm));


    }

    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);


    }
}
