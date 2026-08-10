package com.example.playlistmaker.library.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):
    FragmentStateAdapter(
    fragmentManager,
    lifecycle
    ) {

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) SelectedTracksFragment.newInstance()
        else PlaylistsFragment.newInstance()
    }

    override fun getItemCount(): Int = 2
}