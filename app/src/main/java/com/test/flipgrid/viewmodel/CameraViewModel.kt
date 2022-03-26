package com.test.flipgrid.viewmodel

import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.ViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.test.flipgrid.FlipGridApp
import java.text.SimpleDateFormat
import java.util.*

class CameraViewModel(appContext: FlipGridApp): ViewModel() {
    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    init {
        appContext.getAppComponent().inject(this)
        cameraProviderFuture = ProcessCameraProvider.getInstance(appContext)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    fun getCameraProvider():ListenableFuture<ProcessCameraProvider> {
        return cameraProviderFuture
    }

    fun getCameraSelector(): CameraSelector {
        return cameraSelector
    }

    fun getFileName(): String {
        return SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    }

    fun flipCamera() {
        cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
            CameraSelector.DEFAULT_FRONT_CAMERA
        }else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }
}