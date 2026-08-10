package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment() : Fragment() {
    private var _viewBinding: FragmentSettingsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTheme().observe(viewLifecycleOwner){
            initThemeSwitcher(it)
        }

        viewBinding.shareButton.setOnClickListener {
            viewModel.share()
        }

        viewBinding.supportButton.setOnClickListener {
            viewModel.support()
        }

        viewBinding.agreementButton.setOnClickListener {
            viewModel.agreement()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.themeSwitcher.setOnCheckedChangeListener(null)
        _viewBinding = null
    }

    fun initThemeSwitcher(darkTheme: Boolean) {
        viewBinding.themeSwitcher.isChecked = darkTheme
        viewBinding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeTheme()
        }
    }
}