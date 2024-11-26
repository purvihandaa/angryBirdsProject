package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Pigs {
    protected Texture texture;
    protected float x, y;
    protected float width;
    protected float height;
    public boolean damaged = false;
    private float damageTimer = 0;

    public Pigs(float x, float y, String texturePath, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = new Texture(texturePath);
    }

    public void update(float dt) {
        if (damaged) {
            damageTimer += dt; // Increment the timer
        }
    }


    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setTexture(Texture newTexture) {
        if (this.texture != null) {
            this.texture.dispose(); // Dispose of the old texture to avoid memory leaks
        }
        this.texture = newTexture;
    }

    public boolean isReadyToRemove() {
        return damaged && damageTimer >= 1.0f; // Ready to remove after 1 second
    }

    public void damage() {
        if (!damaged) {
            this.texture = new Texture("pig_damaged.png"); // Set to damaged texture
            damaged = true;
        }
    }


    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
