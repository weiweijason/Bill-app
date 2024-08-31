import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

open class AvatarRepository(
    private val storage: FirebaseStorage,
    private val context: Context
) {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadAvatar(imageUri: Uri, userId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = storage.reference.child("USER_IMAGE_${System.currentTimeMillis()}.${getFileExtension(imageUri)}")
                val uploadTask = ref.putFile(imageUri).await()
                val downloadUrl = ref.downloadUrl.await()

                updateUserImage(userId, downloadUrl.toString())

                Log.i("Downloadable Image URL", downloadUrl.toString())
                downloadUrl.toString()
            } catch (e: Exception) {
                Log.e("Firebase Image URL", e.message, e)
                null
            }
        }
    }

    suspend fun uploadGroupAvatar(imageUri: Uri, groupId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = storage.reference.child("Group_IMAGE_${System.currentTimeMillis()}.${getFileExtension(imageUri)}")
                val uploadTask = ref.putFile(imageUri).await()
                val downloadUrl = ref.downloadUrl.await()

                updateGroupImage(groupId, downloadUrl.toString())

                Log.i("Downloadable Image URL", downloadUrl.toString())
                downloadUrl.toString()
            } catch (e: Exception) {
                Log.e("Firebase Image URL", e.message, e)
                null
            }
        }
    }

    suspend fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(uri!!))
    }

    suspend fun updateUserImage(userId: String, imageUrl: String) {
        try {
            firestore.collection("users").document(userId)
                .update("image", imageUrl)
                .await()
            Log.i("Firestore Update", "User image updated successfully")
        } catch (e: Exception) {
            Log.e("Firestore Update", e.message, e)
        }
    }

    suspend fun updateGroupImage(groupId: String, imageUrl: String) {
        try {
            firestore.collection("groups").document(groupId)
                .update("image", imageUrl)
                .await()
            Log.i("Firestore Update", "Group image updated successfully")
        } catch (e: Exception) {
            Log.e("Firestore Update", e.message, e)
        }
    }

    suspend fun getUserAvatarUrl(userId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val document = firestore.collection("users").document(userId).get().await()
                document.getString("image")
            } catch (e: Exception) {
                Log.e("Firestore Get", "Error getting user avatar URL", e)
                null
            }
        }
    }

    suspend fun getGroupAvatarUrl(groupId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val document = firestore.collection("groups").document(groupId).get().await()
                document.getString("image")
            } catch (e: Exception) {
                Log.e("Firestore Get", "Error getting group avatar URL", e)
                null
            }
        }
    }

}