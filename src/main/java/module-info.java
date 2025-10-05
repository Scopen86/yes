module org.example.arkanoidFX {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.arkanoidFX to javafx.fxml;
    exports org.example.arkanoidFX;
    exports org.example.arkanoidFX.renderer;
    opens org.example.arkanoidFX.renderer to javafx.fxml;
}