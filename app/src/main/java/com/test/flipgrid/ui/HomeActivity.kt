package com.test.flipgrid.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.R
import com.test.flipgrid.models.CatBreedListItem
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.ui.fragment.CatBreedsDetailFragment
import com.test.flipgrid.ui.fragment.CatBreedsListFragment
import com.test.flipgrid.viewmodel.HomeActivityViewModel
import com.test.flipgrid.utils.Utils
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*


class HomeActivity : BaseActivity() {
    private var drawerLayout: DrawerLayout? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var viewModel: HomeActivityViewModel
    companion object {
        const val FROM = "com.test.flipgrid.ui.from"
    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_home
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout?.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        handleButtonClicks()
        setupDefaultFragment()
        updateAppVersion()
    }

    /**
     * Initialize view model
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeActivityViewModel(applicationContext as FlipGridApp) as T
            }
        }).get(HomeActivityViewModel::class.java)
        initObservers()
        viewModel.getProfile()
    }

    /**
     * Handle edit profile button click
     * handle nav bar icon click.
     */
    private fun handleButtonClicks() {
        sideMenuIcon.setOnClickListener {
            openNavigationalDrawer()
        }
        labelEditProfile.setOnClickListener {
            launchEditProfile()
        }
    }
    /**
     * Open create profile activity in edit mode.
     */
    private fun launchEditProfile() {
        closeNavigationDrawer()
        val i = Intent(this@HomeActivity, CreateProfileActivity::class.java)
        i.putExtra(FROM,true)
        launchCreateProfileActivity.launch(i)
    }

    /**
     * Read the app version and set it to textview
     */
    private fun updateAppVersion() {
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            val appVersion = pInfo.versionName
            appVersionTv.text = getString(R.string.app_version, appVersion)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
    }

    /**
     * CreateProfileActivity Launcher for Edit profile data.
     */
    var launchCreateProfileActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getProfile()
        }
    }

    /**
     * Set the list fragment as default
     */
    private fun setupDefaultFragment() {
        val fragment: Fragment = CatBreedsListFragment.newInstance()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, "list_fragment")
        transaction.commit()
    }

    private fun closeNavigationDrawer() {
        drawerLayout?.closeDrawer(GravityCompat.START, true)
    }
    private fun openNavigationalDrawer() {
      drawerLayout?.openDrawer(GravityCompat.START, true)
    }

    /**
     * Update UI with profile data
     */
    private fun initObservers() {
        viewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            val userProfileEntity = it[0]
            labelName.text = String.format(getString(R.string.nav_name), userProfileEntity.fName, userProfileEntity.lName)
            labelEmail.text = String.format(getString(R.string.nav_email),userProfileEntity.email)
            labelWebsite.text = String.format(getString(R.string.nav_work),userProfileEntity.website)

            if(!TextUtils.isEmpty(userProfileEntity.image)) {
                if(userProfileEntity.image?.contains("file:///")!!) {
                    val uri = Uri.parse(userProfileEntity.image)
                    var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    bitmap = Utils.fixOrientation(bitmap)
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                }
            }
        })
    }

    /**
     * Open the cat detail fragment.
     */
    fun openDetailFragment(catBreedListItem: CatBreedListItem) {
        val fragment: Fragment = CatBreedsDetailFragment.newInstance(catBreedListItem)
        val transaction: FragmentTransaction = supportFragmentManager!!.beginTransaction()
        transaction.hide(supportFragmentManager?.findFragmentByTag("list_fragment")!!)
        transaction.add(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}