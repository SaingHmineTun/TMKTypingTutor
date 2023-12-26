package it.saimao.tmk_typing_tutor.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Burma_KeyMap {
    private static Map<String, String> row1Values;
    private static Map<String, String> row1ShiftValues;
    private static Map<String, String> row2Values;
    private static Map<String, String> row2ShiftValues;
    private static Map<String, String> row3Values;
    private static Map<String, String> row3ShiftValues;
    private static Map<String, String> row4Values;
    private static Map<String, String> row4ShiftValues;
    private static Map<String, String> row5Values;
    private static Map<String, String> row5ShiftValues;
    private static Map<String, String> allValuesMap;

    public static Map<String, String> getAllValuesMap() {
        return allValuesMap;
    }

    public static List<Map<String, String>> getAllValuesList() {
        return allValuesList;
    }

    public static List<Map<String, String>> allValuesList;
    static  {
        initValues();
    }

    private static void initValues() {
        // Row 1
        row1Values = new LinkedHashMap<>();
        row1Values.put("`" , "`");
        row1Values.put("1" , "၁");
        row1Values.put("2", "၂");
        row1Values.put("3", "၃");
        row1Values.put("4", "၄");
        row1Values.put("5", "၅");
        row1Values.put("6", "၆");
        row1Values.put("7", "၇");
        row1Values.put("8", "၈");
        row1Values.put("9", "၉");
        row1Values.put("0", "၀");
        row1Values.put("-", "-");
        row1Values.put("=", "=");
        row1Values.put("Back", "Back");

        // Row 1 Shift
        row1ShiftValues = new LinkedHashMap<>();
        row1ShiftValues.put("~", "~");
        row1ShiftValues.put("!", "ဍ");
        row1ShiftValues.put("@", "ၒ");
        row1ShiftValues.put("#", "ဋ");
        row1ShiftValues.put("$", "ၓ");
        row1ShiftValues.put("%", "ၔ");
        row1ShiftValues.put("^", "ၕ");
        row1ShiftValues.put("&", "ရ");
        row1ShiftValues.put("*", "*");
        row1ShiftValues.put("(", "(");
        row1ShiftValues.put(")", ")");
        row1ShiftValues.put("_", "_");
        row1ShiftValues.put("+", "+");
        row1ShiftValues.put("Back", "Back");

        // Row 2 Values
        row2Values = new LinkedHashMap<>();
        row2Values.put("Tab", "Tab");
        row2Values.put("q", "ဆ");
        row2Values.put("w", "တ");
        row2Values.put("e", "န");
        row2Values.put("r", "မ");
        row2Values.put("t", "အ");
        row2Values.put("y", "ပ");
        row2Values.put("u", "က");
        row2Values.put("i", "င");
        row2Values.put("o", "သ");
        row2Values.put("p", "စ");
        row2Values.put("[", "ဟ");
        row2Values.put("]", "ဩ");
        row2Values.put("\\", "၏");

        // Row 2 Shift Values
        row2ShiftValues = new LinkedHashMap<>();
        row2ShiftValues.put("Tab", "Tab");
        row2ShiftValues.put("Q", "ဈ");
        row2ShiftValues.put("W", "ဝ");
        row2ShiftValues.put("E", "ဣ");
        row2ShiftValues.put("R", "၎");
        row2ShiftValues.put("T", "ဤ");
        row2ShiftValues.put("Y", "၌");
        row2ShiftValues.put("U", "ဥ");
        row2ShiftValues.put("I", "၍");
        row2ShiftValues.put("O", "ဿ");
        row2ShiftValues.put("P", "ဏ");
        row2ShiftValues.put("{", "ဧ");
        row2ShiftValues.put("}", "ဪ");
        row2ShiftValues.put("|", "");

        // Row 3 Values
        row3Values = new LinkedHashMap<>();
        row3Values.put("Caps", "Caps");
        row3Values.put("a", "ေ");
        row3Values.put("s", "ျ");
        row3Values.put("d", "ိ");
        row3Values.put("f", "်");
        row3Values.put("g", "ါ");
        row3Values.put("h", "့");
        row3Values.put("j", "ြ");
        row3Values.put("k", "ု");
        row3Values.put("l", "ူ");
        row3Values.put(";", "း");
        row3Values.put("'", "'");
        row3Values.put("Enter", "Enter");

        row3ShiftValues = new LinkedHashMap<>();
        row3ShiftValues.put("Caps", "Caps");
        row3ShiftValues.put("A", "ဗ");
        row3ShiftValues.put("S", "ှ");
        row3ShiftValues.put("D", "ီ");
        row3ShiftValues.put("F", "္");
        row3ShiftValues.put("G", "ွ");
        row3ShiftValues.put("H", "ံ");
        row3ShiftValues.put("J", "ဲ");
        row3ShiftValues.put("K", "ဒ");
        row3ShiftValues.put("L", "ဓ");
        row3ShiftValues.put(":", "ဂ");
        row3ShiftValues.put("\"", "\"");
        row3ShiftValues.put("Enter", "Enter");

        // Row 4 Values
        row4Values = new LinkedHashMap<>();
        row4Values.put("Shift1", "Shift");
        row4Values.put("z", "ဖ");
        row4Values.put("x", "ထ");
        row4Values.put("c", "ခ");
        row4Values.put("v", "လ");
        row4Values.put("b", "ဘ");
        row4Values.put("n", "ည");
        row4Values.put("m", "ာ");
        row4Values.put(",", ",");
        row4Values.put(".", ".");
        row4Values.put("/", "/");
        row4Values.put("Shift2", "Shift");

        // Row 4 Shift Values
        row4ShiftValues = new LinkedHashMap<>();
        row4ShiftValues.put("Shift1", "Shift");
        row4ShiftValues.put("Z", "ဇ");
        row4ShiftValues.put("X", "ဌ");
        row4ShiftValues.put("C", "ဃ");
        row4ShiftValues.put("V", "ဠ");
        row4ShiftValues.put("B", "ယ");
        row4ShiftValues.put("N", "ဉ");
        row4ShiftValues.put("M", "ဦ");
        row4ShiftValues.put("<", "၊");
        row4ShiftValues.put(">", "။");
        row4ShiftValues.put("?", "?");
        row4ShiftValues.put("Shift2", "Shift");

        // Row 5 Values
        row5Values = new LinkedHashMap<>();
        row5Values.put("Ctrl1", "Ctrl");
        row5Values.put("Win1", "Win");
        row5Values.put("Alt1", "Alt");
        row5Values.put("Space", "Space");
        row5Values.put("Alt2", "Alt");
        row5Values.put("Win2", "Win");
        row5Values.put("Menu", "Menu");
        row5Values.put("Ctrl2", "Ctrl");

        row5ShiftValues = new LinkedHashMap<>();
        row5ShiftValues.put("Ctrl1", "Ctrl");
        row5ShiftValues.put("Win1", "Win");
        row5ShiftValues.put("Alt1", "Alt");
        row5ShiftValues.put("Space", "Space");
        row5ShiftValues.put("Alt2", "Alt");
        row5ShiftValues.put("Win2", "Win");
        row5ShiftValues.put("Menu", "Menu");
        row5ShiftValues.put("Ctrl2", "Ctrl");

        allValuesList = Arrays.asList(row1Values, row1ShiftValues, row2Values, row2ShiftValues, row3Values, row3ShiftValues, row4Values, row4ShiftValues, row5Values, row5ShiftValues);

        allValuesMap = new LinkedHashMap<>();
        allValuesMap.putAll(row1Values);
        allValuesMap.putAll(row1ShiftValues);
        allValuesMap.putAll(row2Values);
        allValuesMap.putAll(row2ShiftValues);
        allValuesMap.putAll(row3Values);
        allValuesMap.putAll(row3ShiftValues);
        allValuesMap.putAll(row4Values);
        allValuesMap.putAll(row4ShiftValues);
        allValuesMap.putAll(row5Values);
        allValuesMap.putAll(row5ShiftValues);
    }
}
