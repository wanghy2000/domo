package com.sywang.model

import android.net.Uri
import java.io.Serializable
import java.util.*

data class MediaFileData (
    val id: Long,
    val dateTaken: Date,
    val displayName: String,
    val uri: Uri
    )