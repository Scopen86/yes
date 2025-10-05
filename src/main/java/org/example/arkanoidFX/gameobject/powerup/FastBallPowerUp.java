package org.example.arkanoidFX.gameobject.powerup;

import org.example.arkanoidFX.gameobject.movable.Ball;
import org.example.arkanoidFX.gameobject.movable.MovableObject;

/**
 * Power-up that increases ball speed.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */
public class FastBallPowerUp extends PowerUp {
    private Ball ball;

    public FastBallPowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "FastBall";
        this.duration = 100; // Duration in game ticks
    }

    @Override
    public void applyEffect(MovableObject movable) {
        if (ball != null) {
            ((Ball) ball).increaseSpeed();
        }
    }

    @Override
    public void removeEffect(MovableObject movable) {
        if (ball != null) {
            ((Ball) ball).normalSpeed();
        }
    }

    @Override
    public void update() {
        fall();
    }

    @Override
    public void render() {
        // Render method for terminal
    }
}
