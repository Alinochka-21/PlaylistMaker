package com.example.playlistmaker.library.ui.fragment.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.view_model.PlaylistsState
import com.example.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment() : Fragment() {

    private var _viewBinding: FragmentPlaylistsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.getPlaylistState().observe(viewLifecycleOwner){ state ->
            when (state) {
                is PlaylistsState.ContentState -> {
                    viewBinding.placeHolderNotPlaylists.isVisible = false
                    playlistAdapter.updatePlaylists(state.playlists)
                    viewBinding.playlistRecyclerView.isVisible = true
                }
                is PlaylistsState.EmptyState -> {
                    viewBinding.playlistRecyclerView.isVisible = false
                    viewBinding.placeHolderNotPlaylists.isVisible = true
                }
            }
        }

        playlistAdapter = PlaylistAdapter(emptyList()){
            //переход в плейлист
        }

        viewBinding.playlistRecyclerView.adapter = playlistAdapter
        viewBinding.playlistRecyclerView.layoutManager = GridLayoutManager(requireActivity(),2)


        viewBinding.toPlaylistCreator.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistCreatorFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}