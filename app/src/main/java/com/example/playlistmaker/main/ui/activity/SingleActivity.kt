package com.example.playlistmaker.main.ui.activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

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