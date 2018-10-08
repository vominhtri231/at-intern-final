package internship.asiantech.a2018summerfinal.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(uploadImage: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    uploadImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

fun getBitmapFromImageView(imageView: ImageView): Bitmap {
    return (imageView.drawable as BitmapDrawable).bitmap
}
