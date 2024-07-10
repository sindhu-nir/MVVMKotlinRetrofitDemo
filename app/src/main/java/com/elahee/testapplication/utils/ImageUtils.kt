package com.btracsolutions.yesparking.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

object ImageUtils {

    internal fun compressImage(
        source: Uri,
        maxSize: Int,
        quality: Int,
        fileNameSuffix: String,
        context: Context
    ): File? {
        // Create bitmap from uri.
        val sourceAsBitmap: Bitmap = getBitmapFromUri(source,context) ?: return null

        // The width and height of the original image.
        val sourceWidth: Float = sourceAsBitmap.width.toFloat()
        val sourceHeight: Float = sourceAsBitmap.height.toFloat()

        // #######################################################################################

        // The height and width of the image after scale must not exceed 4000 pixels.
        // So here, we determine if the current height and width exceed 4000, we choose the 4000,
        // otherwise, we keep the current dimensions.
        // After determining the max between the height and width, we calculate their proportion
        // to correctly scale the image

        val minWidth: Float = min(sourceWidth, maxSize.toFloat()) / sourceWidth
        val minHeight: Float = min(sourceHeight, maxSize.toFloat()) / sourceHeight

        val percentage: Float = min(minWidth, minHeight)

        // The chosen width and height that will be used to scale the image.
        val newWidth: Int = (sourceWidth * percentage).toInt()
        val newHeight: Int = (sourceHeight * percentage).toInt()

        // #######################################################################################

        // Scale image to chosen dimensions.
        val scaledBitmap: Bitmap =
            Bitmap.createScaledBitmap(sourceAsBitmap, newWidth, newHeight, true)

        // Compress the image to the chosen quality and format.
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        // Create file for the scaled & compressed image.
        val compressedFile: File =
            createFile(outputStream.toByteArray(), source, fileNameSuffix,context) ?: return null

        // copyExifData(source, compressedFile, newWidth, newHeight)

        outputStream.close()

        return compressedFile
    }


    private fun createFile(byteArray: ByteArray, source: Uri, nameSuffix: String,context: Context): File? {
        var file: File? = null
        try {
            val sourceAsDocumentFile: DocumentFile? = DocumentFile.fromSingleUri(context, source)

            val sourceFileName: String =
                sourceAsDocumentFile?.name?.substringBefore(".") ?: System.currentTimeMillis()
                    .toString()

            val finalFileName = "" + sourceFileName + "_" + nameSuffix + ".jpg"

            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            // val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            val storageDir: File? = context.externalCacheDir
            val imageFileName = "JPEG_${timeStamp}_"

            file = File.createTempFile(
                imageFileName, /* prefix */
                ".png", /* suffix */
                storageDir /* directory */
            )

            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(byteArray)
            fileOutputStream.close()

            return file
        } catch (e: Exception) {
            println(e.message)
        }
        return file
    }

    private fun getBitmapFromUri(uri: Uri,context: Context): Bitmap? {
        return try {
            val parcelFileDescriptor: ParcelFileDescriptor =
                context.contentResolver?.openFileDescriptor(uri, "r") ?: return null
            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
            val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            image
        } catch (exception: Exception) {
            println("error decoding image using bitmap factory" + exception.message)
            null
        }
    }


}