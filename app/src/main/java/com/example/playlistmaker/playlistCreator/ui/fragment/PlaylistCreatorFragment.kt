package com.example.playlistmaker.playlistCreator.ui.fragment

import android.Manifest
import com.example.playlistmaker.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.playlistCreator.ui.view_model.PlaylistCreatorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.math.PI

open class PlaylistCreatorFragment() : Fragment(){

    private var _viewBinding: FragmentCreatePlaylistBinding? = null
    protected val viewBinding get() = _viewBinding!!

    open  val viewModel: PlaylistCreatorViewModel by viewModel()
    protected var currentCoverForBack: Uri? = null
    protected var savedUri: Uri? = null
    protected var currentName = ""
    protected var currentDescription = ""
    private var dialog: MaterialAlertDialogBuilder? = null

    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        currentCoverForBack = uri
        if (uri!=null) {
            viewBinding.albumCover.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentCreatePlaylistBinding.inflate(inflater,container,false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = createDialog()
        val requester = PermissionRequester.instance()

        viewBinding.playlistName.doOnTextChanged {s, _, _, _ ->
            currentName = s.toString()
            if (s?.isBlank() ?: s.isNullOrEmpty()) {
                viewBinding.createPlaylist.isEnabled = false
            } else {
                viewBinding.createPlaylist.isEnabled = true
            }
        }

        viewBinding.playlistDescription.doOnTextChanged { s, _, _, _ ->
            currentDescription = s.toString()
        }

        val permissions = choiceTruePermissions()

        viewBinding.albumCover.setOnClickListener {
           val checkPermission = requester.areGranted(*permissions)
           if (checkPermission) {
               pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
           } else {
               viewLifecycleOwner.lifecycleScope.launch {
                   requester.request(*permissions).collect { result ->
                       when (result) {
                           PermissionResult.Cancelled -> return@collect
                           is PermissionResult.Denied.DeniedPermanently -> toSettings()
                           is PermissionResult.Denied.NeedsRationale -> return@collect
                           is PermissionResult.Granted -> pickMedia
                       }
                   }
               }
           }
        }

        viewBinding.createPlaylist.setOnClickListener {
            saveImageToPrivateStorage(currentCoverForBack)
            viewModel.addPlaylist(currentName,getDescription(currentDescription),savedUri)
            Snackbar.make(viewBinding.root, "Плейлист $currentName создан", Snackbar.LENGTH_LONG).show()
            currentCoverForBack = null
            savedUri = null
            findNavController().navigateUp()
        }

        viewBinding.backButtonInPlaylist.setOnClickListener {
            backLogic()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backLogic()
        }
    }

    protected fun saveImageToPrivateStorage(uri: Uri?) {
        if (uri != null) {
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "cover_album"
            )

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")

            requireActivity().contentResolver.openInputStream(uri).use { inputStream ->
                file.outputStream().use { outputStream ->
                    BitmapFactory
                        .decodeStream(inputStream)
                        .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
                }
            }
            savedUri = Uri.fromFile(file)
        } else {
            savedUri = null
        }
    }

    private fun choiceTruePermissions(): Array<String> {
         return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ->
              arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
               arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
                )

            else -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun createDialog(): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(requireActivity(), R.style.MyDialogTheme)
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_description)
            .setPositiveButton(R.string.complete) {dialog, which ->
                currentCoverForBack = null
                findNavController().navigateUp()
            }
            .setNeutralButton(R.string.cancel) {dialog, which ->
                dialog.cancel()
            }

    private fun toSettings(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data= Uri.fromParts("package", requireActivity().packageName, null)
        requireActivity().startActivity(intent)
    }

    open fun backLogic() {
        if (currentCoverForBack == null && currentName.isBlank() && currentDescription.isBlank()) {
            findNavController().navigateUp()
        } else {
            dialog?.show()
        }
    }
    private fun getDescription(description: String): String {
        return description.ifBlank { "" }
    }
}

