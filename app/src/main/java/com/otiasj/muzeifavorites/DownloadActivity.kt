package com.otiasj.muzeifavorites


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.apps.muzei.api.MuzeiContract
import java.io.File
import java.io.FileNotFoundException


class DownloadActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermission()) {
            retrievePicture()
            finish()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            retrievePicture()
            finish()
        }
    }

    private fun hasPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
        )
    }

    private fun retrievePicture() {
        val artwork = MuzeiContract.Artwork.getCurrentArtwork(this)

        if (artwork != null) {
            try {
                val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name))
                val pictureFile = artwork.saveToFile(this, directory)

                // add the file to the gallery
                MediaScannerConnection.scanFile(this, arrayOf(pictureFile.toString()), null, null)

                toast(R.string.success)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                toast(R.string.unable_to_save)
            }
        } else {
            toast(R.string.no_artwork)
        }
    }
}

private const val PERMISSION_REQUEST_CODE = 8888