package org.example.arkanoidFX;

import javafx.scene.shape.Shape;

/**
 * Base class for all game objects.
 * Demonstrates OOP principle: Abstraction
 */
public abstract class GameObject extends Shape {
    protected int x, y, width, height;
    
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    // Getters for collision detection
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    
    // Check if this object collides with another
    public boolean collidesWith(GameObject other) {
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y;
    }
    
    public abstract void update();
    public abstract void render();
}
