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
    private final String URL_withoutdatabaseStart = "jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=";
    private final String URL_withoutdatabaseEnd =";integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
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
        String fullUrl = URL_withoutdatabaseStart + database + URL_withoutdatabaseEnd;

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

        if (selectedRow != null && !selectedRow.isEmpty()) {
            selectedRow.clear();
        }
//        System.out.println(selectedRow); //Selected rowu basıyor
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

    public TableView<Map<String, Object>> getSearchedData(String database_name, String table_name) {
        if (selectedRow != null && !selectedRow.isEmpty()) {
            selectedRow.clear();
        }

        TableView<Map<String, Object>> tableViewSearched = new TableView<>();
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(URL)) {





        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        tableViewSearched.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRow = newSelection;
                String a = selectedRow.toString();
                System.out.println("Selected row: " + selectedRow);
            }
        });
        return tableViewSearched;
    }









    public ArrayList<String> getColumnNames(String database_name, String table_name){
        ArrayList<String> columnnamesArraylist= new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL)) {
            if (connection != null) {
                System.out.println("Connection established successfully.");
                Statement stmt = connection.createStatement();
                if(table_name != null) {
                    ResultSet sql = stmt.executeQuery("SELECT * FROM " + database_name +".[dbo]."+ table_name);
                    ResultSetMetaData rsMetaData = sql.getMetaData();
                    int count = rsMetaData.getColumnCount();
                    for(int i = 1; i<=count; i++) {
                        String columnName = rsMetaData.getColumnName(i);
                        columnnamesArraylist.add(columnName);
                    }
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
        return columnnamesArraylist;
    }




    public TableView<Map<String, Object>> deleteData(String database_name, String table_name) {

        String fullUrl = URL_withoutdatabaseStart + database_name + URL_withoutdatabaseEnd;
        String primaryKeyColumn=null;

        try (Connection connection = DriverManager.getConnection(fullUrl)) {
            if (connection != null) {
                System.out.println("Connection established successfully.");
//                Statement stmt = connection.createStatement();
                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, table_name)) {
                    while (primaryKeys.next()) {
                        primaryKeyColumn = primaryKeys.getString("COLUMN_NAME");
                        System.out.println("Primary Key Column: " + primaryKeyColumn);
                    }
                }

                String selectedRowString = selectedRow.toString();
//        PreparedStatement st = connection.prepareStatement("DELETE FROM Table WHERE name = '" + name + "';");


                //Eğer Primary key en sonda ise primary key ve değerini çıkarır
                String c = null;
                int startIndex = selectedRowString.indexOf(primaryKeyColumn);
                int endIndex = selectedRowString.indexOf("}");
                if (startIndex > 0 && endIndex > startIndex) {
                    c = selectedRowString.substring(startIndex, endIndex);
                    System.out.println(c);
                } else {
                    System.out.println("Value not found");
                }

                //Eğer Primary key'in son dışında olma durumları için
                String e =null;
                int startIndex2 = selectedRowString.indexOf(primaryKeyColumn);
                int endIndex2 = selectedRowString.indexOf(",");
                if (startIndex2 > 0 && endIndex2 > startIndex2) {
                    c = selectedRowString.substring(startIndex2, endIndex2);
                    System.out.println(c);
                } else {
                    System.out.println("Value not found");
                }






//                String d=null;
//                int startIndex2 = selectedRowString.indexOf("=") + 1;
//                int endIndex2 = selectedRowString.indexOf(",");
//                if (startIndex2 > 0 && endIndex2 > startIndex2) {
//                    String result = selectedRowString.substring(startIndex2, endIndex2);
//                    d = result;
//                    System.out.println(d);
//                } else {
//                    System.out.println("Value not found");
//                }
//
//
//
//
//
//
//                if(table_name != null) {
//                    System.out.println("DELETE FROM "+ database_name + ".[dbo]." + table_name + " WHERE " + c + "'"+ d + "'" + ";");
//                    PreparedStatement st = connection.prepareStatement("DELETE FROM "+ database_name + ".[dbo]." + table_name + " WHERE " + c + "'"+ d + "'" + ";");
//                    st.executeUpdate();
//                }

            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }






        return null;
    }


    public String getPrimaryKey(String database_name, String table_name)  {
        String fullUrl = URL_withoutdatabaseStart + database_name + URL_withoutdatabaseEnd;
        String primaryKeyColumn = null;
        String holder = null;
        try (Connection connection = DriverManager.getConnection(fullUrl)) {
            if (connection != null) {
                System.out.println("Connection established successfully.");

                DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, table_name)) {
                    while (primaryKeys.next()) {
                        primaryKeyColumn = primaryKeys.getString("COLUMN_NAME");
                        holder =("Primary Key Column: " + primaryKeyColumn);
                    }
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
        return holder;
    }

    public Map<String, Object> getSelectedRow() {
        return selectedRow;
    }
}

//"jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=Try;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";










/*
* YARIN YAPILACAKLAR
* UPDATE KISMI YENİ TABLE SEÇİNCE VE YA YENİ DATA BASE SEÇİNCE GÜNCELLENMİYOR////////////
* UPDATE KISMINA UPDATE TUŞU
* DELETE METHODUNUN PRİMARY KEYİNİ SQL KODUNA ÇIKARMASI LAZIM
* DELETE OLUNCA TEABLEVİEW'İN GÜNCELLENMESİ LAZIM
* CREATE KSIMINDA TEXTFIELD LER 3 LÜ SATIRLAR HALİNDE ALT ALT SIRALANSIN
* CRATE KSIMI İÇİN CRATE TUŞU
* */