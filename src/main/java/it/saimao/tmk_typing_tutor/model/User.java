package it.saimao.tmk_typing_tutor.model;

public class User {
    private int id;
    private String username;
    private String displayName;
    private String password;
    private int theme;
    private int lesson;
    private int level;
    private int keyboard;

    public User(String username, String password) {
        this.username = username;
        this.displayName = username;
        this.password = password;
    }

    public User(int id, String username, String displayName, String password, int theme, int lesson, int level, int keyboard) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.theme = theme;
        this.lesson = lesson;
        this.level = level;
        this.keyboard = keyboard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        if (displayName == null || displayName.isBlank())
            return username;
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(int keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public String toString() {
        return username;
    }
}
