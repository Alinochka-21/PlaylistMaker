package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.main.ui.activity.MainActivity
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var viewBiding: ActivitySearchBinding
    private var savedText: String = ""
    private var canPress : Boolean = true
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewBiding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBiding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.getTrackEnable().observe(this) {
            canPress = it
        }


        viewBiding.recyclerSearchTrackView.isVisible = false

        historyAdapter =
            TrackAdapter(emptyList()) { track ->
                if (canPress) {
                    viewModel.clickDebounce()
                    startActivity(createIntent(this, track))
                }
            }

        searchAdapter = TrackAdapter(emptyList()) { track ->
            if (canPress) {
                viewModel.clickDebounce()
                viewModel.addHistoryTrackList(track)
                startActivity(createIntent(this, track))
            }
        }

        viewBiding.recyclerSearchTrackView.adapter = searchAdapter
        viewBiding.recyclerViewTrackHistory.adapter = historyAdapter

        viewBiding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistoryTrackList()
        }

        viewBiding.updateButton.setOnClickListener {
                viewModel.retryLastRequest()
        }

        viewBiding.backButton.setOnClickListener {
                Intent(this@SearchActivity, MainActivity::class.java)
                finish()
        }


        val buttonClear = findViewById<ImageView>(R.id.buttonClear).apply {
            setOnClickListener {
                viewBiding.editText.setText("")
                viewModel.setBeginningState()
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }

        viewBiding.editText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.onFocusChanged(hasFocus, viewBiding.editText.text.toString())
        }

        viewBiding.editText.doOnTextChanged { s, _, _, _ ->
            buttonClear.visibility = clearButtonVisibility(s)

            if (s.toString().isNotEmpty()) {
                viewModel.searchDebounce(
                    textChanged = s?.toString() ?: ""
                )
            }
            else {
                viewModel.setBeginningState()
            }
            savedText = s?.toString() ?: ""

        }

        viewBiding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.performSearch(viewBiding.editText.text.toString().trim().lowercase())
                true
            }
            false
        }

        viewModel.getStateLiveData().observe(this) {
            render(it)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FOR_SAVE_TEXT_IN_SEARCH,savedText)
    }

    override fun onRestoreInstanceState(fromBundle: Bundle){
        super.onRestoreInstanceState(fromBundle)
        savedText = fromBundle.getString(KEY_FOR_SAVE_TEXT_IN_SEARCH,SAVE_TEXT_IN_SEARCH)
        viewBiding.editText.setText(savedText)
        if (savedText.isNotEmpty()) {
            viewModel.searchDebounce(savedText)
        } else {
            viewModel.setBeginningState()
        }
    }

    fun showError(message: String) {
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = true
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = false
            searchAdapter.updateTracks(emptyList())
            progressBar.isVisible = false
        }
    }

    fun showContent(tracks: List<Track>) {
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            trackHistoryLayout.isVisible = false
            searchAdapter.updateTracks(tracks)
            recyclerSearchTrackView.isVisible = true
            progressBar.isVisible = false
        }
    }

    fun showEmpty(message: String) {
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = true
            trackHistoryLayout.isVisible = false
            recyclerSearchTrackView.isVisible = false
            searchAdapter.updateTracks(emptyList())
            progressBar.isVisible = false
        }
    }

    fun showDefaultState() {
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = false
            progressBar.isVisible = false
        }
    }

    fun showHistoryContent(historyTracks: List<Track>) {
        historyAdapter.updateTracks(historyTracks)
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = true
            progressBar.isVisible = false
        }
    }

    fun showLoading() {
        viewBiding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = false
            progressBar.isVisible = true
        }
    }

    fun render(state: State) {
        when (state) {
            is State.Loading -> showLoading()
            is State.HistoryContent -> showHistoryContent(state.historyTracks)
            is State.Error -> showError(state.message)
            is State.Content -> showContent(state.tracks)
            is State.Default -> showDefaultState()
            is State.Empty -> showEmpty(state.message)
        }
    }

    companion object{
        const val KEY_FOR_SAVE_TEXT_IN_SEARCH = "KEY_FOR_SAVE_VALUE_IN_SEARCH"
        const val SAVE_TEXT_IN_SEARCH = ""
        const val CURRENT_TRACK = "CURRENT_TRACK"

        fun createIntent(context: Context, currentTrack: Track): Intent {
            return Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra(CURRENT_TRACK, currentTrack)
            }
        }
    }
}