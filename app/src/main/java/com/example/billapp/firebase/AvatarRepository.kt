import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

open class AvatarRepository(
    private val storage: FirebaseStorage,
    private val context: Context
) {
    private val imageLoader = ImageLoader(context)
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun uploadAvatar(imageUri: Uri, userId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ref = storage.reference.child("USER_IMAGE_${System.currentTimeMillis()}.${getFileExtension(imageUri)}")
                val uploadTask = ref.putFile(imageUri).await()
                val downloadUrl = ref.downloadUrl.await()

                // Update the user's image URL in Firestore
                updateUserImage(userId, downloadUrl.toString())

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

    // 更新 Firestore 中 User 的 image 字段
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

    suspend fun getAvatar(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Disable hardware bitmaps for easy conversion
                .build()

            val result = imageLoader.execute(request)
            if (result is SuccessResult) {
                (result.drawable as BitmapDrawable).bitmap
            } else {
                null
            }
        }
    }

    fun getPresetAvatarDrawableId(presetName: String): Int {
        return context.resources.getIdentifier(presetName, "drawable", context.packageName)
    }
}
