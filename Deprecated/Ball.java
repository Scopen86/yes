package org.example.arkanoidFX;

/**
 * Represents the ball that bounces around the game area.
 * Demonstrates OOP principle: Polymorphism (overriding methods)
 */
public class Ball extends MovableObject {
    private int speed;
    private final int gameWidth;
    private final int gameHeight;

    public Ball(int x, int y, int width, int height, int gameWidth, int gameHeight) {
        super(x, y, width, height);
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.speed = 1;
        this.dx = 1;  // Initial direction
        this.dy = -1; // Moving up
    }


    public void bounceOffPaddle(Paddle paddle) {
        // Bounce vertically
        dy = -Math.abs(dy);
        
        // Add horizontal component based on where ball hits paddle
        int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        int ballCenter = x + width / 2;
        int offset = ballCenter - paddleCenter;
        
        // Adjust horizontal direction based on offset
        if (offset < -paddle.getWidth() / 4) {
            dx = -speed;
        } else if (offset > paddle.getWidth() / 4) {
            dx = speed;
        }
    }
    
    public void bounceOffBrick() {
        dy = -dy;
    }
    
    public void bounceOffWall() {
        dx = -dx;
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
        
        // Bounce off left and right walls
        if (x <= 0 || x + width >= gameWidth) {
            dx = -dx;
            x = x <= 0 ? 0 : gameWidth - width;
        }
        
        // Bounce off top wall
        if (y <= 0) {
            dy = -dy;
            y = 0;
        }
    }
    
    @Override
    public void update() {
        move();
    }
    
    @Override
    public void render() {
        // Render method for terminal
    }
    
    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 1;
        this.dy = -1;
    }

    public boolean isOutOfBottom() {
        return y > gameHeight;
    }
}
