package org.example.arkanoidFX.gameobject.movable;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.arkanoidFX.gameobject.powerup.PowerUp;


public class Paddle extends MovableObject {
    private int speed;
    private PowerUp currentPowerUp;
    private int originalWidth;
    private int gameWidth;

    public Paddle(int x, int y, int width, int height, int gameWidth) {
        super(x, y, width, height);
        this.speed = 10;
        this.originalWidth = width;
        this.gameWidth = gameWidth;

        // Create the Rectangle shape for JavaFX rendering
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.BLUE);
        rectangle.setLayoutX(x);
        rectangle.setLayoutY(y);
        this.shape = rectangle;
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

    public void applyPowerUp(PowerUp powerUp) {
        this.currentPowerUp = powerUp;
        powerUp.applyEffect(this);
    }

    public void expandPaddle() {
        this.width = originalWidth * 2;
        if (shape instanceof Rectangle) {
            ((Rectangle) shape).setWidth(width);
        }
    }

    public void restorePaddleSize() {
        this.width = originalWidth;
        if (shape instanceof Rectangle) {
            ((Rectangle) shape).setWidth(width);
        }
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    @Override
    public void move() {
        x += dx;
        // Keep paddle within game boundaries
        if (x < 0) x = 0;
        if (x + width > gameWidth) x = gameWidth - width;

        // Update shape position
        updateShapePosition();
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render() {
        // Render method for terminal (will be used by Renderer)
    }
}
