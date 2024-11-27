package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import static com.Desktop.angryBird.States.BaseLevel.PPM;

public class Obst4 extends Obstacles {
    private Texture texture;

    public Obst4(World world, float x, float y, float width, float height) {
        super(world, x, y, width, height);
        texture = new Texture("obj4.png");
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(
            texture,
            body.getPosition().x * PPM - texture.getWidth() / 2,
            body.getPosition().y * PPM - texture.getHeight() / 2
        );
    }

    public void dispose() {
        texture.dispose();
    }
}
