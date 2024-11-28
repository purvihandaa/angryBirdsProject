package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.physics.box2d.World;

public class BlueBird extends Bird {
    public BlueBird(World world, float x, float y) {
        super(world,x, y, "birdBlue.png");
    }

    @Override
    public void update(float dt) {
    }
}
