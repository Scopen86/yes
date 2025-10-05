package org.example.arkanoidFX.gameobject.brick;


/**
 * Strong brick that requires multiple hits to destroy.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class StrongBrick extends Brick {
    public StrongBrick(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = 3;
        this.type = "Strong";
        this.scoreValue = 30;
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
        // Strong bricks don't need updating
    }

    @Override
    public void render() {
        // Render method for terminal
    }
}
