package com.test.flipgrid.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.R
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.ui.CameraActivity.Companion.PHOTO_URI
import com.test.flipgrid.ui.HomeActivity.Companion.FROM
import com.test.flipgrid.utils.DataCallback
import com.test.flipgrid.utils.Utils
import com.test.flipgrid.viewmodel.CreateProfileViewModel
import kotlinx.android.synthetic.main.activity_create_profile.*
import kotlinx.android.synthetic.main.profile_pic_layout_row.*
import java.io.IOException

class CreateProfileActivity: BaseActivity() {
    private lateinit var viewModel: CreateProfileViewModel
    private var imageData: String? = null
    private var profileId = 0
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_create_profile
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        val forEdit = intent.getBooleanExtra(FROM, false)
        handleSaveButtonClick()
        imageView.setOnClickListener {
            chooseImagePickerClicked()
        }

        if(forEdit) {
            initEditModeObserver()
        }
    }
    /**
     * Initialize view model
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreateProfileViewModel(applicationContext as FlipGridApp) as T
            }
        }).get(CreateProfileViewModel::class.java)
        initObservers()
    }

    /**
     * Register the observer to observe results from viewmodel
     */
    private fun initObservers() {
        viewModel.getCreateProfileObserver().observe(this, Observer <DataCallback<Boolean>>{
            hideProgress()
            when(it?.status) {
                DataCallback.Status.SUCCESS -> {
                    if (profileId == 0) {
                        val i = Intent(this@CreateProfileActivity, ProfileConfirmationActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
                else -> {
                    showError(
                        getString(R.string.common_error_title),
                        getString(R.string.common_error_message),
                        getString(R.string.common_string_ok)
                    )
                }
            }
        })
    }

    /**
     * Initialize edit mode
     * get the data from DB
     * display profile on UI.
     */
    private fun initEditModeObserver() {
        viewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            val userProfileEntity = it[0]
            profileId = userProfileEntity.id
            setUIData(userProfileEntity)
            updateButtonText()
            updateScreenTitle()
            if(!TextUtils.isEmpty(userProfileEntity.image)) {
                imageData = userProfileEntity.image
                setAvatarToImageView()
            }
        })
        viewModel.getProfile()
    }

    /**
     * Set the captured avatar to image view.
     */
    private fun setAvatarToImageView() {
        val uri = Uri.parse(imageData)
        var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        bitmap = Utils.fixOrientation(bitmap)
        findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
        selectAvatarText.visibility = GONE
    }
    /**
     * Update screen title for Edit
     */
    private fun updateScreenTitle() {
        titleLabel.text = getString(R.string.profile_edit_profile_label)
    }

    /**
     * Update save button to update when coming for edit.
     */
    private fun updateButtonText() {
        saveButton.text = getString(R.string.common_string_update)
    }

    /**
     * Update profile data on UI.
     */
    private fun setUIData(userProfileEntity: UserProfileEntity) {
        inputFirstName.setText(userProfileEntity.fName.toString())
        inputLastName.setText(userProfileEntity.lName)
        inputEmail.setText(userProfileEntity.email)
        inputPassword.setText(userProfileEntity.password)
        inputWebsite.setText(userProfileEntity.website)
    }

    /**
     * Handle save button click.
     */
    private fun handleSaveButtonClick() {
        saveButton.setOnClickListener {
            validateInputFields()
        }
    }

    /**
     * Create profile in DB
     */
    private fun createProfile(userProfileEntity: UserProfileEntity) {
        showProgress()
        viewModel.createProfile(userProfileEntity)
    }

    /**
     * Get input data and validate
     * after validation save it in DB to create profile.
     */
    private fun validateInputFields() {
        val inputFirstName =  inputFirstName.text.toString()
        val inputLastName =  inputLastName.text.toString()
        val inputEmail =  inputEmail.text.toString()
        val inputPassword =  inputPassword.text.toString()
        val inputWebsite =  inputWebsite.text.toString()

        hideAllValidationErrors()
        if(TextUtils.isEmpty(inputEmail)) {
            errorEmail.visibility= View.VISIBLE
            return
        } else if(TextUtils.isEmpty(inputPassword)) {
            errorPassword.visibility= View.VISIBLE
            return
        }
        val userProfile = UserProfileEntity(id=profileId, inputFirstName, inputLastName, inputEmail,
            inputPassword, inputWebsite, imageData)
        createProfile(userProfile)
    }

    /**
     * Hide the errors visible currently.
     */
    private fun hideAllValidationErrors() {
        errorEmail.visibility= View.GONE
        errorPassword.visibility= View.GONE
    }

    /**
     * Show Dialog to open phone gallery.
     */
   private fun chooseImagePickerClicked() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.profile_pic_dialog_title))
        builder.setMessage(getString(R.string.profile_pic_dialog_message))
        builder.setPositiveButton(getString(R.string.profile_pic_camera_dialog_button)) { dialog, which ->
            launchCamera()
        }
        builder.setNegativeButton(getString(R.string.common_string_cancel)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun launchCamera() {
        val intent = Intent(this@CreateProfileActivity, CameraActivity::class.java)
        launchCameraActivity.launch(intent)
    }

    var launchCameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                imageData = result.data?.getStringExtra(PHOTO_URI)
                setAvatarToImageView()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showProgress() {
        progressBar_cyclic.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar_cyclic.visibility = View.GONE
    }
}