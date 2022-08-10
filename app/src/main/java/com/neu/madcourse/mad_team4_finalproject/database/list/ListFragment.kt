package com.neu.madcourse.mad_team4_finalproject.database.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailViewModel
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrails
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentListBinding


class ListFragment : Fragment() {
    /* The Fragment view binding */
    private lateinit var mBinding: FragmentListBinding
    private lateinit var mSavedTrailViewModel: SavedTrailViewModel

    /* The Activity context */
    private val mContext = requireActivity()
    private var savedTrailList = mutableListOf<SavedTrails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentListBinding.inflate(inflater, container, false)

        // Set the FAB onClick action
        mBinding.viewFabAddListing.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Recycler View initialization
        val trailAdapter = TrailDatabaseAdapter(mContext, savedTrailList)
        val trailRecyclerView = mBinding.recyclerViewDbListing
        trailRecyclerView.adapter = trailAdapter
        trailRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        //TrailViewModel initialization
        mSavedTrailViewModel = ViewModelProvider(this)[SavedTrailViewModel::class.java]
        mSavedTrailViewModel.readAllData.observe(viewLifecycleOwner, Observer {savedTrail ->
            trailAdapter.setData(savedTrail)
        })

        return mBinding.root;
    }
}