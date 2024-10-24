package com.Desktop.angryBird.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Obstacles {

    protected Texture texture;
    protected float x, y;
    protected float width;
    protected float height;

    public Obstacles(float x, float y, String texturePath, float w, float h) {
        this.x = x;
        this.y = y;
        this.width=w;
        this.height=h;
        this.texture = new Texture(texturePath);
    }

    public abstract void update(float dt);

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
