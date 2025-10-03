package it.saimao.tmk_typing_tutor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {

    private static final String DB_URL;

    static {
        try {
            String dbDir = System.getProperty("user.home") + File.separator + ".tmk_typing_tutor";
            Path dbDirPath = Paths.get(dbDir);
            if (Files.notExists(dbDirPath)) {
                Files.createDirectories(dbDirPath);
            }
            String dbPath = dbDir + File.separator + "typing_tutor.db";
            DB_URL = "jdbc:sqlite:" + dbPath;
        } catch (IOException e) {
            // Handle exception, maybe log it or rethrow as a runtime exception
            throw new RuntimeException("Could not initialize database path", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // Explicitly load the SQLite JDBC driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " username text NOT NULL UNIQUE,\n"
                + " password text NOT NULL\n"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
