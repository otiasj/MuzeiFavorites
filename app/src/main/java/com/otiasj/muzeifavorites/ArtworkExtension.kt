package com.otiasj.muzeifavorites

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.MuzeiContract
import timber.log.Timber
import java.io.File
import java.util.*


fun Artwork.saveToFile(context: Context, directory: File): File? {

    Timber.v("" + this.imageUri)
    val image = BitmapFactory.decodeStream(context.contentResolver.openInputStream(MuzeiContract.Artwork.CONTENT_URI))

    if (image != null) {
        val filename = createFileName(context.resources, this.title, this.byline)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val pictureFile = File(directory, filename)
        pictureFile.outputStream().use { out ->
            image.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }

        Timber.i("Image saved to: " + pictureFile.toString())
        return pictureFile
    }
    return null
}

fun createFileName(resources: Resources, title: String?, author: String?): String {
    var filename: String

    if (title != null && author != null) {
        filename = resources.getString(R.string.picture_by_author, title, author)
    } else if (title != null) {
        filename = title.trim()
    } else if (author != null) {
        filename = author.trim()
    } else {
        filename = Date().toString()
    }

    // filter out bad characters
    filename = filename.replace("\\W+".toRegex(), "_")

    return "$filename.jpg"
}
