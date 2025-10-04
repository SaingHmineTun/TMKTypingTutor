package it.saimao.tmk_typing_tutor.utils;

import it.saimao.tmk_typing_tutor.model.Lesson;
import it.saimao.tmk_typing_tutor.model.LessonProgress;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
            System.out.println("Saving lesson progress: User="+ lessonProgress.getUserId() + 
                             ", Level=" + lessonProgress.getLevelIndex() + 
                             ", Lesson=" + lessonProgress.getLessonIndex() + 
                             ", WPM=" + lessonProgress.getWpm() + 
                            ", Accuracy=" + lessonProgress.getAccuracy() + 
                             ", Mistakes=" +lessonProgress.getMistakes());
            
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
        } catch(SQLException e) {
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
            System.out.println("Fetching lesson progress for User=" +userId + ", Level=" + levelIndex);
            
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
            System.out.println("Error fetching lesson progress: "+e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Total records found: " + lessonProgressList.size());
        return lessonProgressList;
    }

    public static double getAverageWpmForLevel(int userId, int levelIndex) {
        String sql = "SELECT AVG(wpm) as average_wpmFROM lesson_progress WHERE user_id = ? AND level_index = ?";
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
    
    public static List<Lesson> getLessonsForLevel(int levelIndex) {
List<Lesson> lessonList = new ArrayList<>();
        InputStream is = null;
        
        try {
            if (levelIndex == 0) {
                is = LessonProgressService.class.getResourceAsStream("/assets/lesson_1.csv");
            } else if (levelIndex == 1) {
                is = LessonProgressService.class.getResourceAsStream("/assets/lesson_2.csv");
            } else if (levelIndex == 2) {
                is = LessonProgressService.class.getResourceAsStream("/assets/lesson_3.csv");
            } else if (levelIndex == 3) {
                is = LessonProgressService.class.getResourceAsStream("/assets/lesson_4.csv");
            }
            
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.replaceAll("\\uFEFF", "");
                        String[] values = line.split(",");
                        int no = Integer.parseInt(values[0].trim());
                        String title = values[1].trim();
                        String content = values[2].replace("\"", "").trim();
                        lessonList.add(new Lesson(no, title, content));
                    }
                    if (levelIndex == 1)// Level 2
                        Collections.reverse(lessonList);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading lessons for level " + levelIndex + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return lessonList;
    }
    
public static boolean isUserLevelCompleted(int userId, int levelIndex) {
        // 获取该级别的总课程数
        List<Lesson> lessons = getLessonsForLevel(levelIndex);
        int totalLessons = lessons.size();
        
        // 如果没有课程，则认为未完成
        if (totalLessons <= 0) {
            return false;
        }
        
        // 获取用户在该级别已完成的课程数
        List<LessonProgress> completedLessons = getAllLessonProgressForLevel(userId, levelIndex);
        int completedCount = completedLessons.size();
        
        // 特殊处理：对于级别0（基础级别），需要完成9节课
        if (levelIndex == 0) {
            totalLessons = 9; // 基础级别需要完成9节课
        }
        
        // 检查用户是否完成了足够的课程
        return completedCount >= totalLessons;
    }
}