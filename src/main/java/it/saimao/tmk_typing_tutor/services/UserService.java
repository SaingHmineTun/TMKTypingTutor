package it.saimao.tmk_typing_tutor.services;

import it.saimao.tmk_typing_tutor.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public static void saveUser(User user) {
        String sql = "INSERT INTO users (username, displayName, password, background_music, error_sound) VALUES(?,?,?,?,?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getDisplayName());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getBackgroundMusic());
            pstmt.setInt(5, user.getErrorSound());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("displayName"),
                        rs.getString("password"),
                        rs.getInt("theme"),
                        rs.getInt("lesson"),
                        rs.getInt("level"),
                        rs.getInt("keyboard"),
                        rs.getInt("background_music"),
                        rs.getInt("error_sound")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static User getUserById(int userId) {
        String sql = "SELECT* FROM users WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("displayName"),
                        rs.getString("password"),
                        rs.getInt("theme"),
                        rs.getInt("lesson"),
                        rs.getInt("level"),
                        rs.getInt("keyboard"),
                        rs.getInt("background_music"),
                        rs.getInt("error_sound")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("displayName"),
                        rs.getString("password"),
                        rs.getInt("theme"),
                        rs.getInt("lesson"),
                        rs.getInt("level"),
                        rs.getInt("keyboard"),
                        rs.getInt("background_music"),
                        rs.getInt("error_sound")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static void updateUser(User user) {
        String sql = "UPDATE users SET displayName = ?, theme = ?, lesson = ?, level = ?, keyboard = ?, background_music = ?, error_sound = ? WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getDisplayName());
            pstmt.setInt(2, user.getTheme());
            pstmt.setInt(3, user.getLesson());
            pstmt.setInt(4, user.getLevel());
            pstmt.setInt(5, user.getKeyboard());
            pstmt.setInt(6, user.getBackgroundMusic());
            pstmt.setInt(7, user.getErrorSound());
            pstmt.setInt(8, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updatePassword(User user) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getPassword());
            pstmt.setInt(2, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser(int userId) {
        String deleteUserSql = "DELETE FROM users WHERE id = ?";
        String deleteProgressSql = "DELETE FROM lesson_progress WHERE user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql);
             PreparedStatement deleteProgressStmt = conn.prepareStatement(deleteProgressSql)) {

            // First delete all progress records for this user
            deleteProgressStmt.setInt(1, userId);
            deleteProgressStmt.executeUpdate();

            // Then delete the user
            deleteUserStmt.setInt(1, userId);
            deleteUserStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}