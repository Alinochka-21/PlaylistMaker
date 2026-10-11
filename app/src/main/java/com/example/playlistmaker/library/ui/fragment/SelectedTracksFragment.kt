package com.example.playlistmaker.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import com.example.playlistmaker.library.ui.view_model.FavoriteListState
import com.example.playlistmaker.library.ui.view_model.SelectedTracksViewModel
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.fragment.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment() : Fragment() {

    private  var _viewBinding: FragmentSelectedTracksBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: SelectedTracksViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private var favoriteList: List<Track> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteListState().observe(viewLifecycleOwner){ state ->
            when (state) {
                is FavoriteListState.FavoriteList -> {
                    favoriteList = state.list
                    viewBinding.placeHolderNotSelect.isVisible = false
                    viewBinding.recyclerFavoriteTrackView.isVisible = true
                }
                is FavoriteListState.EmptyFavoriteList -> {
                    favoriteList = emptyList()
                    viewBinding.placeHolderNotSelect.isVisible = true
                    viewBinding.recyclerFavoriteTrackView.isVisible = false
                }
            }
            trackAdapter =
                TrackAdapter(favoriteList) { track ->
                    toPlayer(track)
                }

            viewBinding.recyclerFavoriteTrackView.adapter = trackAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun toPlayer(currentTrack: Track){
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            AudioPlayerFragment.putCurrentTrack(currentTrack)
        )
    }
    companion object {
        fun newInstance() = SelectedTracksFragment()
    }
}