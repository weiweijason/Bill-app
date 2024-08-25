package com.example.billapp.firebase

import android.util.Log
import com.example.billapp.models.Group
import com.example.billapp.models.PersonalTransaction
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
                Log.e("CreateGroup", "Group created successfully with ID: $groupId")
            }
            .addOnFailureListener { e ->
                Log.e(
                    "CreateGroup",
                    "Error while creating a group.",
                    e
                )
            }
    }

    suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
        val currentUser = getAuthInstance().currentUser ?: throw IllegalStateException("No user logged in")
        getFirestoreInstance().collection("users").document(currentUser.uid).get().await().toObject(User::class.java)
            ?: throw IllegalStateException("User data not found")
    }

    suspend fun getUserTransactions(userId: String): List<PersonalTransaction> {
        val transactions = mutableListOf<PersonalTransaction>()
        val snapshot = FirebaseFirestore.getInstance()
            .collection(Constants.USERS)
            .document(userId)
            .collection(Constants.TRANSACTIONS)
            .get()
            .await()

        for (document in snapshot.documents) {
            val transaction = document.toObject(PersonalTransaction::class.java)
            if (transaction != null) {
                transactions.add(transaction)
            }
        }
        return transactions
    }

    suspend fun getUserGroups(): List<Group> = withContext(Dispatchers.IO) {
        val currentUser = getAuthInstance().currentUser ?: throw IllegalStateException("No user logged in")
        val userId = currentUser.uid

        // Query for groups where the user is in assignedTo
        val assignedGroups = getFirestoreInstance()
            .collection(Constants.GROUPS)
            .whereArrayContains("assignedTo", userId)
            .get()
            .await()
            .toObjects(Group::class.java)

        // Query for groups where the user is the creator
        val createdGroups = getFirestoreInstance()
            .collection(Constants.GROUPS)
            .whereEqualTo("createdBy", userId)
            .get()
            .await()
            .toObjects(Group::class.java)

        // Combine both lists, removing any duplicates if necessary
        val allGroups = (assignedGroups + createdGroups).distinctBy { it.id }

        return@withContext allGroups
    }

    suspend fun deleteGroup(groupId: String) = withContext(Dispatchers.IO) {
        getFirestoreInstance().collection("groups").document(groupId).delete().await()
    }

    suspend fun updateGroup(groupId: String, group: Group) = withContext(Dispatchers.IO) {
        getFirestoreInstance().collection("groups").document(groupId).set(group).await()
    }

    suspend fun assignUserToGroup(groupId: String, userId: String) = withContext(Dispatchers.IO) {
        val groupRef = getFirestoreInstance().collection("groups").document(groupId)
        val group = groupRef.get().await().toObject(Group::class.java)
        group?.assignedTo?.add(userId)
        groupRef.set(group!!).await()
    }

    suspend fun getGroup(groupId: String): Group = withContext(Dispatchers.IO) {
        return@withContext getFirestoreInstance()
            .collection("groups")
            .document(groupId)
            .get()
            .await()
            .toObject(Group::class.java) ?: throw IllegalStateException("Group not found")
    }


}