package com.masorone.encryptionalgorithms

interface EncryptionAlgorithm<I, O, K> {

    fun encrypt(data: I, key: K): Result<O>

    fun decrypt(data: O, key: K): Result<O>

    sealed class Result<O> : Map<O> {

        data class EmptyMessage(private val message: String) : Result<String>() {

            override fun map(): String {
                return message
            }
        }

        data class UnknownSign(private val message: String) : Result<String>() {

            override fun map(): String {
                return message
            }
        }

        data class NegativeKey(private val message: String) : Result<String>() {

            override fun map(): String {
                return message
            }
        }

        data class Success(private val data: String) : Result<String>() {

            override fun map(): String {
                return this.data
            }
        }
    }

    interface Caesar : EncryptionAlgorithm<String, String, String> {

        class RuLowerCase(
            private val provideResources: ProvideResources,
            private val alphabet: String = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        ) : Caesar {

            override fun encrypt(data: String, key: String): Result<String> {
                if ((key.isEmpty() || key == "0") && data.isNotEmpty())
                    return Result.Success(data)
                if (data.isEmpty())
                    return Result.EmptyMessage(provideResources.string(R.string.error_empty_message))
                data.forEach { ch ->
                    if (!alphabet.contains(ch))
                        return Result.UnknownSign(
                            provideResources.string(
                                R.string.error_unknown_sign,
                                ch.toString()
                            )
                        )
                }
                var intKey = key.toInt()
                if (intKey < 0)
                    return Result.NegativeKey(
                        provideResources.string(
                            R.string.error_negative_key,
                            key
                        )
                    )
                if (intKey > alphabet.length - 1)
                    intKey %= alphabet.length
                var result = ""
                data.forEach{ chD ->
                    alphabet.forEachIndexed { index, chA ->
                        if (chA == chD) {
                        result += if (index + intKey > alphabet.length - 1)
                            alphabet[intKey + index - alphabet.length]
                        else
                            alphabet[index + intKey]
                        }
                    }
                }
                return Result.Success(result)
            }

            override fun decrypt(data: String, key: String): Result<String> {
                if ((key.isEmpty() || key == "0") && data.isNotEmpty())
                    return Result.Success(data)
                if (data.isEmpty())
                    return Result.EmptyMessage(provideResources.string(R.string.error_empty_message))
                data.forEach { ch ->
                    if (!alphabet.contains(ch))
                        return Result.UnknownSign(
                            provideResources.string(
                                R.string.error_unknown_sign,
                                ch.toString()
                            )
                        )
                }
                var intKey = key.toInt()
                if (intKey < 0)
                    return Result.NegativeKey(
                        provideResources.string(
                            R.string.error_negative_key,
                            key
                        )
                    )
                if (intKey > alphabet.length - 1)
                    intKey %= alphabet.length
                var result = ""
                data.forEach{ chD ->
                    alphabet.forEachIndexed { index, chA ->
                        if (chA == chD)
                            result += if (index - intKey < 0)
                                alphabet[alphabet.length - intKey + index]
                            else
                                alphabet[index - intKey]
                    }
                }
                return Result.Success(result)
            }
        }
    }
}

interface Map<O> {

    fun map(): O
}