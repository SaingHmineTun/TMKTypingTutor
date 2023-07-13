package it.saimao.tmk_typing.utils;

import java.util.Map;

public class Utils {
    public static boolean isEnglishConsonant(String value) {
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



}
