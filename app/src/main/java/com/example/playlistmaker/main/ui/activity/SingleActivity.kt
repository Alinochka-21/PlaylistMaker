package com.example.playlistmaker.main.ui.activity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SingleActivityBinding
import com.example.playlistmaker.main.ui.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleActivity : AppCompatActivity() {
    private lateinit var viewBiding: SingleActivityBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewBiding = SingleActivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(viewBiding.bottomNavigationView) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = 0
            v.layoutParams = layoutParams
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment -> viewBiding.bottomNavigationView.isVisible = false
                else -> viewBiding.bottomNavigationView.isVisible = true
            }
        }

        viewBiding.bottomNavigationView.setupWithNavController(navController)

        viewModel?.getLiveTheme()?.observe(this) {
            (applicationContext as App).switchTheme(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel?.getThemeMode()
    }
}