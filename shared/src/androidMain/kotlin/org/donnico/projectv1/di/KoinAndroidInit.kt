package org.donnico.projectv1.di

import android.content.Context
import org.koin.android.ext.koin.androidContext

fun initKoinAndroid(context: Context) = initKoin {
    androidContext(context)
}