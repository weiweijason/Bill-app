package com.example.billapp

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageViewModel : ViewModel() {
    var selectedImageUri = mutableStateOf<Uri?>(null)
    var selectedImageBitmap = mutableStateOf<ImageBitmap?>(null)

    fun setImageUri(uri: Uri, context: android.content.Context) {
        selectedImageUri.value = uri
        val imageLoader = ImageLoader(context)
        viewModelScope.launch {
            val bitmap = loadImageBitmap(uri, imageLoader, context)
            selectedImageBitmap.value = bitmap
        }
    }

    private suspend fun loadImageBitmap(uri: Uri, imageLoader: ImageLoader, context: android.content.Context): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(uri)
                .build()

            val result = (imageLoader.execute(request) as? SuccessResult)?.drawable
            result?.toBitmap()?.asImageBitmap()
        }
    }

}