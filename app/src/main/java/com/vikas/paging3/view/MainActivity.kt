package com.vikas.paging3.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.content.res.Resources
import android.util.Log.d
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.vikas.paging3.R
import com.vikas.paging3.util.MyPreference
import com.vikas.paging3.view.details.TrailerActivity
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var myPreference:MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        val locale = Locale(myPreference.getLanguage())
//        setLanguage(locale)
        setContentView(R.layout.activity_main)
        setUpView()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.language_action -> {
                val menuItemView = findViewById<View>(R.id.language_action) // SAME ID AS MENU ID
                val popupMenu = PopupMenu(this, menuItemView)
                popupMenu.inflate(R.menu.language_menu)
                popupMenu.setOnMenuItemClickListener {
                    val locale = getLocaleByItemId(it.itemId)
                            setLanguage(locale)
                            d(TAG,"Language is en")
                         true
                    }
                popupMenu.show()
                return true
            }
            R.id.settings_action -> {


                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpView() {
        findViewById<BottomNavigationView>(R.id.navBottomBar).apply {
            setupWithNavController(findNavController(R.id.fragmentNavHost))
        }
    }

    private fun getLocaleByItemId(itemId: Int) : Locale{
        return when(itemId){
            R.id.language_en_action->{
                Locale.ENGLISH
            }
            R.id.language_es_action->{
                Locale("es")
            }
            R.id.language_iw_action->{
                Locale( "iw")
            }
            else -> Locale.ENGLISH
        }
    }
    private fun setLanguage(locale: Locale) {
        Locale.setDefault(locale)
        val resources: Resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}

