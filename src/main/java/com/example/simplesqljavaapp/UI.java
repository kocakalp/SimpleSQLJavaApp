package com.example.simplesqljavaapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class UI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox maninBox = new VBox();
        HBox buttonHbox = new HBox();
        VBox tableViewVBox = new VBox();
        VBox textfieldVBox = new VBox();
        VBox updateVBox = new VBox();
        Sql a= new Sql();

        Button createButton = new Button("Create");
        Button deleteButton = new Button("Delete");
        TextField searchLabel = new TextField();
        searchLabel.setPromptText("Search");
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        buttonHbox.getChildren().addAll(createButton, deleteButton, updateButton, searchLabel, searchButton);


        Font font_30 = new Font(30);

        Label databaseLabel = new Label("Database");
        databaseLabel.setFont(font_30);
        ComboBox<String> databaseComboBox = new ComboBox<>();
        HBox labelHbox_1 = new HBox();
        labelHbox_1.getChildren().addAll(databaseLabel,databaseComboBox);

        Label tableLabel = new Label("Table");
        tableLabel.setFont(font_30);
        ComboBox<String> tableComboBox = new ComboBox<>();
        HBox labelHbox_2 = new HBox();
        labelHbox_2.getChildren().addAll(tableLabel,tableComboBox);




        databaseComboBox.getItems().addAll(a.getDatabaseNames());
        databaseComboBox.setOnAction(e -> {

            tableComboBox.getItems().clear();
            tableComboBox.getItems().addAll(a.getTableNames(databaseComboBox.getValue()));
            tableComboBox.setOnAction(b -> {
                textfieldVBox.getChildren().clear();
                updateVBox.getChildren().clear();
                tableViewVBox.getChildren().clear();
                TableView<Map<String, Object>> tableView = new TableView<>();
                tableView.getItems().clear();
                tableView = a.getData(databaseComboBox.getValue(), tableComboBox.getValue());
                tableView.refresh();
                tableViewVBox.getChildren().add(tableView);
//               System.out.println(a.getSelectedRow()); //Selected rowu basıyor
                });
        });
//                tableComboBox.getValue diyeceksin
//                burdan sana bir string değeri dönecek
//                bu değeri getData ya koyacaksın ve
//                burdan devam edeceksin


        deleteButton.setOnAction(e -> {
//            System.out.println(a.getSelectedRow() + " BUTTON"); //Selected rowu basıyor
            if (a.getSelectedRow() != null && !a.getSelectedRow().isEmpty()) {
                a.deleteData(databaseComboBox.getValue(), tableComboBox.getValue());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Unable to delete");
                alert.setHeaderText(null);
                alert.setContentText("Please select a row.\n");
                alert.showAndWait();
            }
        });



        Scene updateScene = new Scene(updateVBox, 320, 240);
        Stage updateStage = new Stage();
        updateStage.alwaysOnTopProperty();
        updateButton.setOnAction(e -> {
            if (a.getSelectedRow() != null && !a.getSelectedRow().isEmpty()) {// Eğer seçili bir değer varsa
                    updateStage.setTitle("SQL Update Screen");
                    updateStage.getIcons().add(new Image("/dinodino.png"));

                    if(tableComboBox.getValue()!=null && !tableComboBox.getValue().isEmpty()) {
                        updateVBox.getChildren().clear();
                        TextField field = new TextField(a.getSelectedRow().toString());
                        updateVBox.getChildren().add(field);
                        Label primarkeyLabel = new Label(a.getPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue()));
                        primarkeyLabel.setFont(font_30);
                        updateVBox.getChildren().add(primarkeyLabel);
                    } else {
                        updateVBox.getChildren().clear();
                    }

                    updateStage.setScene(updateScene);
                    updateStage.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Unable to update");
                alert.setHeaderText(null);
                alert.setContentText("Please select a row.\n");
                alert.showAndWait();
            }
        });

        Scene createScene = new Scene(textfieldVBox, 320, 240);
        Stage createStage = new Stage();
        createStage.alwaysOnTopProperty();
        createButton.setOnAction(e -> {
            if (tableComboBox.getValue()!=null) {// tableComboBox'un içinde değer varsa
                createStage.setTitle("SQL Create Screen");
                createStage.getIcons().add(new Image("/sailing.gif"));

                if(tableComboBox.getValue()!=null && !tableComboBox.getValue().isEmpty()) {
                    textfieldVBox.getChildren().clear();
                    ArrayList<String> columnNamesArraylist = a.getColumnNames(databaseComboBox.getValue(), tableComboBox.getValue());
                    for (String i: columnNamesArraylist) {
                        TextField data_4 = new TextField();
                        data_4.setPromptText(i);
                        //data_4.cursorProperty().unbind();
                        textfieldVBox.getChildren().add(data_4);
                    }
                    Label primarkeyLabel = new Label(a.getPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue()));
                    primarkeyLabel.setFont(font_30);
                    textfieldVBox.getChildren().add(primarkeyLabel);
                }
                createStage.setScene(createScene);
                createStage.show();

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Unable to create");
                alert1.setHeaderText(null);
                alert1.setContentText("Please select a table.\n");
                alert1.showAndWait();
            }
        });

        searchButton.setOnAction(e ->{

            System.out.println(searchLabel.getCharacters());

        });









        maninBox.getChildren().addAll(labelHbox_1, labelHbox_2, buttonHbox, tableViewVBox);
        Scene scene = new Scene(maninBox, 320, 240);
        stage.setTitle("SimpleSQLJavaApp");
        stage.getIcons().add(new Image("/view.gif"));
        stage.setMaximized(true);
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
