package com.masorone.encryptionalgorithms

interface EncryptionAlgorithm<I, O, K> {

    fun encrypt(data: I, key: K): Result

    fun decrypt(data: O, key: K): Result

    sealed class Result {

        object EmptyData : Result()

        data class UnknownSign(private val sign: String) : Result()

        data class Success(private val data: String) : Result()
    }

    interface Caesar : EncryptionAlgorithm<String, String, Int> {

        class Ru(
            private val alphabet: String =
                "АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя"
        ) : Caesar {

            override fun encrypt(data: String, key: Int): Result {
                if (data.isEmpty())
                    return Result.EmptyData

                var result = ""
                data.forEach { dataStr ->
                    if (!alphabet.contains(dataStr))
                        return if (" " == dataStr.toString())
                            Result.UnknownSign("_")
                        else
                            Result.UnknownSign(dataStr.toString())
                    alphabet.forEachIndexed { index, alphStr ->
                        val i = index + (key * 2)
                        if (dataStr == alphStr) {
                            result += if (i < alphabet.length)
                                alphabet[i]
                            else
                                alphabet[i - (alphabet.length + key - 1)]
                        }
                    }
                }
                return Result.Success(result)
            }

            override fun decrypt(data: String, key: Int): Result {
                if (data.isEmpty())
                    return Result.EmptyData

                var result = ""
                data.forEach { dataStr ->
                    if (!alphabet.contains(dataStr))
                        return if (" " == dataStr.toString())
                            Result.UnknownSign("_")
                        else
                            Result.UnknownSign(dataStr.toString())
                    alphabet.forEachIndexed { index, alphStr ->
                        val i = index - (key * 2)
                        if (dataStr == alphStr) {
                            result += if (i > -1)
                                alphabet[i]
                            else
                                alphabet[i + (alphabet.length + key - 1)]
                        }
                    }
                }
                return Result.Success(result)
            }
        }
    }
}