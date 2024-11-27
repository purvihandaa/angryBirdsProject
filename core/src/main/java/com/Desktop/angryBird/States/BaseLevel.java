package com.Desktop.angryBird.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public abstract class BaseLevel extends state {
    public static final float PPM = 50f;
    protected World world;
    protected Box2DDebugRenderer b2dr;

    public BaseLevel(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.8f), true); // Standard gravity
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void update(float dt) {
        handleInput(); // Ensure input is handled during updates
        world.step(1 / 60f, 6, 2); // Box2D physics step
    }

    @Override
    public void render(SpriteBatch sb) {
        b2dr.render(world, cam.combined.scl(PPM)); // Debug renderer
    }


    public void dispose() {
        world.dispose();
        b2dr.dispose();
    }

    @Override
    protected void handleInput() {
        // Placeholder for handling user input
        // You can add level-specific input handling here if needed.
    }
}
