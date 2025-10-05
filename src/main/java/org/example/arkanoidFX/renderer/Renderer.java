package org.example.arkanoidFX.renderer;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.arkanoidFX.gameobject.GameObject;


public class Renderer {
    private Group root;
    private Scene scene;
    private Stage primaryStage;

    public Renderer(int width, int height, Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new Group();
        scene = new Scene(root, width, height, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Arkanoid!");
    }

    public void draw(GameObject obj) {
        root.getChildren().add(obj.getShape());
        primaryStage.show();
    }

    public void clear() {
        root.getChildren().clear();
    }

    public void drawText(String text, int x, int y, Color color) {
        Text txt = new javafx.scene.text.Text(x, y, text);
        txt.setFill(color);
        root.getChildren().add(txt);
    }

    public Scene getScene() {
        return scene;
    }

}
