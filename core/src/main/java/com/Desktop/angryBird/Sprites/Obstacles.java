package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Obstacles {

    protected Texture texture;
    protected float x, y;
    protected float width;
    protected float height;
    public int power = 5; // Add power attribute (example: 5)

    public Obstacles(float x, float y, String texturePath, float w, float h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.texture = new Texture(texturePath);
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Check if bird collides with the obstacle
    public boolean checkCollision(Bird bird) {
        return bird.getBounds().overlaps(this.getBounds());
    }

    // Handle the collision between bird and obstacle
    public void handleCollision(Bird bird) {
        if (checkCollision(bird)) {
            bird.reducePower(this.power); // Reduce bird's power by obstacle's power
            this.dispose(); // Destroy obstacle
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public void setTexture(Texture texture) {
        this.texture = texture; // Set the texture to null or an invisible texture
    }
}
