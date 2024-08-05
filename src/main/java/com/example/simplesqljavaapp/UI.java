package com.example.simplesqljavaapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;


public class UI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox maninBox = new VBox();
        HBox buttonHbox = new HBox();
        VBox tableViewVBox = new VBox();
        Sql a= new Sql();

        TextField data_1 = new TextField();
        TextField data_2 = new TextField();
        TextField data_3 = new TextField();

        Button createButton = new Button("Create");
        Button deleteButton = new Button("Delete");
        Button selectButton = new Button("Select");
        Button updateButton = new Button("Update");
        buttonHbox.getChildren().addAll(createButton, deleteButton, selectButton, updateButton);

        ComboBox<String> databaseComboBox = new ComboBox<>();
        ComboBox<String> tableComboBox = new ComboBox<>();



        databaseComboBox.getItems().addAll(a.getDatabaseNames());
        databaseComboBox.setOnAction(e -> {

            tableComboBox.getItems().clear();
            tableComboBox.getItems().addAll(a.getTableNames(databaseComboBox.getValue()));
            tableComboBox.setOnAction(b -> {

                tableViewVBox.getChildren().clear();
                TableView<Map<String, Object>> tableView = new TableView<>();
                tableView.getItems().clear();
                tableView = a.getData(databaseComboBox.getValue(), tableComboBox.getValue());
                tableView.refresh();
                tableViewVBox.getChildren().add(tableView);
                });
        });


//                tableComboBox.getValue diyeceksin
//                burdan sana bir string değeri dönecek
//                bu değeri getData ya koyacaksın ve
//                burdan devam edeceksin




        maninBox.getChildren().addAll(databaseComboBox, tableComboBox, data_1, data_2, data_3, buttonHbox, tableViewVBox);
        Scene scene = new Scene(maninBox, 320, 240);
        stage.setTitle("SQL DATA");
        stage.setScene(scene);
        stage.show();
    }

}



/*public class Main {
    public static void main(String[] args) {


        String connectionString =
                "jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS01;Database=Movies;integratedSecurity=true";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
            return;
        }

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}*/
