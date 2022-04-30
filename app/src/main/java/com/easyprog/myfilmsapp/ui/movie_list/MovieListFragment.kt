package com.easyprog.myfilmsapp.ui.movie_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyprog.myfilmsapp.R
import com.easyprog.myfilmsapp.adapters.MovieListAdapter
import com.easyprog.myfilmsapp.adapters.interface_adapters.MovieClickHandler
import com.easyprog.myfilmsapp.converters.getMoviePreviewModel
import com.easyprog.myfilmsapp.databinding.FragmentMovieListFragmentBinding
import com.easyprog.myfilmsapp.helpers.AddChildEventListenerHelper
import com.easyprog.myfilmsapp.model.MovieListItemModel
import com.google.firebase.database.*

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListFragmentBinding? = null
    private val binding get() = _binding!!
    private val mAdapter = MovieListAdapter()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListFragmentBinding.inflate(layoutInflater, container, false)
        initFirebase()
        return binding.root
    }

    private fun initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.url_firebase))
        databaseReference = firebaseDatabase.reference.child("films")
    }

    override fun onResume() {
        super.onResume()
        //initView()
        initRecyclerView()
        mAdapter.attachClickHandler(object : MovieClickHandler{
            override fun onItemClick(movieId: Int) {
                val bundle = Bundle()
                bundle.putInt("movie_id", movieId)
                findNavController().navigate(R.id.action_movieListFragmentFragment_to_moviePlayerFragment, bundle)
            }
        })
    }

    private fun initView() {

    }

    private fun initRecyclerView() {
        binding.recyclerMovieList.adapter = mAdapter
        binding.recyclerMovieList.layoutManager = LinearLayoutManager(requireContext())
        getMovieList()
    }

    private fun getMovieList() {
        databaseReference.addChildEventListener(AddChildEventListenerHelper{
            val preview = it.getMoviePreviewModel()
            loadMovieListToRecyclerView(preview)
        })
    }

    private fun loadMovieListToRecyclerView(newMovieList: MovieListItemModel) {
        mAdapter.getMovieList(newMovieList)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}