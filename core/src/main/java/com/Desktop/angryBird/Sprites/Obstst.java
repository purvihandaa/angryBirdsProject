package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class Obstst extends Obstacles {

    // Define a hit threshold for this obstacle
    private static final int HIT_THRESHOLD = 6; // Example threshold

    // Define specific physical properties for this obstacle
    private static final float DENSITY = 2.0f; // Example density
    private static final float RESTITUTION = 0.2f; // Example restitution
    private static final float FRICTION = 2.0f; // Example friction

    public Obstst(World world, float x, float y, float width, float height) {
        // Pass the hit threshold and physical properties to the superclass
        super(world, x, y, width, height, HIT_THRESHOLD, DENSITY, RESTITUTION, FRICTION);
        setTexture(new Texture("objst.png"));  // Set the texture for this obstacle
    }

    // You can add additional methods or overrides specific to Obstst if needed
}
