package com.masorone.encryptionalgorithms

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EncryptionAlgorithmCaesarRuLowerCaseTest {

    private lateinit var encryptionAlgorithm: EncryptionAlgorithm.Caesar.RuLowerCase

    @Before
    fun setUp() {
        encryptionAlgorithm = EncryptionAlgorithm.Caesar.RuLowerCase(
            VerifiedData.StringMessageAndKey(
                resources = ProvideResources.Test()
            )
        )
    }

    //region encrypt tests
    @Test
    fun `encrypt empty message`() {
        val expected = EncryptionAlgorithm.Result.EmptyMessage(
            message = "Пустое поле. Введите текст."
        )
        val actual = encryptionAlgorithm.encrypt("", "1")
        assertEquals(expected, actual)
    }

    @Test
    fun `encrypt empty message and key`() {
        val expected = EncryptionAlgorithm.Result.EmptyMessage(
            message = "Пустое поле. Введите текст."
        )
        val actual = encryptionAlgorithm.encrypt("", "")
        assertEquals(expected, actual)
    }

    @Test
    fun `encrypt empty key or zero`() {
        val expected = EncryptionAlgorithm.Result.Success("абв")
        var actual = encryptionAlgorithm.encrypt("абв", "")
        assertEquals(expected, actual)
        actual = encryptionAlgorithm.encrypt("абв", "0")
        assertEquals(expected, actual)
    }

    @Test
    fun `encrypt negative key`() {
        val listOfKeys = listOf("-1", "-21")
        listOfKeys.forEach { key ->
            val expected = EncryptionAlgorithm.Result.NegativeKey(
                message = "Вы ввели ключ: $key. Ключ не может быть меньше 0."
            )
            val actual = encryptionAlgorithm.encrypt("абв", key)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `encrypt key bigger alphabet size`() {
        val listOfKeys = listOf(
            "33", "34", "35",
            "40", "50", "60",
            "100"
        )
        val listOfExpected = listOf(
            "абвя", "бвга", "вгдб",
            "жзиё", "рстп", "ъыьщ",
            "бвга"
        )

        listOfKeys.forEachIndexed { index, key ->
            val actual = encryptionAlgorithm.encrypt("абвя", key)
            assertEquals(EncryptionAlgorithm.Result.Success(listOfExpected[index]), actual)
        }
    }

    @Test
    fun `encrypt key corner cases`() {
        val actual = encryptionAlgorithm.encrypt("эюя", "3")
        val expected = EncryptionAlgorithm.Result.Success("абв")
        assertEquals(expected, actual)
    }

    @Test
    fun `encrypt other languages or digits or special signs`() {
        val listOfMessages = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "P!", "валDK", "ЖЛcД", "ды(с", " "
        )
        val listOfExpected = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "P", "D", "Ж", "(", "Пробел"
        )
        listOfMessages.forEachIndexed { index, str ->
            val expected = EncryptionAlgorithm.Result.UnknownSign(
                message = "Неизвестный символ \"${listOfExpected[index]}\"." +
                        "Используйте строчные русские буквы."
            )
            val actual = encryptionAlgorithm.encrypt(str, "1")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `encrypt lower case Ru key 1`() {
        val listOfMessages = listOf(
            "а", "б", "ю", "я",
            "ая", "бю", "юа", "яб",
            "абя", "бюя", "клм", "яъь"
        )
        val listOfExpected = listOf(
            "б", "в", "я", "а",
            "ба", "вя", "яб", "ав",
            "бва", "вяа", "лмн", "аыэ"
        )
        listOfMessages.forEachIndexed { index, message ->
            val expected = EncryptionAlgorithm.Result.Success(message = listOfExpected[index])
            val actual = encryptionAlgorithm.encrypt(message, "1")
            assertEquals(expected, actual)
        }
    }
    //endregion

    //region decrypt tests
    @Test
    fun `decrypt empty message`() {
        val expected = EncryptionAlgorithm.Result.EmptyMessage(
            message = "Пустое поле. Введите текст."
        )
        val actual = encryptionAlgorithm.decrypt("", "1")
        assertEquals(expected, actual)
    }

    @Test
    fun `decrypt empty message and key`() {
        val expected = EncryptionAlgorithm.Result.EmptyMessage(
            message = "Пустое поле. Введите текст."
        )
        val actual = encryptionAlgorithm.decrypt("", "")
        assertEquals(expected, actual)
    }

    @Test
    fun `decrypt empty key or zero`() {
        val expected = EncryptionAlgorithm.Result.Success("абв")
        var actual = encryptionAlgorithm.decrypt("абв", "")
        assertEquals(expected, actual)
        actual = encryptionAlgorithm.decrypt("абв", "0")
        assertEquals(expected, actual)
    }

    @Test
    fun `decrypt negative key`() {
        val listOfKeys = listOf("-1", "-21")
        listOfKeys.forEach { key ->
            val expected = EncryptionAlgorithm.Result.NegativeKey(
                message = "Вы ввели ключ: $key. Ключ не может быть меньше 0."
            )
            val actual = encryptionAlgorithm.decrypt("абв", key)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `decrypt key bigger alphabet size`() {
        val listOfKeys = listOf(
            "33", "34", "35",
            "40", "50", "60",
            "100"
        )
        val listOfMessages = listOf(
            "абвя", "бвга", "вгдб",
            "жзиё", "рстп", "ъыьщ",
            "бвга"
        )

        listOfKeys.forEachIndexed { index, key ->
            val actual = encryptionAlgorithm.decrypt(listOfMessages[index], key)
            assertEquals(EncryptionAlgorithm.Result.Success("абвя"), actual)
        }
    }

    @Test
    fun `decrypt key corner cases`() {
        val actual = encryptionAlgorithm.decrypt("абв", "3")
        val expected = EncryptionAlgorithm.Result.Success("эюя")
        assertEquals(expected, actual)
    }

    @Test
    fun `decrypt other languages or digits or special signs`() {
        val listOfMessages = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "P!", "валDK", "ЖЛcД", "ды(с", " "
        )
        val listOfExpected = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "P", "D", "Ж", "(", "Пробел"
        )
        listOfMessages.forEachIndexed { index, str ->
            val expected = EncryptionAlgorithm.Result.UnknownSign(
                message = "Неизвестный символ \"${listOfExpected[index]}\"." +
                        "Используйте строчные русские буквы."
            )
            val actual = encryptionAlgorithm.decrypt(str, "1")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `decrypt lower case Ru key 1`() {
        val listOfMessages= listOf(
            "б", "в", "я", "а",
            "ба", "вя", "яб", "ав",
            "бва", "вяа", "лмн", "аыэ"
        )
        val listOfExpected = listOf(
            "а", "б", "ю", "я",
            "ая", "бю", "юа", "яб",
            "абя", "бюя", "клм", "яъь"
        )
        listOfMessages.forEachIndexed { index, message ->
            val expected = EncryptionAlgorithm.Result.Success(message = listOfExpected[index])
            val actual = encryptionAlgorithm.decrypt(message, "1")
            assertEquals(expected, actual)
        }
    }
    //endregion
}