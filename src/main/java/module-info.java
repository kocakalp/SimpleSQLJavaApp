module com.example.simplesqljavaapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens com.example.simplesqljavaapp to javafx.fxml;
    exports com.example.simplesqljavaapp;
}