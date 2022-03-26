package com.test.flipgrid.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.fligrid.viewmodel.SplashViewModel
import com.test.flipgrid.FlipGridApp
import com.test.flipgrid.R
import com.test.flipgrid.models.entity.UserProfileEntity
import com.test.flipgrid.viewmodel.HomeActivityViewModel

class SplashActivity : BaseActivity() {
    private lateinit var viewModel: SplashViewModel
    private lateinit var  homeActivityViewModel: HomeActivityViewModel
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        initObservers()
    }

    /**
     * Initialize view model
     */
    override fun onViewModelInit() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeActivityViewModel(applicationContext as FlipGridApp) as T
            }
        }).get(HomeActivityViewModel::class.java)
    }

    /**
     * Register the observer to observe results from viewmodel
     */
    private fun initObservers() {
        viewModel.getSplashObserver().observe(this, Observer <Boolean>{
            homeActivityViewModel.getProfile()
        })
        homeActivityViewModel.getProfileObserver().observe(this, Observer <List<UserProfileEntity>>{
            if(it != null && it.size > 0)
                launchHome()
            else
                launchCreateProfile()
        })
        viewModel.startSplashDelay()
    }

    /**
     * Launch the school list activity and close itself
     */
    private fun launchCreateProfile() {
        val i = Intent(this@SplashActivity, CreateProfileActivity::class.java)
        startActivity(i)
        finish()
    }

    /**
     * Launch HOme activity.
     */
    private fun launchHome() {
       // val i = Intent(this@SplashActivity, HomeActivity::class.java)
        val i = Intent(this@SplashActivity, ProfileConfirmationActivity::class.java)
        startActivity(i)
        finish()
    }
}