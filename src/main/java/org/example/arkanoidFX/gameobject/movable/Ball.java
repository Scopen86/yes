package org.example.arkanoidFX.gameobject.movable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents the ball that bounces around the game area.
 * Demonstrates OOP principle: Polymorphism (overriding methods)
 */
public class Ball extends MovableObject {
    private int speed;
    private int gameWidth;
    private int gameHeight;
    private boolean active;

    public Ball(int x, int y, int width, int height, int gameWidth, int gameHeight) {
        super(x, y, width, height);
        this.speed = 10;  // Set to a reasonable speed (3 pixels per frame)
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.dx = speed;  // Use speed variable
        this.dy = -speed; // Use speed variable (moving up)
        this.active = true;

        // Create the Circle shape for JavaFX rendering
        Circle circle = new Circle(width / 2.0);
        circle.setFill(Color.WHITE);
        circle.setLayoutX(x + width / 2.0);
        circle.setLayoutY(y + height / 2.0);
        this.shape = circle;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void increaseSpeed() {
        speed = 2;
        updateVelocity();
    }

    public void normalSpeed() {
        speed = 1;
        updateVelocity();
    }

    private void updateVelocity() {
        int dirX = dx > 0 ? 1 : -1;
        int dirY = dy > 0 ? 1 : -1;
        dx = dirX * speed;
        dy = dirY * speed;
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
        if (!active) return;

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

        // Update shape position
        updateShapePosition();
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
        this.dx = speed;  // Use current speed
        this.dy = -speed; // Use current speed (moving up)
        this.active = true;
        updateShapePosition();
    }
}
