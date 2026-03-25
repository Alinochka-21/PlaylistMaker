package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged

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

        val backButtom = findViewById<com.google.android.material.button.MaterialButton>(R.id.back)
        backButtom.setOnClickListener {
            Intent(this, MainActivity::class.java)
            finish()
        }

        val buttonClear = findViewById<ImageView>(R.id.clearIcon)
        editText = findViewById<EditText>(R.id.editText)

        buttonClear.setOnClickListener {
            editText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        editText.doOnTextChanged { s, _, _, _ ->
            buttonClear.visibility = clearButtonVisibility(s)
            savedText = s?.toString() ?: ""
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