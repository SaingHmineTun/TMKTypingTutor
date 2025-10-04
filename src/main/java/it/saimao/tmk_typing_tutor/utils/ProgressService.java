package it.saimao.tmk_typing_tutor.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProgressService {

    public static void saveProgress(int userId, int levelIndex, int lessonIndex) {
        String sql = "INSERT OR IGNORE INTO progress(user_id, level_index, lesson_index) VALUES(?,?,?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, levelIndex);
            pstmt.setInt(3, lessonIndex);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getCompletedLessonCount(int userId, int levelIndex) {
        String sql = "SELECT COUNT(DISTINCT lesson_index) FROM progress WHERE user_id = ? AND level_index = ?";
        int count = 0;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, levelIndex);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
}
