module org.example.arkanoidfxp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens org.example.arkanoidfxp to javafx.fxml;
    exports org.example.arkanoidfxp;
}