package it.saimao.tmk_typing.utils

class KeyValue {

    companion object {
        private val row1Values = linkedMapOf(
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
        private val row1ShiftValues = linkedMapOf(
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
        private val row2Values = linkedMapOf(
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

        private val row2ShiftValues = linkedMapOf(
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
        private val row3Values = linkedMapOf(
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
        private val row3ShiftValues = linkedMapOf(
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
        private val row4Values = linkedMapOf(
            "Shift1" to "Shift",
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
            "Shift2" to "Shift"
        )
        private val row4ShiftValues = linkedMapOf(
            "Shift1" to "Shift",
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
            "Shift2" to "Shift"
        )
        private val row5Values = linkedMapOf(
            "Ctrl1" to "Ctrl",
            "Win1" to "Win",
            "Alt1" to "Alt",
            "Space" to "Space",
            "Alt2" to "Alt",
            "Win2" to "Win",
            "Menu" to "Menu",
            "Ctrl2" to "Ctrl"
        )
        private val row5ShiftValues = linkedMapOf(
            "Ctrl1" to "Ctrl",
            "Win1" to "Win",
            "Alt1" to "Alt",
            "Space" to "Space",
            "Alt2" to "Alt",
            "Win2" to "Win",
            "Menu" to "Menu",
            "Ctrl2" to "Ctrl"
        )

        val allValuesList = arrayListOf(
            row1Values,
            row1ShiftValues,
            row2Values,
            row2ShiftValues,
            row3Values,
            row3ShiftValues,
            row4Values,
            row4ShiftValues,
            row5Values,
            row5ShiftValues
        )
        val allValuesMap =
            row1Values + row1ShiftValues + row2Values + row2ShiftValues + row3Values + row3ShiftValues + row4Values + row4ShiftValues + row5Values + row5ShiftValues

        fun findKeyByValue(str: String): Any = {
            for (value in allValuesList) {
                if (value.containsValue(str)) {
                    value.entries.find { it.value == str }?.key
                }
            }

        }
    }


}