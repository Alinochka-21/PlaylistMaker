package com.example.playlistmaker.playlist.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.domain.model.Playlist
import com.example.playlistmaker.player.ui.fragment.AudioPlayerFragment
import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlistmaker.playlistEdit.ui.fragment.PlaylistEditFragment
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment() : Fragment() {
    var _viewBinding: FragmentPlaylistBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var adapter: PlaylistTracksAdapter
    private var trackIdToDelete: Long? = null
   private lateinit var playlist: Playlist

    private lateinit var tracksBottomSheet: BottomSheetBehavior<ViewGroup>

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        if (!isAdded) return@OnGlobalLayoutListener

        val contentBottom = viewBinding.contentLayout.bottom
        val screenHeight = resources.displayMetrics.heightPixels
        val marginPx = resources.getDimensionPixelSize(R.dimen.bottom_sheet_margin_top)

        tracksBottomSheet.peekHeight = (screenHeight - contentBottom - marginPx)
            .coerceAtLeast(0)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPlaylistBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.backFromPlaylistFragmentButton.setOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigateUp()
        }

        viewModel.getPlaylistInfo(requireArguments().getLong(CURRENT_PLAYLIST_ID))

        viewModel.getState().observe(viewLifecycleOwner){ state ->
            if (state.tracks.isEmpty()){
                viewBinding.noTracks.isVisible = true
            }
            render(state.playlist, state.tracks)
            adapter.updateTracks(state.tracks.reversed())
            playlist = state.playlist

        }

        adapter = PlaylistTracksAdapter(
            emptyList(),
            {track ->
                findNavController()
                    .navigate(
                        R.id.action_playlistFragment_to_audioPlayerFragment,
                        AudioPlayerFragment.putCurrentTrack(track)
                    )
            }) { track ->
            trackIdToDelete = track.trackId

            craftDialog(R.string.delete_dialog, R.string.yes, R.string.no) {
                viewModel.deleteTrack(viewModel.getCurrentPlaylist().playlistId, trackIdToDelete!!)
            }.show()
        }
        viewBinding.playlistListRecycler.adapter = adapter

        tracksBottomSheet = BottomSheetBehavior.from<ViewGroup>(viewBinding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewBinding.contentLayout.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        val menuBottomSheet = BottomSheetBehavior.from(viewBinding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewBinding.share.setOnClickListener {

            if (viewModel.getCurrentPlaylist().playlistSize == 0) {
                Snackbar.make(viewBinding.root, R.string.no_tracks_in_playlist, Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.sharePlaylist(buildPlaylistShareText(viewModel.getCurrentPlaylist(),viewModel.getCurrentTrackList()))
            }
        }

        viewBinding.deletePlaylist.setOnClickListener {
            craftDialog(R.string.sureDeletePlaylist, R.string.yes_delete, R.string.cancel) {
                viewModel.deletePlaylist()
                menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                findNavController().navigateUp()
            }.show()
            menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        viewBinding.overlay.isVisible = false
                    }
                    else -> {

                        viewBinding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {}
        })

        viewBinding.menu.setOnClickListener {
            playlistElementPreparation(playlist)
            menuBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewBinding.playlistShare.setOnClickListener {
            menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            if (viewModel.getCurrentPlaylist().playlistSize == 0) {
                Snackbar.make(viewBinding.root, R.string.no_tracks_in_playlist, Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.sharePlaylist(buildPlaylistShareText(viewModel.getCurrentPlaylist(),viewModel.getCurrentTrackList()))
            }
        }

        viewBinding.editPlaylist.setOnClickListener {
            findNavController()
                .navigate(R.id.action_playlistFragment_to_playlistEditFragment, bundleOf(
                    PlaylistEditFragment.CURRENT_PLAYLIST_ID_FOR_EDIT to viewModel.getCurrentPlaylist().playlistId))
        }
    }

    private fun craftDialog(messageId: Int, positive: Int, negative: Int, actionDelete: () -> Unit): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(requireActivity(), R.style.deleteDialog)
            .setMessage(getString(messageId) )
            .setPositiveButton(positive){ dialog, which ->
                actionDelete()
            }
            .setNegativeButton(negative){ dialog, which ->
                dialog.dismiss()
            }

    @SuppressLint("SetTextI18n")
    private fun render(playlist: Playlist, tracks: List<Track>) {
        if (playlist.coverUri == null) {
            viewBinding.coverInPlaylistFragment.setImageResource(R.drawable.ic_empty_cover_236)
            viewBinding.coverInPlaylistFragment.scaleType = ImageView.ScaleType.CENTER
        } else {
            viewBinding.coverInPlaylistFragment.setImageURI(playlist.coverUri)
            viewBinding.coverInPlaylistFragment.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        viewBinding.name.text = playlist.playlistName

        if (playlist.playlistDescription.isNotEmpty()) {
            viewBinding.description.text = playlist.playlistDescription
        } else {
            viewBinding.description.isVisible = false
        }

        viewBinding.time.text = totalDuration(tracks)
        viewBinding.tracksCount.text = tracks.size.toString() + wordEnding(tracks.size)
    }

    @SuppressLint("SetTextI18n")
    private fun playlistElementPreparation(playlist: Playlist) {
        viewBinding.menuBSName.text = playlist.playlistName
        viewBinding.menuBSSize.text = playlist.trackIdList.size.toString() + wordEnding(playlist.trackIdList.size)
        if (playlist.coverUri == null) {
            viewBinding.menuBSCover.setImageResource(R.drawable.ic_default_45)
            viewBinding.menuBSCover.scaleType = ImageView.ScaleType.CENTER
        } else {
            viewBinding.menuBSCover.setImageURI(playlist.coverUri)
            viewBinding.menuBSCover.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun buildPlaylistShareText(playlist: Playlist, tracks: List<Track>): String =
        buildString {
            append(playlist.playlistName).append("\n")

            if (!playlist.playlistDescription.isNullOrBlank()) {
                append(playlist.playlistDescription).append("\n")
            }

            append("${tracks.size} ${wordEnding(tracks.size)}").append("\n")

            tracks.forEachIndexed { index, track ->
                append(
                    "${index + 1}. ${track.artistName} - ${track.trackName} " +
                           "(${totalDuration(tracks)})"
                )
                if (index < tracks.size - 1) append("\n")
            }
        }

    private fun totalDuration(tracks: List<Track>): String {
        val time = tracks.map { track ->
            track.trackTimeMillis
        }
        val result = (time.sum()/1000/60).toString()
        return result + minuteEnding(time.sum()/1000/60)
    }
    private fun wordEnding(count: Int): String{
        return when(count) {
            1 ->  requireActivity().getString(R.string.one)
            in (2..4) -> requireActivity().getString(R.string.two_to_four)
            else -> requireActivity().getString(R.string.from_four)
        }
    }
    private fun minuteEnding(count: Long): String {
        return when(count) {
            1L -> requireActivity().getString(R.string.one_minute)
            in (2..4) -> requireActivity().getString(R.string.two_minutes_to_four)
            else -> requireActivity().getString(R.string.more_four_minutes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackIdToDelete = null
    }
    companion object {
        const val CURRENT_PLAYLIST_ID = "CURRENT_ID"

        fun getPlaylistId(id: Long): Bundle {
            return bundleOf(CURRENT_PLAYLIST_ID to id)
        }
    }
}