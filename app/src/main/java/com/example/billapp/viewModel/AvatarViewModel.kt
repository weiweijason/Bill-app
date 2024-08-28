package com.example.billapp.viewModel

import AvatarRepository
import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvatarViewModel(application: Application) : AndroidViewModel(application) {
    // Using Application context
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val repository = AvatarRepository(FirebaseStorage.getInstance(), context)

    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl: StateFlow<String?> = _avatarUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun uploadAvatar(imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val url = repository.uploadAvatar(imageUri)
                _avatarUrl.value = url
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAvatar(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bitmap = repository.getAvatar(url)
                _avatarUrl.value = url
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPresetAvatar(resourceId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val url = repository.uploadPresetAvatar(resourceId)
                _avatarUrl.value = url
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
