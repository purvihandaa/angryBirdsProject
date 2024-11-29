package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Obst2 extends Obstacles {

    // Define a hit threshold for this obstacle
    private static final int HIT_THRESHOLD = 1; // Example threshold

    private static final float DENSITY = 0.5f; // Example density
    private static final float RESTITUTION = 0.2f; // Example restitution
    private static final float FRICTION = 0.4f; // Example friction

    public Obst2(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height, HIT_THRESHOLD, DENSITY, RESTITUTION, FRICTION);
        setTexture(new Texture("obj2.png"));  // Set the texture for this obstacle
    }
    // You can add additional methods or overrides specific to Obst4 if needed
}
