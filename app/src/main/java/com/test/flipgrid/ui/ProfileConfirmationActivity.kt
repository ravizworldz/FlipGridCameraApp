package com.test.flipgrid.ui

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.R
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.utils.Utils
import com.test.flipgrid.viewmodel.CreateProfileViewModel
import kotlinx.android.synthetic.main.activity_create_profile.*
import kotlinx.android.synthetic.main.activity_profile_confirmation.*
import kotlinx.android.synthetic.main.activity_profile_confirmation.titleLabel
import kotlinx.android.synthetic.main.profile_pic_layout_row.*

class ProfileConfirmationActivity : BaseActivity() {
    private lateinit var viewModel: CreateProfileViewModel

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_profile_confirmation
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        signInButton.setOnClickListener {
            launchHomeActivity()
        }
    }

    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CreateProfileViewModel(applicationContext as FlipGridApp) as T
            }
        }).get(CreateProfileViewModel::class.java)
        initEditModeObserver()
    }

    /**
     * Take user to the Home activity.
     */
    private fun launchHomeActivity() {
        val i = Intent(this@ProfileConfirmationActivity, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
    /**
     * Initialize edit mode
     * get the data from DB
     * display profile on UI.
     */
    private fun initEditModeObserver() {
        viewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            val userProfileEntity = it[0]
            setUIData(userProfileEntity)
            if(!TextUtils.isEmpty(userProfileEntity.image)) {
                if(userProfileEntity.image?.contains("file:///")!!) {
                    val uri = Uri.parse(userProfileEntity.image)
                    var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    bitmap = Utils.fixOrientation(bitmap)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                    selectAvatarText.visibility = View.GONE
                }
            }
        })
        viewModel.getProfile()
    }

    /**
     * Update profile data on UI.
     */
    private fun setUIData(userProfileEntity: UserProfileEntity) {
        titleLabel.text = String.format(getString(R.string.profile_confirmation_hello_message), userProfileEntity.fName)
        nameLabel.text = "${userProfileEntity.fName} ${userProfileEntity.lName}"
        emailLabel.text = userProfileEntity.email
        websiteLabel.text = userProfileEntity.website
        underlineWebsite()
    }

    /**
     * Underline the website label
     */
    private fun underlineWebsite() {
        websiteLabel.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }
}