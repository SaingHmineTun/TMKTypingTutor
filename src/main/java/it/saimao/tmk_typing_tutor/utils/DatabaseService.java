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
            throw new RuntimeException("Could not initialize database path", e);
        }
    }

  public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createUserTableSql= "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " username text NOT NULL UNIQUE,\n"
                + " password text NOT NULL,\n"
                + " theme INTEGER DEFAULT 0,\n"
                + " lesson INTEGER DEFAULT 0,\n"
                + " level INTEGER DEFAULT 0,\n"
                + " keyboard INTEGER DEFAULT 0\n"
                + ");";

        String createProgressTableSql = "CREATE TABLE IF NOT EXISTS progress (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " user_id INTEGER NOT NULL,\n"
                + " level_index INTEGER NOT NULL,\n"
                + " lesson_index INTEGER NOT NULL,\n"
                + " FOREIGN KEY (user_id) REFERENCES users (id),\n"
                + " UNIQUE(user_id, level_index, lesson_index)\n"
                + ");";
                
        String createLessonProgressTableSql = "CREATE TABLE IF NOT EXISTS lesson_progress (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " user_id INTEGER NOT NULL,\n"
                + " level_index INTEGER NOT NULL,\n"
                + " lesson_index INTEGER NOT NULL,\n"
                + " wpm INTEGERNOTNULL,\n"
                + " accuracy REAL NOT NULL,\n"
                + " mistakes INTEGER NOT NULL,\n"
                + " FOREIGN KEY (user_id) REFERENCES users (id),\n"
                + " UNIQUE(user_id, level_index, lesson_index)\n"
                + ");";

        try (Connection conn= getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTableSql);
            stmt.execute(createProgressTableSql);
            stmt.execute(createLessonProgressTableSql);

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