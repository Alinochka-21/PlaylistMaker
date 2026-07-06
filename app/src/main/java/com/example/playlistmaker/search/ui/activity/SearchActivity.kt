package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.main.ui.activity.MainActivity
import com.example.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModelFake
import com.example.playlistmaker.search.ui.view_model.State
import com.google.android.material.button.MaterialButton

class SearchActivity : AppCompatActivity() {
    private var viewModel: SearchViewModel? = null
    private var savedText: String = ""
    private lateinit var editText: EditText
  //  lateinit var lastQuery: String

//    val tracks: MutableList<Track> = mutableListOf()
//    private var canPressOnTrack = true
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    lateinit var progressBar: ProgressBar
 //   lateinit var searchRunnable: Runnable
    private var canPress : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_search)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            val recyclerSearchTrackView = findViewById<RecyclerView>(R.id.recyclerTrackView).apply {
                isVisible = false
            }

        viewModel = try {
            ViewModelProvider(
                this,
                SearchViewModel.getSearchFactory()
            ).get(SearchViewModel::class.java)
        } catch (e: Exception) {
            Log.e("SearchActivity", "❌ Ошибка создания ViewModel: ${e.message}", e)
            e.printStackTrace()
            null // или SearchViewModel(applicationContext)
        }



        viewModel?.getTrackEnable()?.observe(this) {
                canPress = it
            }

            val recyclerHistoryTrackView = findViewById<RecyclerView>(R.id.recyclerviewTrackHistory)
            val historyAdapter =
                TrackAdapter(emptyList()) { track ->
                    if (canPress) {
                        startActivity(createIntent(this, track))
                    }
                }

            val searchAdapter = TrackAdapter(emptyList()) { track ->
                if (canPress) {
                    viewModel?.addHistoryTrackList(track)
                    //   historyAdapter.updateTracks(viewModel!!.getHistoryTrackList())
                    startActivity(createIntent(this, track))
                }
            }

            recyclerSearchTrackView.adapter = searchAdapter
            recyclerHistoryTrackView.adapter = historyAdapter

            val trackHistoryLayout = findViewById<LinearLayout>(R.id.trackHistory)

            val clearHistoryButton = findViewById<MaterialButton>(R.id.bottomClearTrackHistory)
            clearHistoryButton.setOnClickListener {
                viewModel?.clearHistoryTrackList()
                //  trackHistoryLayout.visibility = View.GONE
            }

            val placeholderLayoutNotFound =
                findViewById<LinearLayout>(R.id.placeholderLayoutNotFound)
            val placeholderNotInternet = findViewById<LinearLayout>(R.id.placeholderNotInternet)

            /*   fun showErrorPlaceholder(code: Int){
            when (code){
                R.id.recyclerTrackView -> {
                    placeholderLayoutNotFound.visibility = View.GONE
                    placeholderNotInternet.visibility = View.GONE
                }
                R.id.placeholderLayoutNotFound -> {
                    placeholderLayoutNotFound.visibility = View.VISIBLE
                    placeholderNotInternet.visibility = View.GONE
                    tracks.clear()
                    searchAdapter.notifyDataSetChanged()
                }
                R.id.placeholderNotInternet -> {
                    placeholderNotInternet.visibility = View.VISIBLE
                    placeholderLayoutNotFound.visibility = View.GONE
                    tracks.clear()
                    searchAdapter.notifyDataSetChanged()
                }
            }
        }

      */

            progressBar = findViewById(R.id.progressBarInSearchActivity)

            // val interactor = Creator.getTracksInteractor()

            /*  fun performSearch(query: String) {
            if (query.isEmpty()) return

            lastQuery = query
            progressBar.isVisible = true
            recyclerSearchTrackView.isVisible = false
            interactor.searchTracks(query, object : TrackInteractor.TrackConsumer{
                override fun onSuccess(tracks: List<Track>) {
                    progressBar.isVisible = false
                    recyclerSearchTrackView.isVisible = true
                    if (tracks.isNotEmpty()){
                        this@SearchActivity.tracks.clear()
                        this@SearchActivity.tracks.addAll(tracks)
                        searchAdapter.notifyDataSetChanged()
                        showErrorPlaceholder(R.id.recyclerTrackView)
                    }
                    else {
                        if (savedText.isNotEmpty()) {
                            showErrorPlaceholder(R.id.placeholderLayoutNotFound)
                        }
                        else {
                            recyclerSearchTrackView.isVisible = false
                        }
                    }
                }

                override fun onFailure(errorCode: Int, message: String) {
                    progressBar.isVisible = false
                    showErrorPlaceholder(R.id.placeholderNotInternet)
                }
            })
        }

       */

            val updateButton = findViewById<MaterialButton>(R.id.placeholderErrorButton).apply {
                setOnClickListener {
                    //  performSearch(lastQuery)
                    viewModel?.retryLastRequest()
                }
            }

            val backButton = findViewById<MaterialButton>(R.id.back).apply {
                setOnClickListener {
                    Intent(this@SearchActivity, MainActivity::class.java)
                    finish()
                }
            }

            editText = findViewById(R.id.editText)
            val buttonClear = findViewById<ImageView>(R.id.clearIcon).apply {
                setOnClickListener {
                    editText.setText("")
                    viewModel?.setBeginningState()
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    //  recyclerSearchTrackView.visibility = View.GONE
                    //  showErrorPlaceholder(R.id.recyclerTrackView)
                }
            }

            editText.setOnFocusChangeListener { view, hasFocus ->
                //trackHistoryLayout.isVisible = hasFocus && editText.text.isEmpty() && Creator.getTrackHistoryInteractor().getTrackList().isNotEmpty()
                viewModel?.onFocusChanged(hasFocus, editText.text.toString())
            }

            editText.doOnTextChanged { s, _, _, _ ->
                buttonClear.visibility = clearButtonVisibility(s)
                viewModel?.searchDebounce(
                    textChanged = s?.toString() ?: ""
                )
                savedText = s?.toString() ?: ""
                /*
            searchDebounce()

            if (savedText.isEmpty()){
                recyclerSearchTrackView.isVisible = false
                if (editText.hasFocus() && Creator.getTrackHistoryInteractor().getTrackList().isNotEmpty()) trackHistoryLayout.isVisible = true

            } else {
                recyclerSearchTrackView.isVisible = true
                trackHistoryLayout.isVisible = false
            }

            */
            }

            /*   searchRunnable = Runnable { performSearch(savedText) }

      */

            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel?.searchDebounce(editText.text.toString().trim().lowercase())
                    //   recyclerSearchTrackView.isVisible = false
                    //  progressBar.isVisible = true
                    // performSearch(query)
                    true
                }
                false
            }

            fun showError(message: String) {
                placeholderNotInternet.isVisible = true
                placeholderLayoutNotFound.isVisible = false
                recyclerSearchTrackView.isVisible = false
                trackHistoryLayout.isVisible = false
                searchAdapter.updateTracks(emptyList())
            }

            fun showContent(tracks: List<Track>) {
                placeholderNotInternet.isVisible = false
                placeholderLayoutNotFound.isVisible = false
                trackHistoryLayout.isVisible = false
                searchAdapter.updateTracks(tracks)
                recyclerSearchTrackView.isVisible = true
            }

            fun showEmpty(message: String) {
                placeholderNotInternet.isVisible = false
                placeholderLayoutNotFound.isVisible = true
                trackHistoryLayout.isVisible = false
                recyclerSearchTrackView.isVisible = false
                searchAdapter.updateTracks(emptyList())
            }

            fun showDefaultState() {
                placeholderNotInternet.isVisible = false
                placeholderLayoutNotFound.isVisible = false
                recyclerSearchTrackView.isVisible = false
                trackHistoryLayout.isVisible = false
            }

            fun showHistoryContent(historyTracks: List<Track>) {
                historyAdapter.updateTracks(historyTracks)
                placeholderNotInternet.isVisible = false
                placeholderLayoutNotFound.isVisible = false
                recyclerSearchTrackView.isVisible = false
                trackHistoryLayout.isVisible = true
            }

            fun showLoading() {
                placeholderNotInternet.isVisible = false
                placeholderLayoutNotFound.isVisible = false
                recyclerSearchTrackView.isVisible = false
                trackHistoryLayout.isVisible = false
                progressBar.isVisible = true
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

            viewModel?.getStateLiveData()?.observe(this) {
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
        savedText=fromBundle.getString(KEY_FOR_SAVE_TEXT_IN_SEARCH,SAVE_TEXT_IN_SEARCH)
        editText.setText(savedText)
        if (savedText.isNotEmpty()) {
            viewModel?.searchDebounce(savedText)
        } else {
            viewModel?.setBeginningState()
        }
    }

 /*   fun clickDebounce(): Boolean{
        val currentState = canPressOnTrack
        if (canPressOnTrack){
            canPressOnTrack = false
            mainThreadHandler.postDelayed({canPressOnTrack = true}, CLICK_DEBOUNCE_DELAY)
        }
        return currentState
    }

  */

 /*   fun searchDebounce(){
        if (savedText.isNotEmpty()) {
            mainThreadHandler.apply {
                removeCallbacks(searchRunnable)
                postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            }
        }
    }

  */


    companion object{
        const val KEY_FOR_SAVE_TEXT_IN_SEARCH = "KEY_FOR_SAVE_VALUE_IN_SEARCH"
        const val SAVE_TEXT_IN_SEARCH = ""
        const val CURRENT_TRACK = "CURRENT_TRACK"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun createIntent(context: Context, currentTrack: Track): Intent {
            return Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra(CURRENT_TRACK, currentTrack)
            }
        }
    }
}