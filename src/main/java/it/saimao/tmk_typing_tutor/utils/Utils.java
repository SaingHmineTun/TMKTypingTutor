package it.saimao.tmk_typing_tutor.utils;

import it.saimao.tmk_typing_tutor.Main;
import it.saimao.tmk_typing_tutor.model.Hand;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Utils {
    public static boolean isEnglishCharacter(String value) {
//        if (value != null || !value.isEmpty())
//            System.out.println(value.charAt(0) == 0x0061);
        char[] chars = value.toCharArray();
        for (char character: chars) {
            if ((character >= '\u0021' && character <= '\u007E')) {
                return true;
            }
        }
        return false;
    }

    public static Hand getAppropriateHand(String lowerEnglishCharacter) {
        Hand hand = new Hand();
        switch (lowerEnglishCharacter) {
            case "q", "a", "z", "1" -> hand.setLeftHand("left_pinky");
            case "2", "w", "s", "x" -> hand.setLeftHand("left_ring");
            case "3", "e", "d", "c" -> hand.setLeftHand("left_middle");
            case "4", "5", "r", "t", "f", "g", "v", "b" -> hand.setLeftHand("left_index");
            case "6", "7", "y", "u", "h", "j", "n", "m" -> hand.setRightHand("right_index");
            case "8", "i", "k", "," -> hand.setRightHand("right_middle");
            case "9", "o", "l", "." -> hand.setRightHand("right_ring");
            case "0", "p", ";", "/", "-", "=", "[", "]", "\\", "'" -> hand.setRightHand("right_pinky");
            case "space" -> {
                hand.setLeftHand("left_thumb");
                hand.setRightHand("right_thumb");
            }
        }
        return hand;
    }

    public static Image createIcon(String iconName, String iconColor) {
        String imagePath = String.format("/images/%s_%s.png", iconName, iconColor);

        // Check if the specific icon exists, if not use appropriate fallback
        if (Main.class.getResourceAsStream(imagePath) == null) {
            // For close, prev, next and retry icons, fallback to either dark orwhite depending on what's available
            if ("close".equals(iconName) || "prev".equals(iconName) || "next".equals(iconName) || "retry".equals(iconName)) {
                String fallbackColor = "white".equals(iconColor) ? "dark" : "white";
                String fallbackPath = String.format("/images/%s_%s.png", iconName, fallbackColor);
                if (Main.class.getResourceAsStream(fallbackPath) != null) {
                    imagePath = fallbackPath;
                } else {
                    // If neither color variant exists, use the default one without color suffix
                    imagePath = String.format("/images/%s.png", iconName);
                }
            }
        }

        // Final check to ensure we have a valid image path
        if (Main.class.getResourceAsStream(imagePath) == null) {
            imagePath = "/images/" + iconName + ".png";
        }

        Image image = new Image(Main.class.getResourceAsStream(imagePath), 20, 20, true, true);
        return image;
    }


}
