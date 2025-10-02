package org.example.demofx;

/**
 * Normal brick that breaks after one hit.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class NormalBrick extends Brick {
    public NormalBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = 1;
        this.type = "Normal";
        this.scoreValue = 10;
    }
    
    @Override
    public void takeHit() {
        hitPoints--;
    }
    
    @Override
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
    
    @Override
    public void update() {
        // Normal bricks don't need updating
    }
    
    @Override
    public void render() {
        // Render method for terminal
    }
}
