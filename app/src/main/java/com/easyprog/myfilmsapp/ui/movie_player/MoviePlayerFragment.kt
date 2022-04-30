package com.easyprog.myfilmsapp.ui.movie_player

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.easyprog.myfilmsapp.R
import com.easyprog.myfilmsapp.activity.MainActivity
import com.easyprog.myfilmsapp.converters.getFilmModel
import com.easyprog.myfilmsapp.databinding.FragmentMoviePlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.firebase.database.*
import java.lang.Exception


class MoviePlayerFragment : Fragment() {
    private var _binding: FragmentMoviePlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var exoPlayerView: PlayerControlView
    private lateinit var exoPlayer: ExoPlayer
    private var movieID: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviePlayerBinding.inflate(layoutInflater, container, false)
        initFirebase()
        return binding.root
    }

    private fun initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.url_firebase))
        databaseReference = firebaseDatabase.getReference("films")
    }

    override fun onResume() {
        super.onResume()
        checkOrientation()
        getVideoID()
        getVideoUrl()
    }

    private fun getVideoID() {
        movieID = arguments?.getInt("movie_id")
    }

    private fun getVideoUrl() {
        databaseReference.child(movieID.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val videoUrl = snapshot.getFilmModel()
                binding.filmName.text = videoUrl.name
                initExoPlayerView(videoUrl.url)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Fail to get video url.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initExoPlayerView(videoUrl: String) {
        try {
           prepareExoPlayer(videoUrl)
        } catch (e: Exception) {
            Log.e("TAG", "Error : $e")
        }
    }

    private fun prepareExoPlayer(videoUrl: String) {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(videoUrl))
        binding.exoPlayerView.player = exoPlayer
        exoPlayer.playWhenReady = true
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun checkOrientation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemBars()
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars() {
        binding.exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        (activity as MainActivity).supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController =
                ViewCompat.getWindowInsetsController(requireActivity().window.decorView) ?: return
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            val flags =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            requireActivity().window!!.decorView.systemUiVisibility = flags
        }
    }

    override fun onStop() {
        exoPlayer.playWhenReady = false
        super.onStop()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}