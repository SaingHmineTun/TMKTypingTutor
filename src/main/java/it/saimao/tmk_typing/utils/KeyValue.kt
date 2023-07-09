package it.saimao.tmk_typing.utils

class KeyValue {
//    private val row3Values = arrayOf("Caps", "ေ", "ႄ", "ိ", "်", "ွ", "ႉ", "ႇ", "ု", "ူ", "ႈ", "'", "Enter")


    companion object {
        val row1Values = mutableMapOf(
                "`" to "`",
                "1" to "1",
                "2" to "2",
                "3" to "3",
                "4" to "4",
                "5" to "5",
                "6" to "6",
                "7" to "7",
                "8" to "8",
                "9" to "9",
                "0" to "0",
                "-" to "-",
                "=" to "=",
                "Back" to "Back"
        )
        val row1ShiftValues = mutableMapOf(
                "~" to "~",
                "!" to "!",
                "@" to "@",
                "#" to "#",
                "$" to "$",
                "%" to "%",
                "^" to "^",
                "&" to "&",
                "*" to "*",
                "(" to "(",
                ")" to ")",
                "_" to "_",
                "+" to "+",
                "Back" to "Back"
        )
        val row2Values = mutableMapOf(
                "Tab" to "Tab",
                "q" to "ၸ",
                "w" to "တ",
                "e" to "ၼ",
                "r" to "မ",
                "t" to "ဢ",
                "y" to "ပ",
                "u" to "ၵ",
                "i" to "င",
                "o" to "ဝ",
                "p" to "ႁ",
                "[" to "[",
                "]" to "]",
                "\\" to "\\"
        )

        val row2ShiftValues = mutableMapOf(
                "Tab" to "Tab",
                "Q" to "ꩡ",
                "W" to "ၻ",
                "E" to "ꧣ",
                "R" to "႞",
                "T" to "ြ",
                "Y" to "ၿ",
                "U" to "ၷ",
                "I" to "ရ",
                "O" to "သ",
                "P" to "ႀ",
                "{" to "{",
                "}" to "}",
                "|" to "|"
        )
        val row3Values = mutableMapOf(
                "Caps" to "Caps",
                "a" to "ေ",
                "s" to "ႄ",
                "d" to "ိ",
                "f" to "်",
                "g" to "ွ",
                "h" to "ႉ",
                "j" to "ႇ",
                "k" to "ု",
                "l" to "ူ",
                ";" to "ႈ",
                "'" to "'",
                "Enter" to "Enter"
        )
        val row3ShiftValues = mutableMapOf(
                "Caps" to "Caps",
                "A" to "ဵ",
                "S" to "ႅ",
                "D" to "ီ",
                "F" to "ႂ်",
                "G" to "ႂ",
                "H" to "့",
                "J" to "ႆ",
                "K" to "”",
                "L" to "ႊ",
                ":" to "း",
                "\"" to "“",
                "Enter" to "Enter"
        )
        val row4Values = mutableMapOf(
                "Shift" to "Shift",
                "z" to "ၽ",
                "x" to "ထ",
                "c" to "ၶ",
                "v" to "လ",
                "b" to "ယ",
                "n" to "ၺ",
                "m" to "ၢ",
                "," to ",",
                "." to ".",
                "/" to "/",
                "Shift" to "Shift"
        )
        val row4ShiftValues = mutableMapOf(
                "Shift" to "Shift",
                "Z" to "ၾ",
                "X" to "ꩪ",
                "C" to "ꧠ",
                "V" to "ꩮ",
                "B" to "ျ",
                "N" to "႟",
                "M" to "ႃ",
                "<" to "၊",
                ">" to "။",
                "?" to "?",
                "Shift" to "Shift"
        )
        val row5Values = mutableMapOf(
                "Ctrl" to "Ctrl",
                "Win" to "Win",
                "Alt" to "Alt",
                "Space" to "Space",
                "Alt" to "Alt",
                "Win" to "Win",
                "Menu" to "Menu",
                "Ctrl" to "Ctrl"
        )
        val row5ShiftValues = mutableMapOf(
                "Ctrl" to "Ctrl",
                "Win" to "Win",
                "Alt" to "Alt",
                "Space" to "Space",
                "Alt" to "Alt",
                "Win" to "Win",
                "Menu" to "Menu",
                "Ctrl" to "Ctrl"
        )

        val allValuesList = arrayListOf(row1Values, row1ShiftValues, row2Values, row2ShiftValues, row3Values, row3ShiftValues, row4Values, row4ShiftValues, row5Values, row5ShiftValues)
        val allValuesMap = row1Values + row1ShiftValues + row2Values + row2ShiftValues + row3Values + row3ShiftValues + row4Values + row4ShiftValues + row5Values + row5ShiftValues
        fun findKeyByValue(str: String): Any = {
            for (value in allValuesList) {
                if (value.containsValue(str)) {
                    value.entries.find { it.value == str }?.key
                }
            }

        }
    }



}