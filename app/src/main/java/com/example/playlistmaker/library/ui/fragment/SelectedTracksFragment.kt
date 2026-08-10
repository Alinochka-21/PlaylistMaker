package com.example.playlistmaker.library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSelectedTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectedTracksFragment() : Fragment() {

    private  var _viewBinding: FragmentSelectedTracksBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: SelectedTracksViewModel by viewModel()

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
        this@SelectedTracksFragment.viewBinding.placeHolderNotSelect.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
    companion object {
        fun newInstance() = SelectedTracksFragment()
    }
}