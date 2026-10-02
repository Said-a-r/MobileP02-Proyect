package org.donnico.projectv1

import android.app.Application
import org.donnico.projectv1.di.initKoinAndroid

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}