package com.Desktop.angryBird.States;

import java.io.Serializable;

public class GameObjectState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type; // e.g., "bird", "obstacle", "target"
    private float x, y;  // Position
    private float velocityX, velocityY;

    // Constructor
    public GameObjectState(String type, float x, float y, float velocityX, float velocityY) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }
// Getters and setters...
}
