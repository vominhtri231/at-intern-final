package internship.asiantech.a2018summerfinal.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.utils.bitmapToByteArray
import java.io.ByteArrayOutputStream
import java.util.*

object FirebaseStorageUtils {
    private const val URL_IMAGE = "gs://asiantech-intern-final.appspot.com"
    private val storage = FirebaseStorage.getInstance()

    fun uploadImage(uploadImage: Bitmap, storageUpdater: StorageUpdater){
        val storageRef = storage.getReferenceFromUrl(URL_IMAGE.trim())
        val mountainsRef = storageRef.child("${Calendar.getInstance().timeInMillis}.png")
        val data = bitmapToByteArray(uploadImage)
        val uploadTask = mountainsRef.putBytes(data)

        uploadTask.addOnFailureListener {
            storageUpdater.onFail()
        }.addOnSuccessListener { uri ->
            uri?.uploadSessionUri?.let {
                storageUpdater.onSuccess(it)
            }
        }
    }
}
