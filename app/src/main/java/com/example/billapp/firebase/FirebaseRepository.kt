package com.example.billapp.firebase

import android.util.Log
import com.example.billapp.models.Group
import com.example.billapp.models.User
import com.example.billapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseRepository {

    private fun getFirestoreInstance() = FirebaseFirestore.getInstance()
    private fun getAuthInstance() = FirebaseAuth.getInstance()

    suspend fun createGroup(group: Group) = withContext(Dispatchers.IO) {
        val currentUser = getAuthInstance().currentUser ?: throw IllegalStateException("No user logged in")
        val groupId = getFirestoreInstance().collection(Constants.GROUPS).document().id
        val groupData = group.copy(
            createdBy = currentUser.uid,
            id = groupId  // 使用 Firestore 生成的 ID
        )
        getFirestoreInstance().collection(Constants.GROUPS)
            .document(groupId)
            .set(groupData, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("CreateBoard", "Board created successfully with ID: $groupId")
            }
            .addOnFailureListener { e ->
                Log.e(
                    "CreateBoard",
                    "Error while creating a board.",
                    e
                )
            }
    }

    suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
        val currentUser = getAuthInstance().currentUser ?: throw IllegalStateException("No user logged in")
        getFirestoreInstance().collection("users").document(currentUser.uid).get().await().toObject(User::class.java)
            ?: throw IllegalStateException("User data not found")
    }

    suspend fun getUserGroups(): List<Group> = withContext(Dispatchers.IO) {
        val currentUser = getAuthInstance().currentUser ?: throw IllegalStateException("No user logged in")
        return@withContext getFirestoreInstance()
            .collection("groups")
            .whereArrayContains("assignedTo", currentUser.uid)
            .get()
            .await()
            .toObjects(Group::class.java)
    }
}