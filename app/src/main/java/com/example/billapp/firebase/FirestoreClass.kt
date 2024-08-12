package com.example.billapp.firebase

import android.util.Log
import com.example.billapp.activity.SignInActivity
import com.example.billapp.activity.SignUpActivity
import com.example.billapp.models.User
import com.example.billapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo : User){
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
                activity.hideProgressDialog()
                activity.showErrorSnackBar(e.message.toString())
            }
    }

    fun signInUser(activity: SignInActivity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(
                    activity.javaClass.simpleName, document.toString()
                )
                val loggedInUser = document.toObject(User::class.java)!!

                activity.signInSuccess(loggedInUser)

            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )

                activity.hideProgressDialog()
                activity.showErrorSnackBar(e.message.toString())

                }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}