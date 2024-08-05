package com.example.simplesqljavaapp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Sql {
    private final String URL = "jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    private  final String URL_withoutdatabase = "jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=";
    private Map<String, Object> selectedRow;

    public Sql() {
    }


    public ArrayList<String> getDatabaseNames() {

        ArrayList<String> databases = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT name FROM sys.databases WHERE name NOT IN ('master', 'tempdb', 'model', 'msdb')")) {

            while (resultSet.next()) {
                databases.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databases;
    }

    public ArrayList<String> getTableNames(String database) {

        ArrayList<String> tables = new ArrayList<>();
        String fullUrl = URL_withoutdatabase + database + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

        try (Connection connection = DriverManager.getConnection(fullUrl)) {
                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet resultSet = metaData.getTables(null, null, "%", new String[] {"TABLE"})) {
                    while (resultSet.next()) {
                        if(!resultSet.getString("TABLE_NAME").equals("sysdiagrams")&&
                                !resultSet.getString("TABLE_NAME").equals("trace_xe_event_map")&&
                                !resultSet.getString("TABLE_NAME").equals("trace_xe_action_map")) {
                            tables.add(resultSet.getString("TABLE_NAME"));
                        }
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }

    public TableView<Map<String, Object>> getData(String database_name, String table_name) {
//        if(selectedRow!=null) {
//            selectedRow.clear();
//        }


        TableView<Map<String, Object>> tableView = new TableView<>();
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(URL)) {
            if (connection != null) {
                System.out.println("Connection established successfully.");
                Statement stmt = connection.createStatement();

                if(table_name != null) {
                    ResultSet sql = stmt.executeQuery("SELECT * FROM " + database_name +".[dbo]."+ table_name);
                    System.out.println("SELECT * FROM " + database_name + ".[dbo]." + table_name);
                    ResultSetMetaData rsMetaData = sql.getMetaData();
                    int count = rsMetaData.getColumnCount();
                    for(int i = 1; i<=count; i++) {
                        String columnName = rsMetaData.getColumnName(i);
                        TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                        column.setPrefWidth(100);
                        column.setResizable(false);
                        column.setReorderable(false);
                        column.setSortable(false);
                        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(columnName)));
                        tableView.getColumns().add(column);
//                        System.out.println(tableView.getSelectionModel().selectedItemProperty());
                    }
                    while (sql.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= count; i++) {
                            String columnName = rsMetaData.getColumnName(i);
                            row.put(columnName, sql.getObject(i));
                        }
                        observableList.add(row);
                    }
                    tableView.setItems(observableList);
                }

            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRow = newSelection;
                String a = selectedRow.toString();
                System.out.println("Selected row: " + selectedRow);
            }
        });
        return tableView;
    }
//    tableView.getSelectionModel().selectedItemProperty() nerde kullanabilirim



    public TableView<Map<String, Object>> deleteData() {


        return null;
    }




    public Map<String, Object> getSelectedRow() {
        return selectedRow;
    }
}

//"jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=Try;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

