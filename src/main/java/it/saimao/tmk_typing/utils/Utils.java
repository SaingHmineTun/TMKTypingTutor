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

    public static String convertToShanChar(String character) {
//        Map<String, String> map = KeyValue.Companion.getAllValuesMap();
//        for (Map.Entry<String, String> entry: map.entrySet()) {
//            if (entry.getValue().equals(character)) {
//                return entry.getKey();
//            }
//        }
//        return null;
        Map<String, String> map = KeyValue.getAllValuesMap();
        return map.getOrDefault(character, null);
    }

}
