package com.example.playlistmaker.player.ui.fragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.util.TypedValueCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.player.ui.view_model.BottomSheetState
import com.example.playlistmaker.search.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue
class AudioPlayerFragment() : Fragment() {
    private var _viewBinding: FragmentAudioPlayerBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val lazyCurrentTrack: Track by lazy {
        if (Build.VERSION.SDK_INT >= 33) {
            requireNotNull(requireArguments().getParcelable(CURRENT_TRACK, Track::class.java))
        } else {
            @Suppress("DEPRECATION")
            requireNotNull(requireArguments().getParcelable(CURRENT_TRACK) as? Track)
        }
    }

    val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(lazyCurrentTrack.previewUrl,lazyCurrentTrack.isFavorite)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _viewBinding = FragmentAudioPlayerBinding.inflate(
           inflater,
           container,
           false
       )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTrack = lazyCurrentTrack

        viewModel.getPlayerStatus().observe(viewLifecycleOwner) { state ->
            playOrPause(state.play)
            canPressOnButton(state.isPlayButtonEnabled)
            viewBinding.currentTimeTrack.text = state.progress
            changeLikeState(state.isFavorite)
        }

        viewBinding.backButtonInPlayer.setOnClickListener {
            findNavController().navigateUp()
        }

        viewBinding.trackConerOnPlayer.apply {
            Glide.with(context)
                .load(currentTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.ic_default_45)
                .error(R.drawable.ic_default_45)
                .transform(
                    RoundedCorners(
                        TypedValueCompat.dpToPx(8f, this.resources.displayMetrics).toInt()
                    )
                )
                .into(this)
        }

        viewBinding.playTrackButton.setOnClickListener {
            viewModel.playButtonControl()
        }

        viewBinding.trackDurationValue.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack.trackTimeMillis)

        viewBinding.likeTrackButton.setOnClickListener {
            val newFavoriteState = !currentTrack.isFavorite
            currentTrack.isFavorite = newFavoriteState
            viewModel.onFavoriteClicked(currentTrack)
        }

        viewBinding.trackAlbumValue.apply {
            if (!currentTrack.collectionName.isNullOrEmpty()) {
                text = currentTrack.collectionName
            } else {
                visibility = View.GONE
                viewBinding.trackAlbum.visibility = View.GONE
            }
        }

        viewBinding.trackYearValue.apply {
            if (!currentTrack.releaseDate.isNullOrEmpty()) {
                text = currentTrack.releaseDate.take(4)
            } else {
                visibility = View.GONE
                viewBinding.trackYear.visibility = View.GONE
            }
        }

        viewBinding.trackNameInPlayer.text = currentTrack.trackName
        viewBinding.artistNameInPlayer.text = currentTrack.artistName
        viewBinding.trackGenreValue.text = currentTrack.primaryGenreName
        viewBinding.trackCountryValue.text = currentTrack.country

        val adapter = PlaylistListAdapter(emptyList()) { playlist ->
            viewModel.addTrack(
                name = playlist.playlistName,
                id = playlist.playlistId,
                idTracks = playlist.trackIdList,
                currentTrack = currentTrack
            )
        }

        viewBinding.playlistListRecycler.adapter = adapter

        val overlay = viewBinding.overlay
        val bottomSheet = BottomSheetBehavior.from(viewBinding.bottomSheetConstructor).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        overlay.visibility = View.VISIBLE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewBinding.addTrackInPlaylistButton.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                val playlist = viewModel.getPlaylist()
                adapter.updatePlaylists(playlist)
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }

        viewBinding.toPlaylistCreatorFromPlayer.setOnClickListener {
            findNavController()
                .navigate(R.id.action_audioPlayerFragment_to_playlistCreatorFragment)
        }

        fun trackAlreadyIncluded(playlistName: String) {
            Snackbar.make(viewBinding.root,"Трек уже добавлен в плейлист $playlistName", Snackbar.LENGTH_LONG).show()
        }
        fun trackIncluded(playlistName: String, success: Boolean){
            viewLifecycleOwner.lifecycleScope.launch {
                bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                val playlist = viewModel.getPlaylist()
                adapter.updatePlaylists(playlist)
                Snackbar.make(viewBinding.root, "Добавлено в плейлист $playlistName", Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.getSheetState().observe(viewLifecycleOwner) {state ->
            when (state) {
                is BottomSheetState.Success -> {
                    trackIncluded(state.name, state.success)
                }
                is BottomSheetState.AlreadyState -> {
                    trackAlreadyIncluded(state.name)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        BottomSheetBehavior.from(viewBinding.bottomSheetConstructor).state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
    private fun playOrPause(play: Boolean) {
        Log.d("IconDebug", "playOrPause: play = $play")
        Log.d("IconDebug", "lifecycle = ${viewLifecycleOwner.lifecycle.currentState}")
        Log.d("IconDebug", "Stack: ${Throwable().stackTraceToString()}")

        if (play) {
            viewBinding.playTrackButton.setImageResource(R.drawable.ic_pause_83)
        } else {
            viewBinding.playTrackButton.setImageResource(R.drawable.ic_play_83)
        }
    }

    private fun canPressOnButton(canPress: Boolean){
        viewBinding.playTrackButton.isEnabled = canPress
    }

    private fun changeLikeState(like: Boolean){
        if (like){
            viewBinding.likeTrackButton.setImageResource(R.drawable.ic_like_25)
        } else {
            viewBinding.likeTrackButton.setImageResource(R.drawable.ic_dislike_25)
        }
    }

    companion object {
        private const val CURRENT_TRACK = "CURRENT_TRACK"

        fun putCurrentTrack(currentTrack: Track): Bundle{
            return bundleOf(CURRENT_TRACK to currentTrack)
        }
    }
}

