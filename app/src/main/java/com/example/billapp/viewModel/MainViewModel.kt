package com.example.billapp.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billapp.models.User
import com.example.billapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)
                    _user.value = loggedInUser
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "loadUserData",
                        "Error while getting loggedIn user details",
                        e
                    )
                }
        }
    }

    private fun updateUserProfile(updatedUser: User) {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection(Constants.USERS)
                .document(getCurrentUserID())
                .set(updatedUser)
                .addOnSuccessListener {
                    _user.value = updatedUser
                }
                .addOnFailureListener { e ->
                    Log.e("updateUserProfile", "Error updating user profile", e)
                }
        }
    }

    private fun uploadImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    onSuccess(downloadUri.toString())
                } else {
                    onFailure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun updateUserProfileWithImage(updatedUser: User, imageUri: Uri?) {
        viewModelScope.launch {
            if (imageUri != null) {
                uploadImage(
                    imageUri,
                    onSuccess = { imageUrl ->
                        val userWithImage = updatedUser.copy(image = imageUrl)
                        updateUserProfile(userWithImage)
                    },
                    onFailure = { e ->
                        Log.e("updateUserProfileWithImage", "Error uploading image", e)
                    }
                )
            } else {
                updateUserProfile(updatedUser)
            }
        }
    }

    private fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }
}