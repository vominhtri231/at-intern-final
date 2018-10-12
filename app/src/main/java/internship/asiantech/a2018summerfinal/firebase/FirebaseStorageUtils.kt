package internship.asiantech.a2018summerfinal.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import internship.asiantech.a2018summerfinal.R
import internship.asiantech.a2018summerfinal.utils.bitmapToByteArray
import java.io.ByteArrayOutputStream
import java.util.*

object FirebaseStorageUtils {
    private val TAG = FirebaseStorageUtils::class.qualifiedName
    private const val URL_IMAGE = "gs://asiantech-intern-final.appspot.com"
    private val storage = FirebaseStorage.getInstance()

    fun uploadImage(uploadImage: Bitmap, storageUpdater: StorageUpdater) {
        val storageRef = storage.getReferenceFromUrl(URL_IMAGE.trim())
        val pathString = "${Calendar.getInstance().timeInMillis}.png"
        val mountainsRef = storageRef.child(pathString)
        val data = bitmapToByteArray(uploadImage)
        val uploadTask = mountainsRef.putBytes(data)

        uploadTask.addOnFailureListener {
            storageUpdater.onFail()
        }.addOnSuccessListener {
            storageUpdater.onSuccess(pathString.trim())
        }
    }

    fun getDownloadUrl(pathString: String, updater: GetDownloadUpdater) {
        val storageRef = storage.getReferenceFromUrl(URL_IMAGE.trim())
        val mountainsRef = storageRef.child(pathString)
        mountainsRef.downloadUrl.addOnSuccessListener {
            Log.e(TAG, it.toString())
            updater.handle(it.path)
        }.addOnFailureListener {
            Log.e(TAG, it.toString())
        }
    }
}
