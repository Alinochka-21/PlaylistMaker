package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchActivity : AppCompatActivity() {
    private var savedText: String = ""
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tracks: List<Track> = listOf(
            Track(getString(R.string.track1),getString(R.string.artist1),getString(R.string.time1),getString(R.string.url1)),
            Track(getString(R.string.track2),getString(R.string.artist2),getString(R.string.time2),getString(R.string.url2)),
            Track(getString(R.string.track3),getString(R.string.artist3),getString(R.string.time3),getString(R.string.url3)),
            Track(getString(R.string.track4),getString(R.string.artist4),getString(R.string.time4),getString(R.string.url4)),
            Track(getString(R.string.track5),getString(R.string.artist5),getString(R.string.time5),getString(R.string.url5))
        )

        val recyclerTrackView = findViewById<RecyclerView>(R.id.recyclerTrackView)
        recyclerTrackView.visibility = View.GONE

        val adapter = TrackAdapter(tracks)
        recyclerTrackView.adapter = adapter

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
        }

        editText.doOnTextChanged { s, _, _, _ ->
            buttonClear.visibility = clearButtonVisibility(s)
            savedText = s?.toString() ?: ""
            recyclerTrackView.visibility=View.VISIBLE
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