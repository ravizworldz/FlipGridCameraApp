package com.test.flipgrid.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.util.Base64.DEFAULT
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri

class Utils {
    companion object {
        fun getEncodedString(bmp: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        fun getBitmapFromEncodedStr(encodedImageString: String?): Bitmap? {
            val bytarray: ByteArray = Base64.decode(encodedImageString, DEFAULT)
            val bmimage = BitmapFactory.decodeByteArray(
                bytarray, 0,
                bytarray.size
            )
            return bmimage
        }

        fun openUrl(url: String?, context: Context) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            context.startActivity(openURL)
        }

        fun fixOrientation(mBitmap: Bitmap): Bitmap? {
            var bitmap: Bitmap? = null
            if (mBitmap.getWidth() > mBitmap.getHeight()) {
                val matrix = Matrix()
                matrix.postRotate(90f)
                bitmap = Bitmap.createBitmap(
                    mBitmap,
                    0,
                    0,
                    mBitmap.getWidth(),
                    mBitmap.getHeight(),
                    matrix,
                    true
                )
            }
            return bitmap
        }
    }
}