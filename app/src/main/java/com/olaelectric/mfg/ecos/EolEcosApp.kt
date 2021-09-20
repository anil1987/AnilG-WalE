package com.olaelectric.mfg.ecos

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.olaelectric.mfg.ecos.presentation.koin.presentationKoinModule
import com.olaelectric.mfg.ecos.presentation.router.IAppLifecycle
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class EolEcosApp : Application(), IAppLifecycle {
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
            androidContext(this@EolEcosApp)
            modules(presentationKoinModule + module { single<IAppLifecycle> { this@EolEcosApp } })
        }
    }
}
