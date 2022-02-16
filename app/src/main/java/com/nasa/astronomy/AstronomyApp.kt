package com.nasa.astronomy

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.nasa.astronomy.data.koin.dataKoinModule
import com.nasa.astronomy.domain.koin.domainKoinModule
import com.nasa.astronomy.presentation.koin.presentationKoinModule
import com.nasa.astronomy.presentation.router.IAppLifecycle
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class AstronomyApp : Application(), IAppLifecycle {
    override var foregroundActivity: AppCompatActivity? = null
    override fun onCreate() {
        super.onCreate()
        initAppLifecycle()
        koinConfiguration()
    }

    private fun initAppLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacklImpl() {
            override fun onActivityResumed(activity: Activity) {
                activity.let {
                    if (it is AppCompatActivity) {
                        foregroundActivity = it
                    } else {
                        throw RuntimeException("$it must inherit AppCompatActivity")
                    }
                }
            }
        })
    }

    private fun koinConfiguration() {
        startKoin {
            androidLogger()
            androidContext(this@AstronomyApp)
            modules(
                presentationKoinModule + dataKoinModule + domainKoinModule +
                    module { single<IAppLifecycle> { this@AstronomyApp } }
            )
        }
    }
}
