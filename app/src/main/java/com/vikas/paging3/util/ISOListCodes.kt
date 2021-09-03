package com.vikas.paging3.util

import android.util.Log.d
import java.util.*

class ISOListCodes {

    companion object {
        fun getLocaleLanguage(): String {
            return when (Locale.getDefault().language) {
                "iw" -> {
                    d("getLocaleLanguage","language is -> he")
                    "he"
                }
                else -> {
                    d("getLocaleLanguage","language is -> ${Locale.getDefault().language}")
                    Locale.getDefault().language
                }
            }
        }
    }
}