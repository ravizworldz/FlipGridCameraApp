package com.test.flipgrid

import android.app.Application
import com.test.flipgrid.di.AppModule
import com.test.flipgrid.di.DaggerIAppComponent
import com.test.flipgrid.di.IAppComponent

class FlipGridApp: Application() {

    private lateinit var appComponent: IAppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerIAppComponent.builder()
            .appModule(AppModule(this@FlipGridApp))
            .build()
    }

    /**
     * Return DI component
     */
    fun getAppComponent(): IAppComponent {
        return appComponent
    }
}