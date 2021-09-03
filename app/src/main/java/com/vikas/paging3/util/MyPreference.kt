package com.vikas.paging3.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log.d
import com.vikas.paging3.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context: Context) {
    private val myPreference: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )

    fun getLanguage(): String {
        d("MyPreference","Default language-> ${Locale.getDefault().language}")
        return myPreference.getString(LANGUAGE_TAG, Locale.getDefault().isO3Country)!!
    }

    fun setLanguage(language: String) {
        myPreference.edit().putString(LANGUAGE_TAG, language).apply()
    }

    companion object {
        private val LANGUAGE_TAG = "language_tag"
    }
}

