package ru.netology.nmedia.model

import android.net.Uri
import java.io.File

/**МОДЕЛЬ ДЛЯ ФОТО*/

data class PhotoModel(val uri: Uri? = null, val file: File? = null)