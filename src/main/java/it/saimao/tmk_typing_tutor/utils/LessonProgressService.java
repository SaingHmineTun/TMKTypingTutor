package it.saimao.tmk_typing_tutor.utils;

import it.saimao.tmk_typing_tutor.model.LessonProgress;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LessonProgressService {

  public static void saveLessonProgress(LessonProgress lessonProgress) {
        String sql = "INSERT OR REPLACE INTO lesson_progress(user_id, level_index, lesson_index, wpm, accuracy, mistakes) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
pstmt.setInt(1, lessonProgress.getUserId());
            pstmt.setInt(2, lessonProgress.getLevelIndex());
            pstmt.setInt(3, lessonProgress.getLessonIndex());
            pstmt.setInt(4, lessonProgress.getWpm());
            pstmt.setDouble(5, lessonProgress.getAccuracy());
            pstmt.setInt(6, lessonProgress.getMistakes());
            
// Add debugging output
            System.out.println("Saving lesson progress: User=" + lessonProgress.getUserId() + 
                             ", Level=" + lessonProgress.getLevelIndex() + 
                             ", Lesson=" + lessonProgress.getLessonIndex() + 
                             ", WPM=" + lessonProgress.getWpm() + 
                            ", Accuracy=" + lessonProgress.getAccuracy() + 
                             ", Mistakes=" + lessonProgress.getMistakes());
            
            int result = pstmt.executeUpdate();
            System.out.println("Rows affected: " + result);
        } catch (SQLException e) {
            System.out.println("Error saving lesson progress: " + e.getMessage());
            e.printStackTrace();
        }
    }

public static LessonProgress getLessonProgress(int userId, int levelIndex, int lessonIndex) {
        String sql = "SELECT * FROM lesson_progress WHERE user_id = ? AND level_index = ? AND lesson_index = ?";
        LessonProgress lessonProgress = null;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, levelIndex);
            pstmt.setInt(3, lessonIndex);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lessonProgress =new LessonProgress();
                lessonProgress.setId(rs.getInt("id"));
               lessonProgress.setUserId(rs.getInt("user_id"));
                lessonProgress.setLevelIndex(rs.getInt("level_index"));
                lessonProgress.setLessonIndex(rs.getInt("lesson_index"));
                lessonProgress.setWpm(rs.getInt("wpm"));
                lessonProgress.setAccuracy(rs.getDouble("accuracy"));
                lessonProgress.setMistakes(rs.getInt("mistakes"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lessonProgress;
    }

    public static List<LessonProgress> getAllLessonProgressForLevel(int userId, int levelIndex) {
       String sql = "SELECT * FROM lesson_progress WHERE user_id = ? AND level_index = ?";
List<LessonProgress> lessonProgressList = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, levelIndex);
            
            // Add debugging output
            System.out.println("Fetching lesson progress forUser=" + userId + ", Level=" + levelIndex);
            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LessonProgress lessonProgress = new LessonProgress();
                lessonProgress.setId(rs.getInt("id"));
                lessonProgress.setUserId(rs.getInt("user_id"));
                lessonProgress.setLevelIndex(rs.getInt("level_index"));
                lessonProgress.setLessonIndex(rs.getInt("lesson_index"));
                lessonProgress.setWpm(rs.getInt("wpm"));
                lessonProgress.setAccuracy(rs.getDouble("accuracy"));
                lessonProgress.setMistakes(rs.getInt("mistakes"));
                
// Add debugging output
                System.out.println("Found lesson progress: ID=" + lessonProgress.getId() + 
                                 ", User=" + lessonProgress.getUserId() + 
                                 ", Level=" + lessonProgress.getLevelIndex() + 
                                 ", Lesson=" + lessonProgress.getLessonIndex() + 
                                 ", WPM=" + lessonProgress.getWpm() + 
                                 ", Accuracy=" + lessonProgress.getAccuracy() + 
                                 ", Mistakes=" + lessonProgress.getMistakes());
                
                lessonProgressList.add(lessonProgress);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching lesson progress: "+ e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Total records found: " + lessonProgressList.size());
        return lessonProgressList;
    }

public static double getAverageWpmForLevel(int userId, int levelIndex) {
        String sql = "SELECT AVG(wpm) as average_wpm FROM lesson_progress WHERE user_id = ? AND level_index = ?";
        double averageWpm = 0.0;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, levelIndex);
            ResultSet rs= pstmt.executeQuery();

            if(rs.next()) {
                averageWpm = rs.getDouble("average_wpm");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return averageWpm;
    }
}