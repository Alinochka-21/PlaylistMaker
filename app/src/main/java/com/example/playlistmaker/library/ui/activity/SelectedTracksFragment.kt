package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment() : Fragment() {

    private lateinit var viewBiding: FragmentSelectedTracksBinding
    private val viewModel: SelectedTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBiding = FragmentSelectedTracksBinding.inflate(inflater, container, false)
        return viewBiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBiding.placeHolderNotSelect.isVisible = true
    }
    companion object {
        fun newInstance() = SelectedTracksFragment()
    }
}