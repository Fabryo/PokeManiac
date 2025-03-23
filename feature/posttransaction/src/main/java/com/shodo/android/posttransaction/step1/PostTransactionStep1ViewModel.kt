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
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir = context.filesDir
            File.createTempFile("PokeManiac_${timeStamp}_", ".jpg", storageDir)
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
                   context.packageName + ".fileprovider",
                   file
               )
           }
       } catch (e: Exception) {
           viewModelScope.launch { _error.emit(e) }
           null
       }
    }
}