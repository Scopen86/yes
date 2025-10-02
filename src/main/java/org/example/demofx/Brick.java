package org.example.demofx;

/**
 * Base class for all brick types.
 * Demonstrates OOP principle: Abstraction
 */
public abstract class Brick extends GameObject {
    protected int hitPoints;
    protected String type;
    protected int scoreValue;

    public Brick(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    public int getHitPoints() {
        return hitPoints;
    }
    
    public String getType() {
        return type;
    }
    
    public int getScoreValue() {
        return scoreValue;
    }

    public abstract void takeHit();
    public abstract boolean isDestroyed();
}
