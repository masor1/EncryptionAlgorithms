package com.masorone.encryptionalgorithms

interface VerifiedData<INPUT, KEY, OUTPUT : EncryptionAlgorithm.Result<INPUT>> {

    fun verify(message: INPUT, key: KEY, alphabet: INPUT): OUTPUT

    class StringMessageAndKey(
        private val resources: ProvideResources
    ) : VerifiedData<String, String, EncryptionAlgorithm.Result<String>> {

        override fun verify(
            message: String,
            key: String,
            alphabet: String
        ): EncryptionAlgorithm.Result<String> {
            if ((key.isEmpty() || key == "0") && message.isNotEmpty())
                return EncryptionAlgorithm.Result.Success(message)
            if (message.isEmpty())
                return EncryptionAlgorithm.Result.EmptyMessage(resources.string(R.string.error_empty_message))
            message.forEach { ch ->
                if (!alphabet.contains(ch))
                    return EncryptionAlgorithm.Result.UnknownSign(
                        resources.string(
                            R.string.error_unknown_sign,
                            ch.toString()
                        )
                    )
            }
            val intKey = key.toInt()
            if (intKey < 0)
                return EncryptionAlgorithm.Result.NegativeKey(
                    resources.string(
                        R.string.error_negative_key,
                        key
                    )
                )
            return EncryptionAlgorithm.Result.Verified()
        }
    }
}