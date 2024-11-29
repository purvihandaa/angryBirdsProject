package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

public class YellowBird extends Bird {
    private boolean speedBoostAvailable = true;
    private final float SPEED_MULTIPLIER = 1.5f;

    public YellowBird(World world, float x, float y) {
        super(world, x, y, "birdYellow.png");
    }

    @Override
    public void update(float dt) {
        if (speedBoostAvailable && isLaunched && Gdx.input.justTouched()) {
            Vector2 currentVelocity = body.getLinearVelocity();
            body.setLinearVelocity(currentVelocity.x * SPEED_MULTIPLIER,
                currentVelocity.y * SPEED_MULTIPLIER);
            speedBoostAvailable = false;
        }
    }

    @Override
    public void launch(Vector2 velocity) {
        super.launch(velocity);
        speedBoostAvailable = true;
    }
}
