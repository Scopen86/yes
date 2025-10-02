package org.example.demofx;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class RendererApplication extends Application {
    private GameManager gameManager;
    private Circle ballShape;
    private Rectangle paddleShape;
    private List<Rectangle> brickShapes;
    private List<Brick> visualBrickMapping; // Track which brick each shape corresponds to
    private Text scoreText;

    @Override
    public void start(Stage primaryStage) {
        gameManager = new GameManager();

        // Create JavaFX shapes for rendering
        Ball ball = gameManager.getBall();
        ballShape = new Circle(ball.getX() + ball.getWidth()/2.0, ball.getY() + ball.getHeight()/2.0,
                              ball.getWidth()/2.0, Color.RED);

        Paddle paddle = gameManager.getPaddle();
        paddleShape = new Rectangle(paddle.getX(), paddle.getY(),
                                   paddle.getWidth(), paddle.getHeight());
        paddleShape.setFill(Color.BLUE);

        // Create brick shapes with proper mapping
        brickShapes = new ArrayList<>();
        visualBrickMapping = new ArrayList<>();
        for (Brick brick : gameManager.getBricks()) {
            Rectangle brickRect = new Rectangle(brick.getX(), brick.getY(),
                                              brick.getWidth(), brick.getHeight());
            brickRect.setFill(Color.ORANGE);
            brickShapes.add(brickRect);
            visualBrickMapping.add(brick); // Map each visual brick to its game brick
        }

        // Create score display
        scoreText = new Text(10, 30, "Score: 0");
        scoreText.setFont(new Font(20));
        scoreText.setFill(Color.WHITE);

        Scene scene = createScene();
        primaryStage.setTitle("Arkanoid!");
        primaryStage.setScene(scene);
        primaryStage.show();

        startGameLoop();
    }

    private Scene createScene() {
        Group group = new Group();
        group.getChildren().add(ballShape);
        group.getChildren().add(paddleShape);
        group.getChildren().addAll(brickShapes);
        group.getChildren().add(scoreText);

        Scene scene = new Scene(group, 800, 600, Color.BLACK);

        // Input handling
        final boolean[] leftPressed = {false};
        final boolean[] rightPressed = {false};

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT, A -> leftPressed[0] = true;
                case RIGHT, D -> rightPressed[0] = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT, A -> leftPressed[0] = false;
                case RIGHT, D -> rightPressed[0] = false;
            }
        });

        // Update paddle movement based on input
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Paddle paddle = gameManager.getPaddle();
                if (leftPressed[0] && !rightPressed[0]) {
                    paddle.moveLeft();
                } else if (rightPressed[0] && !leftPressed[0]) {
                    paddle.moveRight();
                } else {
                    paddle.stop();
                }
            }
        }.start();

        scene.setOnMouseClicked(event -> scene.getRoot().requestFocus());
        scene.getRoot().requestFocus();

        return scene;
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update game logic
                gameManager.update();

                // Update visual elements
                updateVisuals();
            }
        }.start();
    }

    private void updateVisuals() {
        // Update ball position
        Ball ball = gameManager.getBall();
        ballShape.setCenterX(ball.getX() + ball.getWidth()/2.0);
        ballShape.setCenterY(ball.getY() + ball.getHeight()/2.0);

        // Update paddle position
        Paddle paddle = gameManager.getPaddle();
        paddleShape.setX(paddle.getX());
        paddleShape.setY(paddle.getY());

        // Update brick visibility (remove destroyed bricks correctly)
        List<Brick> currentBricks = gameManager.getBricks();
        for (int i = visualBrickMapping.size() - 1; i >= 0; i--) {
            Brick visualBrick = visualBrickMapping.get(i);
            // Check if this specific brick still exists in the game
            if (!currentBricks.contains(visualBrick)) {
                // Remove the corresponding visual brick
                Rectangle brickShape = brickShapes.get(i);
                ((Group) ballShape.getParent()).getChildren().remove(brickShape);
                brickShapes.remove(i);
                visualBrickMapping.remove(i);
            }
        }

        // Update score
        scoreText.setText("Score: " + gameManager.getScore());
    }

    public static void main(String[] args){
        launch(args);
    }
}