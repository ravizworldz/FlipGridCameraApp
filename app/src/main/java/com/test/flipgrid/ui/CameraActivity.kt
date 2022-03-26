package com.test.flipgrid.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.R
import com.test.flipgrid.viewmodel.CameraViewModel
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.*
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.test.flipgrid.BuildConfig


class CameraActivity: BaseActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var imageUri: Uri? = null
    private lateinit var viewModel: CameraViewModel

    companion object {
           const val PHOTO_URI = "com.test.flipgrid.ui.photo_uri"
           const val CAMERA_PERMISSION_CODE = 100
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_camera
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        isCameraPermissionAllowed()
        handleButtonClicked()
        outputDirectory = getOutputDirectory()
    }

    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CameraViewModel(applicationContext as FlipGridApp) as T
            }
        }).get(CameraViewModel::class.java)
    }

    /**
     * Handle all the buttons click on the screen
     * Capture button
     * switch camera button
     * captured image selection
     */
    private fun handleButtonClicked() {
        camera_capture_button.setOnClickListener {
            takePhoto()
        }
        iv_flipCamera.setOnClickListener {
            flipCamera()
        }
        iv_capture.setOnClickListener {
            sendSuccessResult()
        }
    }

    /**
     * Check if camera permission are allowed
     */
    private fun isCameraPermissionAllowed() {
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    /**
     * Check is camera permission are allowed or not
     * if not then show permission dialog
     * if yes, then start camera.
     */
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@CameraActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@CameraActivity, arrayOf(permission), requestCode)
        } else {
            startCamera()
        }
    }

    /**
     * Hanlde the flip camera button and flip the camera.
     */
    private fun flipCamera() {
        viewModel.flipCamera()
        // restart the camera
        startCamera()
    }

    /**
     * Handle the capture photo and capture the photo.
     */
    private fun takePhoto() {
        // Get a stable reference of the
        // modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time-stamped output file to hold the image
        val photoFile = File(outputDirectory, viewModel.getFileName())
        imageCaptureListener( photoFile)
    }

    /**
     * When image is captured, get the image URI.
     */
    private fun imageCaptureListener(photoFile: File) {
        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    // set the saved uri to the image view
                    iv_capture.visibility = View.VISIBLE
                    iv_capture.setImageURI(savedUri)
                    imageUri = savedUri
                }
            })
    }

    /**
     * Start the camera preview.
     */
    private fun startCamera() {
        viewModel.getCameraProvider().addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider =  viewModel.getCameraProvider().get()
            // Preview
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            // Select back camera as a default
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, viewModel.getCameraSelector(), preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("==", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // creates a folder inside internal storage
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    /**
     * Launch the native settings when permissions are denied.
     */
    private var launchCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        isCameraPermissionAllowed()
    }

    /**
     * Send result back to the previou activity with captured image URI
     */
    private fun sendSuccessResult() {
        val  intent  = Intent()
        intent.putExtra(PHOTO_URI, imageUri.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                cameraPermissionNeeded()
            }
        }
    }

    /**
     * Show permission dialog when camera permission is denied.
     */
    private fun cameraPermissionNeeded() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.profile_pic_camera_permission_dialog_button))
        builder.setMessage(getString(R.string.camera_permission_needed))
        builder.setPositiveButton(getString(R.string.profile_pic_camera_settings_dialog_button)) { dialog, which ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
            launchCameraActivity.launch(intent)
        }
        builder.setNegativeButton(getString(R.string.common_string_cancel)) { dialog, which ->
            dialog.dismiss()
            setResult(RESULT_CANCELED)
            finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}