package com.masorone.encryptionalgorithms

interface EncryptionAlgorithm<I, O, K> {

    fun encrypt(data: I, key: K): Result<O>

    fun decrypt(data: I, key: K): Result<O>

    sealed class Result<O>(private val message: O) : Map<O> {

        override fun map() = message

        data class Verified(private val message: String = "") : Result<String>(message)

        data class EmptyMessage(private val message: String) : Result<String>(message)

        data class UnknownSign(private val message: String) : Result<String>(message)

        data class NegativeKey(private val message: String) : Result<String>(message)

        data class Success(private val message: String) : Result<String>(message)
    }

    interface Caesar : EncryptionAlgorithm<String, String, String> {

        class RuLowerCase(
            private val verifiedData: VerifiedData.StringMessageAndKey,
            private val alphabet: String = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        ) : Caesar {

            override fun encrypt(data: String, key: String): Result<String> {
                return when (val verify = verifiedData.verify(data, key, alphabet)) {
                    is Result.Verified -> {
                        var intKey = key.toInt()
                        if (intKey > alphabet.length - 1)
                            intKey %= alphabet.length
                        var result = ""
                        data.forEach { chD ->
                            alphabet.forEachIndexed { index, chA ->
                                if (chA == chD) {
                                    result += if (index + intKey > alphabet.length - 1)
                                        alphabet[intKey + index - alphabet.length]
                                    else
                                        alphabet[index + intKey]
                                }
                            }
                        }
                        Result.Success(result)
                    }
                    else -> verify
                }
            }


            override fun decrypt(data: String, key: String): Result<String> {
                return when (val verify = verifiedData.verify(data, key, alphabet)) {
                    is Result.Verified -> {
                        var intKey = key.toInt()
                        if (intKey > alphabet.length - 1)
                            intKey %= alphabet.length
                        var result = ""
                        data.forEach { chD ->
                            alphabet.forEachIndexed { index, chA ->
                                if (chA == chD)
                                    result += if (index - intKey < 0)
                                        alphabet[alphabet.length - intKey + index]
                                    else
                                        alphabet[index - intKey]
                            }
                        }
                        Result.Success(result)
                    }
                    else -> verify
                }
            }
        }
    }
}