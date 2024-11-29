package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.physics.box2d.World;

public class Pig3 extends Pigs {
    private static final int HIT_THRESHOLD = 3; // Example threshold for Pig1

    public Pig3(World world, float x, float y) {
        super(world, x, y, "pig3.png", 65, 75, HIT_THRESHOLD); // Pass the hit threshold to the superclass
    }

    @Override
    public void update(float dt) {
        super.update(dt); // Call the superclass update method

        // Additional update logic specific to Pig1 can be added here if needed
    }
}
