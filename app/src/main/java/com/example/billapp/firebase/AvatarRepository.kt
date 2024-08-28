import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
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

    suspend fun uploadAvatar(imageUri: Uri): String {
        return withContext(Dispatchers.IO) {
            val ref = storage.reference.child("avatars/${UUID.randomUUID()}")
            ref.putFile(imageUri).await()
            ref.downloadUrl.await().toString()
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

    suspend fun uploadPresetAvatar(resourceId: Int): String {
        return withContext(Dispatchers.IO) {
            val drawable = ContextCompat.getDrawable(context, resourceId)
            val bitmap = (drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val ref = storage.reference.child("avatars/${UUID.randomUUID()}.jpg")
            ref.putBytes(data).await()
            ref.downloadUrl.await().toString()
        }
    }

    fun getPresetAvatarDrawableId(presetName: String): Int {
        return context.resources.getIdentifier(presetName, "drawable", context.packageName)
    }
}