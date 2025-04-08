package com.shodo.android.posttransaction.step1

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class PostTransactionStep1ViewModel() : ViewModel() {

    private val _error = MutableSharedFlow<Exception>()
    val error = _error.asSharedFlow()

    fun createImageFile(context: Context): File? {
        return try {
            val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())
            val storageDir = context.filesDir
            File.createTempFile("${FILE_PREFIX}_${timeStamp}", FILE_FORMAT, storageDir)
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e) }
            null
        }
    }

    fun getUriForFile(context: Context, file: File?): Uri? {
       return try {
           file?.let {
               FileProvider.getUriForFile(
                   context,
                   context.packageName + FILE_PROVIDER_SUFFIX,
                   file
               )
           }
       } catch (e: Exception) {
           viewModelScope.launch { _error.emit(e) }
           null
       }
    }

    companion object {
        private const val FILE_PROVIDER_SUFFIX = ".fileprovider"
        private const val FILE_PREFIX = "PokeManiac_"
        private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        private const val FILE_FORMAT = ".jpg"
    }
}