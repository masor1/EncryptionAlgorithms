package com.masorone.encryptionalgorithms

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EncryptionAlgorithmTest {

    private lateinit var encryptionAlgorithm: EncryptionAlgorithm.Caesar.Ru

    @Before
    fun setUp() {
        encryptionAlgorithm = EncryptionAlgorithm.Caesar.Ru()
    }

    //region encrypt tests
    @Test
    fun `encrypt empty string`() {
        val expected = EncryptionAlgorithm.Result.EmptyData
        val actual = encryptionAlgorithm.encrypt("", 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `other languages or digits or signs`() {
        val listOfMessages = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "Р!", "валDK", "ЖЛcД", "ды(с", " "
        )
        val listOfExpected = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "!", "D", "c", "(", "_"
        )
        listOfMessages.forEachIndexed { index, str ->
            val actual = encryptionAlgorithm.encrypt(str, 1)
            val expected = EncryptionAlgorithm.Result.UnknownSign(listOfExpected[index])
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `encrypt lower case Ru`() {
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
            val expected = EncryptionAlgorithm.Result.Success(data = listOfExpected[index])
            val actual = encryptionAlgorithm.encrypt(message, 1)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `encrypt upper case Ru`() {
        val listOfMessages = listOf(
            "А", "Б", "Ю", "Я",
            "АЯ", "БЮ", "ЮА", "ЯБ",
            "АБЯ", "БЮЯ", "КЛМ", "ЯЪЬ"
        )
        val listOfExpected = listOf(
            "Б", "В", "Я", "А",
            "БА", "ВЯ", "ЯБ", "АВ",
            "БВА", "ВЯА", "ЛМН", "АЫЭ"
        )
        listOfMessages.forEachIndexed { index, message ->
            val expected = EncryptionAlgorithm.Result.Success(data = listOfExpected[index])
            val actual = encryptionAlgorithm.encrypt(message, 1)
            assertEquals(expected, actual)
        }
    }
    //endregion

    //region decrypt tests
    @Test
    fun `decrypt empty string`() {
        val expected = EncryptionAlgorithm.Result.EmptyData
        val actual = encryptionAlgorithm.encrypt("", 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `decrypt other languages or digits or signs`() {
        val listOfMessages = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "Р!", "валDK", "ЖЛcД", "ды(с", " "
        )
        val listOfExpected = listOf(
            "0", "9", "!", "a",
            "S", ")", "l", "/",
            "!", "D", "c", "(", "_"
        )
        listOfMessages.forEachIndexed { index, str ->
            val actual = encryptionAlgorithm.decrypt(str, 1)
            val expected = EncryptionAlgorithm.Result.UnknownSign(listOfExpected[index])
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `decrypt lower case Ru`() {
        val listOfExpected = listOf(
            "а", "б", "ю", "я",
            "ая", "бю", "юа", "яб",
            "абя", "бюя", "клм", "яъь"
        )
        val listOfMessages = listOf(
            "б", "в", "я", "а",
            "ба", "вя", "яб", "ав",
            "бва", "вяа", "лмн", "аыэ"
        )
        listOfMessages.forEachIndexed { index, message ->
            val expected = EncryptionAlgorithm.Result.Success(data = listOfExpected[index])
            val actual = encryptionAlgorithm.decrypt(message, 1)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `decrypt upper case Ru`() {
        val listOfExpected = listOf(
            "А", "Б", "Ю", "Я",
            "АЯ", "БЮ", "ЮА", "ЯБ",
            "АБЯ", "БЮЯ", "КЛМ", "ЯЪЬ"
        )
        val listOfMessages = listOf(
            "Б", "В", "Я", "А",
            "БА", "ВЯ", "ЯБ", "АВ",
            "БВА", "ВЯА", "ЛМН", "АЫЭ"
        )
        listOfMessages.forEachIndexed { index, message ->
            val expected = EncryptionAlgorithm.Result.Success(data = listOfExpected[index])
            val actual = encryptionAlgorithm.decrypt(message, 1)
            assertEquals(expected, actual)
        }
    }
    //endregion
}