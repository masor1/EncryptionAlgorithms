package com.masorone.encryptionalgorithms

import android.content.res.Resources

interface ProvideResources {

    fun string(idRes: Int, formatArgs: String = ""): String

    class Base(private val resources: Resources) : ProvideResources {

        override fun string(idRes: Int, formatArgs: String) = with(resources) {
            if (formatArgs.isEmpty())
                getString(idRes)
            else
                getString(idRes, if (" " == formatArgs) "Пробел" else formatArgs)
        }
    }

    class Test : ProvideResources {

        override fun string(idRes: Int, formatArgs: String): String {
            return when (idRes) {
                R.string.error_empty_message -> "Пустое поле. " + "Введите текст."
                R.string.error_negative_key -> "Вы ввели ключ: $formatArgs. " +
                        "Ключ не может быть меньше 0."
                R.string.error_unknown_sign -> "Неизвестный символ " +
                        "\"${if (" " == formatArgs) "Пробел" else formatArgs}\"." +
                        "Используйте строчные русские буквы."
                else -> ""
            }
        }
    }
}