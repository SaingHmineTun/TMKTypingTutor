package it.saimao.tmk_typing_tutor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
        String createUserTableSql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " username text NOT NULL UNIQUE,\n"
                + " password text NOT NULL\n"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(createUserTableSql);

            // Add columns for user settings if they don't exist
            if (!columnExists(conn, "users", "theme")) {
                stmt.execute("ALTER TABLE users ADD COLUMN theme INTEGER DEFAULT 0");
            }
            if (!columnExists(conn, "users", "lesson")) {
                stmt.execute("ALTER TABLE users ADD COLUMN lesson INTEGER DEFAULT 0");
            }
            if (!columnExists(conn, "users", "level")) {
                stmt.execute("ALTER TABLE users ADD COLUMN level INTEGER DEFAULT 0");
            }
            if (!columnExists(conn, "users", "keyboard")) {
                stmt.execute("ALTER TABLE users ADD COLUMN keyboard INTEGER DEFAULT 0");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean columnExists(Connection connection, String tableName, String columnName) throws SQLException {
        ResultSet rs = connection.getMetaData().getColumns(null, null, tableName, columnName);
        return rs.next();
    }
}
