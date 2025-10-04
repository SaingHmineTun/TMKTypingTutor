package it.saimao.tmk_typing_tutor.model;

public enum Theme {
    DARK("dark_theme", "Dark", "white"),
    LIGHT("light_theme", "Light", "dark"),
    GREEN("green_theme", "DarkGreen", "white"),
    CRIMSON_RED("crimson_red_theme", "Red", "white"),
    FROST("frost_theme", "Frost", "dark"),
    GOLDEN("golden_theme", "Golden", "dark"),
    MIDNIGHT("midnight_theme", "Midnight", "white"),
    MONOCHROME("monochrome_theme", "Monochrome", "white"),
    OCEAN("ocean_theme", "Ocean", "dark"),
    PASTEL("pastel_theme", "Pastel", "dark"),
    PINKY("pinky_theme", "Pinky", "dark"),
    SUNSET("sunset_theme", "Sunset", "dark"),
    NEON("neon_theme", "Neon", "white");

    private final String id;
    private final String displayName;
    private final String iconColor;

    Theme(String id, String displayName, String iconColor) {
        this.id = id;
        this.displayName = displayName;
        this.iconColor = iconColor;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public String iconColor() {
        return iconColor;
    }

    public static Theme fromIndex(int index) {
        Theme[] themes = values();
        if (index >= 0 && index < themes.length) {
            return themes[index];
        }
        return LIGHT; // default theme
    }
}