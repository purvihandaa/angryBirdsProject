package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.physics.box2d.World;

public class YellowBird extends Bird {

    public YellowBird(World world,float x, float y) {
        super( world,x, y, "birdYellow.png");
    }

    @Override
    public void update(float dt) {
    }
}
