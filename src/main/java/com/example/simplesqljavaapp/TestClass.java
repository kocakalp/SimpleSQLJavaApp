package com.example.simplesqljavaapp;









import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
        import java.util.Map;


/*
public class Hello extends Application {
        @Override
        public void start(Stage primaryStage) {
//            public TableView<Map<String, Object>> getData(String database_name, String table_name) {
//
//                TableView<Map<String, Object>> tableView = new TableView<>();
//                ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList();
//
//                try {
//                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//                    System.out.println("JDBC Driver loaded successfully.");
//                } catch (ClassNotFoundException e) {
//                    System.out.println("JDBC Driver not found.");
//                    e.printStackTrace();
//                }
//
//                try (Connection connection = DriverManager.getConnection(URL)) {
//                    if (connection != null) {
//                        System.out.println("Connection established successfully.");
//                        Statement stmt = connection.createStatement();
//
//                        if(table_name != null) {
//                            ResultSet sql = stmt.executeQuery("SELECT * FROM " + database_name + ".[dbo]." + table_name);
//                            System.out.println("SELECT * FROM " + database_name + ".[dbo]." + table_name);
//                            ResultSetMetaData rsMetaData = sql.getMetaData();
//                            int count = rsMetaData.getColumnCount();
//
//                            // Add columns for each field in the table
//                            for(int i = 1; i<=count; i++) {
//                                String columnName = rsMetaData.getColumnName(i);
//                                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
//                                column.setPrefWidth(100);
//                                column.setResizable(false);
//                                column.setReorderable(false);
//                                column.setSortable(false);
//                                column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnName)));
//                                tableView.getColumns().add(column);
//                            }
//
//                            // Add a column for the delete button
//                            TableColumn<Map<String, Object>, Void> deleteColumn = new TableColumn<>("Actions");
//                            deleteColumn.setPrefWidth(100);
//                            deleteColumn.setResizable(false);
//                            deleteColumn.setReorderable(false);
//                            deleteColumn.setSortable(false);
//
//                            Callback<TableColumn<Map<String, Object>, Void>, TableCell<Map<String, Object>, Void>> cellFactory = new Callback<TableColumn<Map<String, Object>, Void>, TableCell<Map<String, Object>, Void>>() {
//                                @Override
//                                public TableCell<Map<String, Object>, Void> call(final TableColumn<Map<String, Object>, Void> param) {
//                                    final TableCell<Map<String, Object>, Void> cell = new TableCell<>() {
//                                        private final Button deleteButton = new Button("Delete");
//
//                                        {
//                                            deleteButton.setOnAction(event -> {
//                                                Map<String, Object> rowData = getTableView().getItems().get(getIndex());
//                                                // Implement the delete functionality here
//                                                System.out.println("Delete button clicked for: " + rowData);
//                                            });
//                                        }
//
//                                        @Override
//                                        protected void updateItem(Void item, boolean empty) {
//                                            super.updateItem(item, empty);
//                                            if (empty) {
//                                                setGraphic(null);
//                                            } else {
//                                                setGraphic(deleteButton);
//                                            }
//                                        }
//                                    };
//                                    return cell;
//                                }
//                            };
//
//                            deleteColumn.setCellFactory(cellFactory);
//                            tableView.getColumns().add(deleteColumn);
//
//                            // Populate the TableView with data
//                            while (sql.next()) {
//                                Map<String, Object> row = new HashMap<>();
//                                for (int i = 1; i <= count; i++) {
//                                    String columnName = rsMetaData.getColumnName(i);
//                                    row.put(columnName, sql.getObject(i));
//                                }
//                                observableList.add(row);
//                            }
//                            tableView.setItems(observableList);
//                        }
//
//                    } else {
//                        System.out.println("Failed to establish connection.");
//                    }
//                } catch (SQLException e) {
//                    System.out.println("Error connecting to the database.");
//                    System.out.println("Error Code: " + e.getErrorCode());
//                    System.out.println("SQL State: " + e.getSQLState());
//                    e.printStackTrace();
//                }
//
//                return tableView;
//            }

        }



        public static void main(String[] args) {
            String input = "Selected row: {f30=KARISIMSIZ, f10=null, f32=null,";

            // Extract substring between '{' and the first ','
            int startIndex = input.indexOf("{") + 1; // After '{'
            int endIndex = input.indexOf(","); // Before the first ','

            if (startIndex > 0 && endIndex > startIndex) {
                String result = input.substring(startIndex, endIndex);
                System.out.println("Extracted value: " + result);
            } else {
                System.out.println("Value not found");
            }
            //launch(args);
        }
}    */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class TestClass {

    public static void main(String[] args) {
//        String str = "manchester united (with nice players)";
//        String result = str.replace("united", "").replaceAll("\\s{2,}", " ").trim();
//        System.out.println(result);
//
//
//        String str2 = "Selected row: {earning_id=1, Domestic=56671993, Worldwide=187733202, Movie_id=36809}";
//        String result2 = str.replace(", Worldwide=187733202", "").replaceAll("\\s{2,}", " ").trim();
//        System.out.println(result2);
//
//        String str4 = "Selected row: {earning_id=1, Domestic=56671993, Worldwide=187733202, Movie_id=36809}";
//        String result4 = str4.replaceAll(",?\\s*Worldwide=187733202", "").replaceAll("\\s{2,}", " ").trim();
//        System.out.println(result4);

        String a = "hELLO tHERE,";
        System.out.println(a.substring(0,a.length()-1));



//
//
//
//
//
//        String str3 = "manchester united (with nice players)";
//        String result3 = str3.replace("(with nice players)", "").trim() + "csaf";
//        System.out.println(result);

        


    }
}






    //    public static void main(String[] args) {
//        String selectedRow1 = "{Address=Obere Str. 57, PostalCode=12209, Country=Germany, CustomerID=asfaıhfıa, CustomerName=Alfreds Futterkiste, City=Berlin, ContactName=Maria Anders}";
//        String selectedRow2 = "{ShipperName=United Package, Phone=(503) 555-3199, ShipperID=2}";
//        String selectedRow3 = "{earning_id=1, Domestic=56671993, Worldwide=187733202, Movie_id=36809}";
//
//
//        String primaryKey1 = "CustomerID";
//        String primaryKey2 = "ShipperID";
//        String primaryKey3 = "earning_id";
//
//
//        System.out.println(extractPrimaryKey(selectedRow1, primaryKey1));
//        System.out.println(extractPrimaryKey(selectedRow2, primaryKey2));
//        System.out.println(extractPrimaryKey(selectedRow3, primaryKey3));
//    }
//
//    public static String extractPrimaryKey(String row, String primaryKey) {
//        int startIndex = row.indexOf(primaryKey + "=");
//        if (startIndex != -1) {
//            int endIndex = row.indexOf(",", startIndex);
//            if (endIndex == -1) {
//                endIndex = row.indexOf("}", startIndex); // Eğer bu key sondaysa
//            }
//            return "primary key = " + row.substring(startIndex + primaryKey.length() + 1, endIndex);
//        }
//        return "Primary key bulunamadı";
//    }




        //
//
//
//
//
//
//            try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=Try;integratedSecurity=true;encrypt=true;trustServerCertificate=true;")) {
//                String tableName = "eXE"; // Buraya tablo adını yazın
//                findPrimaryKey(connection, tableName);
//            } catch (SQLException e) {
//                e.printStackTrace();


//        public static void findPrimaryKey(Connection connection, String tableName) throws SQLException {
//            DatabaseMetaData metaData = connection.getMetaData();
//            try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName)) {
//                while (primaryKeys.next()) {
//                    String primaryKeyColumn = primaryKeys.getString("COLUMN_NAME");
//                    System.out.println("Primary Key Column: " + primaryKeyColumn);
//                }
//            }
//        }
//    }







