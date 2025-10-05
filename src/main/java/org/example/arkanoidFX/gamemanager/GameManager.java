package org.example.arkanoidFX.gamemanager;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.example.arkanoidFX.gameobject.brick.NormalBrick;
import org.example.arkanoidFX.gameobject.brick.StrongBrick;
import org.example.arkanoidFX.gameobject.brick.Brick;
import org.example.arkanoidFX.gameobject.movable.Ball;
import org.example.arkanoidFX.gameobject.movable.Paddle;
import org.example.arkanoidFX.gameobject.powerup.PowerUp;
import org.example.arkanoidFX.gameobject.powerup.ExpandPaddlePowerUp;
import org.example.arkanoidFX.gameobject.powerup.FastBallPowerUp;
import org.example.arkanoidFX.renderer.Renderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Main game controller that manages game state, logic, and flow.
 * Demonstrates OOP principle: Encapsulation (manages all game objects internally)
 */
public class GameManager {
    // Game constants
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 800;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_SIZE = 30;
    private static final int BRICK_WIDTH = 50;
    private static final int BRICK_HEIGHT = 30;

    // Game objects
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private List<ActivePowerUp> activePowerUps;

    // Game state
    private int score;
    private int lives;
    private int level;
    private String gameState;
    private Renderer renderer;
    private Stage primaryStage;
    private Random random;
    private AnimationTimer gameLoop;

    // Inner class to track active power-ups with remaining duration
    private static class ActivePowerUp {
        PowerUp powerUp;
        int remainingTicks;

        ActivePowerUp(PowerUp powerUp, int duration) {
            this.powerUp = powerUp;
            this.remainingTicks = duration;
        }
    }

    public GameManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.renderer = new Renderer(GAME_WIDTH, GAME_HEIGHT, primaryStage);
        this.random = new Random();
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.activePowerUps = new ArrayList<>();
        this.gameState = "MENU";
    }

    public void startGame() {
        this.score = 0;
        this.lives = 3;
        this.level = 1;
        this.gameState = "PLAYING";
        initializeLevel();
        handleInput();
        startGameLoop();
        renderGame();
    }

    private void handleInput() {
        renderer.getScene().setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (gameState.equals("PLAYING")) {
                switch (code) {
                    case A:
                    case LEFT:
                        paddle.moveLeft();
                        break;
                    case D:
                    case RIGHT:
                        paddle.moveRight();
                        break;
                    case P:
                        gameState = "PAUSED";
                        break;
                }
            } else if (gameState.equals("PAUSED")) {
                if (code == KeyCode.P) {
                    gameState = "PLAYING";
                }
            }
        });

        renderer.getScene().setOnKeyReleased(event -> {
            KeyCode code = event.getCode();

            if (code == KeyCode.A || code == KeyCode.LEFT ||
                code == KeyCode.D || code == KeyCode.RIGHT) {
                paddle.stop();
            }
        });
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Run at approximately 60 FPS
                if (now - lastUpdate >= 16_666_666) {
                    updateGame();
                    renderer.clear();
                    renderGame();
                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();
    }

    private void initializeLevel() {
        // Create paddle
        int paddleX = (GAME_WIDTH - PADDLE_WIDTH) / 2;
        int paddleY = GAME_HEIGHT - PADDLE_HEIGHT - 30;
        paddle = new Paddle(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT, GAME_WIDTH);

        // Create ball
        int ballX = GAME_WIDTH / 2;
        int ballY = GAME_HEIGHT - PADDLE_HEIGHT - BALL_SIZE - 40;
        ball = new Ball(ballX, ballY, BALL_SIZE, BALL_SIZE, GAME_WIDTH, GAME_HEIGHT);

        // Create bricks
        bricks.clear();
        powerUps.clear();
        activePowerUps.clear();

        createBricks();
    }

    private void createBricks() {
        int rows = 5 + level;
        int cols = 8;
        int startY = 2;
        int spacing = 1;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * (BRICK_WIDTH + spacing) + 2;
                int y = row * (BRICK_HEIGHT + spacing) + startY;

                if (random.nextInt(100) < 20) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                } else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    public void updateGame() {
        if (!gameState.equals("PLAYING")) {
            return;
        }

        paddle.update();
        ball.update();

        for (PowerUp powerUp : powerUps) {
            powerUp.update();
        }

        updateActivePowerUps();
        checkCollisions();

        if (ball.getY() >= GAME_HEIGHT) {
            loseLife();
        }

        if (bricks.isEmpty()) {
            levelComplete();
        }
    }

    private void updateActivePowerUps() {
        Iterator<ActivePowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            ActivePowerUp active = iterator.next();
            active.remainingTicks--;

            if (active.remainingTicks <= 0) {
                active.powerUp.removeEffect(paddle);
                iterator.remove();
            }
        }
    }

    public void checkCollisions() {
        if (ball.collidesWith(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (ball.collidesWith(brick)) {
                brick.takeHit();
                ball.bounceOffBrick();

                if (brick.isDestroyed()) {
                    score += brick.getScoreValue();

                    if (random.nextInt(100) < 15) {
                        spawnPowerUp(brick.getX(), brick.getY());
                    }

                    brickIterator.remove();
                }
                break;
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();

            if (powerUp.getY() >= GAME_HEIGHT) {
                powerUpIterator.remove();
                continue;
            }

            if (paddle.collidesWith(powerUp)) {
                activatePowerUp(powerUp);
                powerUpIterator.remove();
            }
        }
    }

    private void spawnPowerUp(int x, int y) {
        PowerUp powerUp;

        if (random.nextBoolean()) {
            powerUp = new ExpandPaddlePowerUp(x, y, 2, 1);
        } else {
            powerUp = new FastBallPowerUp(x, y, 2, 1);
        }

        powerUps.add(powerUp);
    }

    private void activatePowerUp(PowerUp powerUp) {
        powerUp.applyEffect(paddle);
        activePowerUps.add(new ActivePowerUp(powerUp, powerUp.getDuration()));
    }

    private void loseLife() {
        lives--;

        if (lives <= 0) {
            gameOver();
        } else {
            int paddleX = (GAME_WIDTH - paddle.getWidth()) / 2;
            int paddleY = GAME_HEIGHT - PADDLE_HEIGHT - 30;
            paddle.setX(paddleX);
            paddle.stop();

            int ballX = GAME_WIDTH / 2;
            int ballY = GAME_HEIGHT - PADDLE_HEIGHT - BALL_SIZE - 40;
            ball.reset(ballX, ballY);
        }
    }

    private void levelComplete() {
        level++;
        initializeLevel();
    }

    public void gameOver() {
        gameState = "GAME_OVER";
    }

    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private void renderGame() {
        for (Brick brick : bricks) {
            renderer.draw(brick);
        }

        renderer.draw(paddle);
        renderer.draw(ball);
    }
}
