package com.masorone.encryptionalgorithms

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    private val message: String,
    private val key: String
): Parcelable
