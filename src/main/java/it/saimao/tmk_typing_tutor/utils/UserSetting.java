package it.saimao.tmk_typing_tutor.utils;


import java.util.prefs.Preferences;

public class UserSetting {
    private static final Preferences preferences = Preferences.userNodeForPackage(UserSetting.class);
    private static final String THEME_KEY = "theme";
    private static final String KEYBOARD_KEY = "keyboard";

    public static void saveTheme(int theme) {
        preferences.putInt(THEME_KEY, theme);
    }

    public static int loadTheme() {
        return preferences.getInt(THEME_KEY, 0);
    }

    public static void saveKeyboard(int index) {
        preferences.putInt(KEYBOARD_KEY, index);
    }

    public static int loadKeyboard() {
        return preferences.getInt(KEYBOARD_KEY, 0);
    }
}
