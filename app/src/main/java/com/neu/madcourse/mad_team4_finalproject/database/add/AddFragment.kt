package com.neu.madcourse.mad_team4_finalproject.database.add

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailViewModel
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentAddBinding
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils


class AddFragment : Fragment() {
    /* The Fragment view binding */
    private lateinit var mBinding: FragmentAddBinding

    /* The ViewModel reference */
    private lateinit var mTrailViewModel: SavedTrailViewModel

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentAddBinding.inflate(inflater, container, false)

        // Instantiate the view model
        mTrailViewModel = ViewModelProvider(this)[SavedTrailViewModel::class.java]

        // Instantiate the Base utils reference
        mBaseUtils = BaseUtils(requireActivity())

        // Set the Submit button onClick action
        mBinding.viewButtonSubmit.setOnClickListener {
            insertDataToDatabase()
        }

        return mBinding.root;
    }

    /* Helper method to... */
    private fun insertDataToDatabase() {
        val name = mBinding.viewInputName.text.toString()

        if (!TextUtils.isEmpty(name)) {
            // Create a Saved Trail instance
            val savedTrails = SavedTrails(0, Uri.parse(name), name, name, name, name, 0, 0.0, name, 0.0, 0.0, Uri.parse(name))
            // Add data to database
            mTrailViewModel.addTrails(savedTrails)

            mBaseUtils.showToast("Successfully added!", Toast.LENGTH_SHORT)
            // Navigate back to the List fragment
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            mBaseUtils.showToast("Please fill all fields correctly!", Toast.LENGTH_SHORT)
        }
    }
}