package com.example.playlistmaker.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment() : Fragment() {
    private var _viewBinding: FragmentMediaLibraryBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentMediaLibraryBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.pagerInMedia.adapter = MediaPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )

        tabMediator = TabLayoutMediator(
            viewBinding.tableInMedia,
            viewBinding.pagerInMedia
        ) { tab, position ->
            if (position == 0) {
                tab.text = getString(R.string.selected_tracks)
            }
            else {
                tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _viewBinding = null
    }

}