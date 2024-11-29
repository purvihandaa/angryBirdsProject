package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Obstst extends Obstacles {
    private Texture texture;
    private float width;
    private float height;

    public Obstst(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height);
        this.width = width;
        this.height = height;
        texture = new Texture("objst.png");

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
