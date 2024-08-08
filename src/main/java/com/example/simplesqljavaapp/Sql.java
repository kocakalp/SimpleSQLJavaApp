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

    public TableView<Map<String, Object>> getSearchedData(String database_name, String table_name, String searched) {
        if (selectedRow != null && !selectedRow.isEmpty()) {
            selectedRow.clear();
        }
        String fullUrl = URL_withoutdatabaseStart + database_name + URL_withoutdatabaseEnd;
        TableView<Map<String, Object>> tableViewSearched = new TableView<>();
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(fullUrl)) {
            System.out.println("Connection established successfully.");
            Statement stmt = connection.createStatement();

            if(table_name != null) {
//                ResultSet sql = stmt.executeQuery("SELECT * FROM " + database_name +".[dbo]."+ table_name);

                ResultSet sql = stmt.executeQuery("DECLARE @TableName nvarchar(256) = '" + table_name + "';\n" +
                        "DECLARE @Find nvarchar(50) = '" + searched + "';\n" +
                        "\n" +
                        "DECLARE @sql nvarchar(max) = '';\n" +
                        "\n" +
                        "-- Generate the dynamic SQL for filtering\n" +
                        "SELECT @sql = @sql + \n" +
                        "    CASE \n" +
                        "        WHEN @sql = '' THEN ''\n" +
                        "        ELSE '+ '\n" +
                        "    END + 'CONVERT(nvarchar(max), ISNULL(' + cols.COLUMN_NAME + ', '''')) COLLATE DATABASE_DEFAULT '\n" +
                        "FROM INFORMATION_SCHEMA.COLUMNS cols\n" +
                        "WHERE cols.TABLE_NAME = @TableName\n" +
                        "--AND cols.COLUMN_NAME IN ('genre_id', 'genre', 'Movie_id');\n" +
                        "\n" +
                        "-- Construct the final SQL query\n" +
                        "SELECT @sql = 'SELECT TOP (1000) * FROM ' + @TableName + ' WHERE ' + @sql + ' LIKE ''%' + @Find + '%''';\n" +
                        "\n" +
                        "-- Execute the dynamic SQL\n" +
                        "EXEC(@sql);\n");

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
                    tableViewSearched.getColumns().add(column);
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
                tableViewSearched.setItems(observableList);

            } else {
            System.out.println("Failed to establish connection.");
        }




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
                int endIndex=0;
                int startIndex = selectedRowString.indexOf(primaryKeyColumn + "=");
                if(startIndex != -1) {
                     endIndex =selectedRowString.indexOf(",", startIndex);
                    if (endIndex == -1) {
                        endIndex = selectedRowString.indexOf("}", startIndex); //primarykey'in sonda olma durumu için
                    }
                }
                if(table_name != null) {
                    System.out.println("DELETE FROM "+ database_name + ".[dbo]." + table_name + " WHERE " + primaryKeyColumn +" = " + "'"
                            + selectedRowString.substring(startIndex + primaryKeyColumn.length() + 1, endIndex) + "'" + ";");

                    PreparedStatement st = connection.prepareStatement("DELETE FROM "+ database_name + ".[dbo]." + table_name + " WHERE " +
                            primaryKeyColumn +" = " + "'"+ selectedRowString.substring(startIndex + primaryKeyColumn.length() + 1, endIndex) + "'" + ";");
                    st.executeUpdate();
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

    public boolean createTable(String database_name, String create) {
        String fullUrl = URL_withoutdatabaseStart + database_name + URL_withoutdatabaseEnd;

        try (Connection connection = DriverManager.getConnection(fullUrl)) {
            if (connection != null) {
                System.out.println("Connection established successfully.");
                Statement stmt = connection.createStatement();

                if(database_name != null) {
                    int a = stmt.executeUpdate(create);
                    if (a == 0) {
                        return true;
                    } else {
                        return false;
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

        return false;
    }


    public Map<String, Object> getSelectedRow() {
        return selectedRow;
    }
}

//"jdbc:sqlserver://stibrsnbim041\\SQLEXPRESS:1433;databaseName=Try;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";










/*
*                                            TO-DO
*
*                                              7/8/24
* ////////////UPDATE KISMI YENİ TABLE SEÇİNCE VE YA YENİ DATA BASE SEÇİNCE GÜNCELLENMİYOR////////////
* ////////////DELETE METHODUNUN PRİMARY KEYİNİ SQL KODUNA ÇIKARMASI LAZIM////////////
* ////////////DELETE OLUNCA TEABLEVİEW'İN GÜNCELLENMESİ LAZIM////////////
* ////////////INSERT KSIMINDA TEXTFIELD LER 3 LÜ SATIRLAR HALİNDE ALT ALT SIRALANSIN////////////
*
*
*

* DELETE TABLODA PRIMARY KEY YOKSA HATTA ÇIKARIYOR(SILME IŞLEMI GERÇEKLEŞMIYOR)
* DELETE SILINECEK ROW'UN BULUNDUĞU TABLE'IN BAŞKA TABLELAR'LA BAĞLANTISI VARSA HATTA ÇIKARIYOR
* SEARCH INTEGER DEĞERLERDEN BAZILARINDA HATTA ÇIKARIYOR(GALIBA BIR TABLE DA FULL INTEGER VARSA)
* EĞER SEARCH YAPIP DELETE IŞLEMI YAPILIRSA BÜTÜN TABLE GÖZÜKÜYOR
* CREATE KISIMINDA FILE PATHI DINAMIK OLARAK ALMIYOR
* */