package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2; // Import Vector2

public abstract class Bird {
    protected Texture texture;
    public float x, y;
    protected float width = 70;
    protected float height = 70;

    public Bird(float x, float y, String texturePath) {
        this.x = x;
        this.y = y;
        this.texture = new Texture(texturePath);
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getter for the texture
    public Texture getTexture() {
        return texture;
    }

    // Getter for position as Vector2
    public Vector2 getPosition() {
        return new Vector2(x, y); // Return the bird's position as a Vector2
    }

    // Dispose method to prevent memory leaks
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
