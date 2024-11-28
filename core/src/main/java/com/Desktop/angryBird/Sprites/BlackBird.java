package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.physics.box2d.World;

public class BlackBird extends Bird {
    public BlackBird(World world, float x, float y) {
        super(world,x, y, "birdBlack.png");
    }

    @Override
    public void update(float dt) {
    }
}
