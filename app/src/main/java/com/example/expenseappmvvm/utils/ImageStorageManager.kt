package com.example.expenseappmvvm.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object ImageStorageManager {

    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): String {
        try {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, fos)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return context.filesDir.absolutePath
    }

    fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
        val file = File(context.filesDir, imageFileName)
        var bitmapImg: Bitmap? = null
        try {
            bitmapImg = BitmapFactory.decodeStream(FileInputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapImg
    }

    fun deleteImageFromInternalStorage(context: Context, imageFileName: String): Boolean {
        return try {
            File(context.filesDir, imageFileName).delete()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}