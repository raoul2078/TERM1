package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:sqlite:term1.db";
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    void createConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Something went wrong: " + e.getMessage());
            System.exit(0);
        }
    }


    void setupDietTable() {
        String TABLE_NAME = "DIET";
    }

    public static void main(String[] args) {
        DatabaseHandler databaseHandler = getInstance();
    }
}
