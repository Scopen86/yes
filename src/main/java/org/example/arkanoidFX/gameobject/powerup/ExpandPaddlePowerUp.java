package org.example.arkanoidFX.gameobject.powerup;


import org.example.arkanoidFX.gameobject.movable.MovableObject;
import org.example.arkanoidFX.gameobject.movable.Paddle;

/**
 * Power-up that expands the paddle.
 * Demonstrates OOP principle: Inheritance and Polymorphism
 */


public class ExpandPaddlePowerUp extends PowerUp {
    public ExpandPaddlePowerUp(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.type = "ExpandPaddle";
        this.duration = 100; // Duration in game ticks
    }

    @Override
    public void applyEffect(MovableObject paddle) {
        ((Paddle) paddle).expandPaddle();
    }

    @Override
    public void removeEffect(MovableObject paddle) {
        ((Paddle) paddle).restorePaddleSize();
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
