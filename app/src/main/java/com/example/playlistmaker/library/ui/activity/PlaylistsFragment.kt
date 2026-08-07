package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistsFragment() : Fragment() {

    private var _viewBiding: FragmentPlaylistsBinding? = null
    private val viewBiding get() = _viewBiding!!
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBiding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return viewBiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBiding.placeHolderNotPlaylists.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBiding = null
    }
    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}