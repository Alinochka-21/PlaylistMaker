package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.networking.SearchQuery
import com.example.playlistmaker.networking.TracksResponse
import com.example.playlistmaker.networking.iTunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var savedText: String = ""
    private lateinit var editText: EditText
    lateinit var lastQuery: SearchQuery

    private val baseTracksUrl = "https://itunes.apple.com"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseTracksUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tracksService = retrofit.create(iTunesApi::class.java)
    val tracks: ArrayList<Track> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerTrackView = findViewById<RecyclerView>(R.id.recyclerTrackView)
        recyclerTrackView.visibility = View.GONE

        val adapter = TrackAdapter(tracks)
        recyclerTrackView.adapter = adapter

        val placeholderLayoutNotFound = findViewById<LinearLayout>(R.id.placeholderLayoutNotFound)
        val placeholderNotInternet = findViewById<LinearLayout>(R.id.placeholderNotInternet)


        fun showErrorPlaceholder(code: Int){
            when (code){
                R.id.recyclerTrackView -> {
                    placeholderLayoutNotFound.visibility = View.GONE
                    placeholderNotInternet.visibility = View.GONE
                }
                R.id.placeholderLayoutNotFound -> {
                    placeholderLayoutNotFound.visibility = View.VISIBLE
                    placeholderNotInternet.visibility = View.GONE
                    tracks.clear()
                    adapter.notifyDataSetChanged()
                }
                R.id.placeholderNotInternet -> {
                    placeholderNotInternet.visibility = View.VISIBLE
                    placeholderLayoutNotFound.visibility = View.GONE
                    tracks.clear()
                    adapter.notifyDataSetChanged()
                }
            }
        }

        fun performSearch(request: SearchQuery){
            tracksService.search(request.text).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.code() == 200){
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.clear()
                            tracks.addAll(response.body()!!.results)
                            adapter.notifyDataSetChanged()
                            showErrorPlaceholder(R.id.recyclerTrackView)
                        }
                        else {
                            showErrorPlaceholder(R.id.placeholderLayoutNotFound)
                        }
                    } else {
                        showErrorPlaceholder(R.id.placeholderNotInternet)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showErrorPlaceholder(R.id.placeholderNotInternet)
                    lastQuery = request
                }
            })
        }
        val updateButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.placeholderErrorButton)

        updateButton.setOnClickListener {
            performSearch(lastQuery)
        }

        val backButtom = findViewById<com.google.android.material.button.MaterialButton>(R.id.back)
        backButtom.setOnClickListener {
            Intent(this, MainActivity::class.java)
            finish()
        }

        val buttonClear = findViewById<ImageView>(R.id.clearIcon)
        editText = findViewById(R.id.editText)

        buttonClear.setOnClickListener {
            editText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            recyclerTrackView.visibility = View.GONE
            showErrorPlaceholder(R.id.recyclerTrackView)
        }

        editText.doOnTextChanged { s, _, _, _ ->
            buttonClear.visibility = clearButtonVisibility(s)
            savedText = s?.toString() ?: ""
            recyclerTrackView.visibility=View.VISIBLE
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = editText.text.toString().trim().lowercase()
                performSearch(SearchQuery(query))
                true
            }
            false
        }
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(inBundle: Bundle){
        super.onSaveInstanceState(inBundle)
        inBundle.putString(KEY_FOR_SAVE_TEXT_IN_SEARCH,savedText)
    }

    override fun onRestoreInstanceState(fromBundle: Bundle){
        super.onRestoreInstanceState(fromBundle)
        savedText=fromBundle.getString(KEY_FOR_SAVE_TEXT_IN_SEARCH,SAVE_TEXT_IN_SEARCH)
        editText.setText(savedText)
    }

    companion object{
        const val KEY_FOR_SAVE_TEXT_IN_SEARCH = "KEY_FOR_SAVE_VALUE_IN_SEARCH"
        const val SAVE_TEXT_IN_SEARCH = ""
    }
}