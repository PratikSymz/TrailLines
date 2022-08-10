package com.neu.madcourse.mad_team4_finalproject.database.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentListBinding


class ListFragment : Fragment() {
    /* The Fragment view binding */
    private lateinit var mBinding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentListBinding.inflate(inflater, container, false)

        // Set the FAB onClick action
        mBinding.viewFabAddListing.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return mBinding.root;
    }
}