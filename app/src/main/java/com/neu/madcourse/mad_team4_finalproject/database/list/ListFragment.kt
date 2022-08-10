package com.neu.madcourse.mad_team4_finalproject.database.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.adapters.TrailDatabaseAdapter
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailViewModel
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentListBinding
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils


class ListFragment : Fragment() {
    /* The Fragment view binding */
    private lateinit var mBinding: FragmentListBinding
    private lateinit var mSavedTrailViewModel: SavedTrailViewModel

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

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
        mSavedTrailViewModel.readAllData.observe(viewLifecycleOwner, Observer { savedTrail ->
            trailAdapter.setData(savedTrail)
        })

        setHasOptionsMenu(true)

        return mBinding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_item) {
            deleteAllTrails()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllTrails() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mBaseUtils.showToast(
                "All Trails removed successfully!", Toast.LENGTH_SHORT
            )
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are your sure you want to delete everything?")
        builder.create().show()
    }
}