package com.vikas.paging3.data.local

object LocalInjector {

    var appDatabase: AppDatabase? = null

    fun injectDb(): AppDatabase? {
        return appDatabase
    }
}