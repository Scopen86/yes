package org.example.arkanoidFX;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.arkanoidFX.gamemanager.GameManager;

public class Main extends Application {
    public void start(Stage primaryStage) {
        GameManager gameManager = new GameManager(primaryStage);

        // Handle window close event to properly shut down the game
        primaryStage.setOnCloseRequest(event -> {
            gameManager.stopGame();
            Platform.exit();
            System.exit(0);
        });

        gameManager.startGame();
    }

    public static void main(String[] args){
        launch(args);
    }
}
