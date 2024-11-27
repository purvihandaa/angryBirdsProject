package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Obst3 extends Obstacles {
    private Texture texture;
    private float width;
    private float height;

    public Obst3(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height);
        this.width = width;
        this.height = height;
        texture = new Texture("obj3.png");

        // Create the body for the obstacle (scale to Box2D world)
        createBody(x, y, width, height);
    }

    // Adjust the creation of the Box2D body to correctly scale the obstacle
    private void createBody(float x, float y, float width, float height) {
        // Here you should define your Box2D body creation logic (e.g., using a box shape)
        // Scale by PPM to convert from world coordinates to screen coordinates
        Vector2 position = new Vector2(x, y);
        // Create Box2D body (you may need to update this based on your original body creation code)
    }

    @Override
    public void render(SpriteBatch sb) {
        // Scale the texture drawing according to PPM
        sb.draw(
            texture,
            body.getPosition().x * PPM - (width * PPM) / 2,  // Scale position by PPM
            body.getPosition().y * PPM - (height * PPM) / 2,  // Scale position by PPM
            width * PPM,  // Scale the width by PPM
            height * PPM   // Scale the height by PPM
        );
    }

    public void dispose() {
        texture.dispose();
    }
}
