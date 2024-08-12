package com.example.simplesqljavaapp;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class UI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox maninBox = new VBox();
        HBox buttonHbox = new HBox();
        VBox tableViewVBox = new VBox();
        VBox tableViewVBoxSearch = new VBox();
        VBox tableViewVBoxDelete = new VBox();
        VBox TextAreaVBoxCreate = new VBox();
        VBox updateVBox = new VBox();
        Sql a= new Sql();

        GridPane insertGripPane = new GridPane(10,10);

        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        TextField searchLabel = new TextField();
        searchLabel.setPromptText("Search");
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button createButton = new Button("Create");
        Button crudButton = new Button("CRUD");
        crudButton.setPrefWidth(50);
        crudButton.setPrefHeight(50);

        buttonHbox.getChildren().addAll(insertButton, deleteButton, updateButton, createButton, searchLabel, searchButton);


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


        VBox newVBox = new VBox();
        newVBox.getChildren().addAll(labelHbox_1, labelHbox_2);
        HBox newHBox = new HBox();
        newHBox.getChildren().addAll(newVBox, crudButton );





        databaseComboBox.getItems().addAll(a.getDatabaseNames());
        databaseComboBox.setOnAction(e -> {

            tableComboBox.getItems().clear();
            tableComboBox.getItems().addAll(a.getTableNames(databaseComboBox.getValue()));
            tableComboBox.setOnAction(b -> {
                insertGripPane.getChildren().clear();
                tableViewVBoxSearch.getChildren().clear();
                updateVBox.getChildren().clear();
                tableViewVBoxDelete.getChildren().clear();
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
                tableViewVBoxDelete.getChildren().clear();
                tableViewVBoxSearch.getChildren().clear();
                tableViewVBox.getChildren().clear();
                TableView<Map<String, Object>> tableViewDeleted = new TableView<>();
                tableViewDeleted.getItems().clear();
                tableViewDeleted = a.getData(databaseComboBox.getValue(), tableComboBox.getValue());
                tableViewDeleted.refresh();
                tableViewVBoxDelete.getChildren().add(tableViewDeleted);

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
                        String holder = a.getSelectedRow().toString();
                       // String holder1 = a.getSelectedRowWithoutPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue());

                        String result = holder.replace(",?\\s*"+a.getSelectedRowWithoutPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue()),"").replaceAll("\\s{2,}", " ").trim();

                        TextField field = new TextField(result);
                        updateVBox.getChildren().add(field);
                        Label primarkeyLabel = new Label("Primary Key Column: " + a.getPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue()));
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

        Scene insertScene = new Scene(insertGripPane, 320, 240);
        Stage insertStage = new Stage();
        insertStage.alwaysOnTopProperty();

        int numColumns = 3;
        insertButton.setOnAction(e -> {
            if (tableComboBox.getValue()!=null) {// tableComboBox'un içinde değer varsa
                insertStage.setTitle("SQL Insert Screen");
                insertStage.getIcons().add(new Image("/sailing.gif"));

                if(tableComboBox.getValue()!=null && !tableComboBox.getValue().isEmpty()) {
                    insertGripPane.getChildren().clear();
                    ArrayList<String> columnNamesArraylist = a.getColumnNames(databaseComboBox.getValue(), tableComboBox.getValue());

                    int row = 0;
                    for (int i = 0; i < columnNamesArraylist.size(); i++) {
                        TextField data_4 = new TextField();
                        data_4.setPromptText(columnNamesArraylist.get(i));
                        int column = i % numColumns;
                        if (column == 0 && i > 0) {
                            row++;
                        }
                        insertGripPane.add(data_4, column, row);
                    }
                    Label primarkeyLabel = new Label("Primary Key Column: " + a.getPrimaryKey(databaseComboBox.getValue(), tableComboBox.getValue()));
                    primarkeyLabel.setFont(font_30);
                    Button insertButton2 = new Button("Insert");
                    insertGripPane.add(primarkeyLabel, 0,row +1, numColumns,1);

                    insertButton2.setOnAction(b -> {

                    });




                    insertGripPane.add(insertButton2,0,row+2, numColumns,1);
                }
                insertStage.setScene(insertScene);
                insertStage.show();

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Unable to insert");
                alert1.setHeaderText(null);
                alert1.setContentText("Please select a table.\n");
                alert1.showAndWait();
            }
        });



        searchButton.setOnAction(e -> {
            if(tableComboBox.getValue()!=null && !tableComboBox.getValue().isEmpty()) {
                System.out.println(searchLabel.getCharacters());
                tableViewVBox.getChildren().clear();
                tableViewVBoxDelete.getChildren().clear();
                TableView<Map<String, Object>> tableViewSearch = new TableView<>();
                tableViewSearch.getItems().clear();
                tableViewVBoxSearch.getChildren().clear();
                tableViewSearch = a.getSearchedData(databaseComboBox.getValue(), tableComboBox.getValue(), searchLabel.getCharacters().toString());
                tableViewSearch.refresh();
                tableViewVBoxSearch.getChildren().add(tableViewSearch);
            } else {
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Unable to search");
                alert2.setHeaderText(null);
                alert2.setContentText("Please select a table.\n");
                alert2.showAndWait();
            }
        });

        Scene createScene = new Scene(TextAreaVBoxCreate, 320, 240);
        Stage createStage = new Stage();
        createButton.setOnAction(e -> {
            //System.out.println();
            if(databaseComboBox.getValue()!=null && !databaseComboBox.getValue().isEmpty()) {
                createStage.setTitle("SQL Create Screen");
                createStage.getIcons().add(new Image("/climbing.gif"));
                TextAreaVBoxCreate.getChildren().clear();
                /////////////////////////////////////
                Random r = new Random();
                int randomInt = r.nextInt(1, 15);
                String fileContent = "";
                FileReader fr = null;

                try {
                    fr = new FileReader("C:\\Users\\Stajyer\\Desktop\\SimpleSQLJavaApp\\src\\main\\resources\\TableExamples.txt");//Path is not dynamic
                    int ch;
                    StringBuilder contentBuilder = new StringBuilder();
                    while ((ch = fr.read()) != -1) {
                        contentBuilder.append((char) ch);
                    }
                    fileContent = contentBuilder.toString();
                } catch (FileNotFoundException fe) {
                    System.out.println("File not found");
                } catch (IOException x) {
                    System.out.println("Error reading the file");
                } finally {
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (IOException x) {
                            System.out.println("Error closing the file");
                        }
                    }
                }
                String[] tables = fileContent.split("(?=-\\d+)");
                String selectedTable = null;
                if (randomInt < tables.length) {
                    selectedTable = tables[randomInt].trim();
                    selectedTable = selectedTable.replaceFirst("^-\\d+", "").trim();
                }
                /////////////////////////////////////
                TextArea createTextArea = new TextArea(selectedTable);
                Button createButton2 = new Button("Create");
                TextAreaVBoxCreate.getChildren().addAll(createTextArea, createButton2);
                createButton2.setOnAction(b -> {

                  boolean created = a.createTable(databaseComboBox.getValue(), createTextArea.getText());
                  if(created) {
                      createStage.close();
                      Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
                      alert3.setTitle("Created successfully");
                      alert3.setHeaderText(null);
                      alert3.setContentText("Created successfully.\n");
                      alert3.showAndWait();
                      //databaseComboBox.getValue().refresh();
                  } else {
                      Alert alert4 = new Alert(Alert.AlertType.WARNING);
                      alert4.setTitle("Unable to create");
                      alert4.setHeaderText(null);
                      alert4.setContentText("An Error Accured.\n");
                      alert4.showAndWait();
                  }
                });

                createStage.setScene(createScene);
                createStage.show();
            } else {
                Alert alert5 = new Alert(Alert.AlertType.WARNING);
                alert5.setTitle("Unable to create");
                alert5.setHeaderText(null);
                alert5.setContentText("Please select a database.\n");
                alert5.showAndWait();
            }
         });








        VBox vbox = new VBox();
        crudButton.setOnAction(e -> {

            newHBox.getChildren().clear();
            buttonHbox.getChildren().clear();
            tableViewVBox.getChildren().clear();
            tableViewVBoxSearch.getChildren().clear();
            tableViewVBoxDelete.getChildren().clear();
            stage.setMaximized(true);
            Image image = new Image(getClass().getResourceAsStream("/jumpscare.gif"),1616,868,false,false);
            PauseTransition delay = new PauseTransition(Duration.millis(850));
            delay.setOnFinished( event -> stage.close() );
            delay.play();

            ImageView imageView = new ImageView(image);
            vbox.getChildren().add(imageView);

           // stage.close();
        });
        maninBox.getChildren().addAll(newHBox, buttonHbox, tableViewVBox, tableViewVBoxSearch, tableViewVBoxDelete, vbox);
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
