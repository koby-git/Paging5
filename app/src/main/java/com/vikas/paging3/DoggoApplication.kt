package com.vikas.paging3

import android.app.Application
import com.vikas.paging3.data.local.AppDatabase
import com.vikas.paging3.data.local.LocalInjector

class DoggoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalInjector.appDatabase = AppDatabase.getInstance(this@DoggoApplication)
    }

}