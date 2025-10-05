package org.example.arkanoidFX;

/**
 * Represents the player-controlled paddle.
 * Demonstrates OOP principle: Encapsulation (private fields with public methods)
 */
public class Paddle extends MovableObject {
    private final int speed;
    private final int gameWidth;

    public Paddle(int x, int y, int width, int height, int gameWidth) {
        super(x, y, width, height);
        this.speed = 2;
        this.gameWidth = gameWidth;
    }

    public void moveLeft() {
        dx = -speed;
    }
    
    public void moveRight() {
        dx = speed;
    }
    
    public void stop() {
        dx = 0;
    }

    @Override
    public void move() {
        x += dx;
        // Keep paddle within game boundaries
        if (x < 0) x = 0;
        if (x + width > gameWidth) x = gameWidth - width;
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render() {
        // Render method (will be used by RendererApplication)
    }
}
