package org.example.arkanoidFX;

/**
 * Base class for objects that can move.
 * Demonstrates OOP principle: Inheritance
 */
public abstract class MovableObject extends GameObject {
    protected int dx, dy; // Movement speed in x and y direction
    
    public MovableObject(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }
    
    public int getDx() { return dx; }
    public int getDy() { return dy; }
    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
    
    public abstract void move();
}
