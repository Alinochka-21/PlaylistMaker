package com.example.playlistmaker.search.ui.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.State
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment() : Fragment() {
    private var _viewBinding: FragmentSearchBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: SearchViewModel by viewModel()
    private var savedText: String = ""
    private var canPress : Boolean = true
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getTrackEnable().observe(viewLifecycleOwner) {
            canPress = it
        }

        viewBinding.recyclerSearchTrackView.isVisible = false

        historyAdapter =
            TrackAdapter(emptyList()) { track ->
                if (canPress) {
                    viewModel.clickDebounce()
                    toPlayer(track)
                }
            }

        searchAdapter =
            TrackAdapter(emptyList()) { track ->
            if (canPress) {
                viewModel.clickDebounce()
                viewModel.addHistoryTrackList(track)
                toPlayer(track)
            }
        }

        viewBinding.recyclerSearchTrackView.adapter = searchAdapter
        viewBinding.recyclerViewTrackHistory.adapter = historyAdapter

        viewBinding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistoryTrackList()
        }

        viewBinding.buttonTextClear.apply {
            setOnClickListener {
                viewBinding.editText.setText("")
                viewModel.setBeginningState()
                val inputMethodManager =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            }
        }

        viewBinding.editText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.onFocusChanged(hasFocus, viewBinding.editText.text.toString())
        }

        viewBinding.editText.doOnTextChanged { s, _, _, _ ->
            viewBinding.buttonTextClear.visibility = clearButtonVisibility(s)

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

        viewBinding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.performSearch(viewBinding.editText.text.toString().trim().lowercase())
                true
            }
            false
        }

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        viewBinding.updateButton.setOnClickListener {
            viewModel.retryLastRequest()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
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

    fun showError(message: String) {
        viewBinding.apply {
            placeholderLayoutNotInternet.isVisible = true
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = false
            searchAdapter.updateTracks(emptyList())
            progressBar.isVisible = false
        }
    }

    fun showContent(tracks: List<Track>) {
        viewBinding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            trackHistoryLayout.isVisible = false
            searchAdapter.updateTracks(tracks)
            recyclerSearchTrackView.isVisible = true
            progressBar.isVisible = false
        }
    }

    fun showEmpty(message: String) {
        viewBinding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = true
            trackHistoryLayout.isVisible = false
            recyclerSearchTrackView.isVisible = false
            searchAdapter.updateTracks(emptyList())
            progressBar.isVisible = false
        }
    }

    fun showDefaultState() {
        viewBinding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = false
            progressBar.isVisible = false
        }
    }

    fun showHistoryContent(historyTracks: List<Track>) {
        historyAdapter.updateTracks(historyTracks)
        viewBinding.apply {
            placeholderLayoutNotInternet.isVisible = false
            placeholderLayoutNotFound.isVisible = false
            recyclerSearchTrackView.isVisible = false
            trackHistoryLayout.isVisible = true
            progressBar.isVisible = false
        }
    }

    fun showLoading() {
        viewBinding.apply {
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
    fun toPlayer(currentTrack: Track){
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerFragment,
            AudioPlayerFragment.putCurrentTrack(currentTrack)
            )
    }

    companion object{
        const val KEY_FOR_SAVE_TEXT_IN_SEARCH = "KEY_FOR_SAVE_VALUE_IN_SEARCH"
        const val SAVE_TEXT_IN_SEARCH = ""
        const val CURRENT_TRACK = "CURRENT_TRACK"
    }
}