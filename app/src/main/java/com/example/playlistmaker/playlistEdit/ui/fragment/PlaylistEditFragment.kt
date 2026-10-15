package com.example.playlistmaker.playlistEdit.ui.fragment

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.playlistCreator.ui.fragment.PlaylistCreatorFragment
import com.example.playlistmaker.playlistEdit.ui.view_model.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistEditFragment() : PlaylistCreatorFragment() {

    private var oldCover: Uri? = null
    override val viewModel: PlaylistEditViewModel by viewModel()
    private var newName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val blue = ContextCompat.getColor(requireContext(), R.color.YP_Blue)
        val blueStateList = ColorStateList.valueOf(blue)

        with(viewBinding.playlistNameStroke) {
            hintTextColor = blueStateList
            setBoxStrokeColorStateList(blueStateList)
        }
        viewBinding.playlistName.setHintTextColor(blue)

        with(viewBinding.playlistDescriptionStroke) {
            hintTextColor = blueStateList
            setBoxStrokeColorStateList(blueStateList)
        }

        viewBinding.playlistDescription.setHintTextColor(blue)


        viewBinding.createPlaylist.text = getString(R.string.save)
        viewBinding.newPlaylist.text = getString(R.string.editPlaylist)

        viewModel.getPlaylistId(requireArguments().getLong(CURRENT_PLAYLIST_ID_FOR_EDIT))

        viewModel.getState().observe(viewLifecycleOwner) { state ->
           currentName = state.name
           currentDescription = state.description
            oldCover = state.cover

            viewBinding.playlistName.setText(state.name)

            if (!currentDescription.isNullOrEmpty()) {
                viewBinding.playlistDescription.setText(state.description)
            }

            if (state.cover.toString().isNullOrEmpty()) {
                viewBinding.albumCover.setImageResource(R.drawable.ic_empty_cover_236)
                viewBinding.albumCover.scaleType = ImageView.ScaleType.CENTER
            } else {
                viewBinding.albumCover.setImageURI(state.cover)
                viewBinding.albumCover.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        viewBinding.playlistName.doOnTextChanged { s, _, _, _ ->
            newName = s.toString()
            if (s?.isBlank() ?: s.isNullOrEmpty()) {
                viewBinding.createPlaylist.isEnabled = false
            } else {
                viewBinding.createPlaylist.isEnabled = true
            }
        }

        viewBinding.playlistDescription.doOnTextChanged { s, _, _, _ ->
            currentDescription = s.toString()
        }

        viewBinding.createPlaylist.setOnClickListener {
            saveImageToPrivateStorage(currentCoverForBack)
            viewModel.saveNewInformation(getName(),getCover(),getDescription())
            currentCoverForBack = null
            savedUri = null
            findNavController().navigateUp()
        }

    }

    override fun backLogic() {
        findNavController()
            .navigateUp()
    }

    private fun getName(): String{
        return newName.ifEmpty {
            currentName
        }
    }
    private fun getDescription(): String {
       return currentDescription
    }

    private fun getCover(): Uri? {
        return savedUri ?: oldCover
    }

    companion object {
        const val CURRENT_PLAYLIST_ID_FOR_EDIT = "CURRENT_ID_FOR_EDIT"

        fun getPlaylistId(id: Long): Bundle {
            return bundleOf(CURRENT_PLAYLIST_ID_FOR_EDIT to id)
        }
    }
}