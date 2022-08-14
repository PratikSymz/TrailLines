package com.neu.madcourse.mad_team4_finalproject.database.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailViewModel
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentUpdateBinding
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils

class UpdateFragment : Fragment() {

    /* The Fragment view binding */
    private lateinit var mBinding: FragmentUpdateBinding

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

    private lateinit var mSavedTrailViewModel: SavedTrailViewModel

    /*Safe Args reference */
    private val args by navArgs<UpdateFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentUpdateBinding.inflate(inflater, container, false)

        // Instantiate the Base utils reference
        mBaseUtils = BaseUtils(requireActivity())

        // Instantiate the viewModel object
        mSavedTrailViewModel = ViewModelProvider(this).get(SavedTrailViewModel::class.java)

        // setting the trail name for whenever the user updates the name
        mBinding.viewUpdateName.setText(args.currentTrail.shortName)


        return mBinding.root;
    }

    /**
     * Method for updating the name field in the fragment_update xml
     * More fields needs to be added to it and make necessary changes
     * Ignore this not much to do!
     */
    private fun updateTrailItem() {
        val shortName = mBinding.viewUpdateName.text.toString()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_item) {
            deleteTrail()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTrail() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mSavedTrailViewModel.deleteTrail(args.currentTrail)
            mBaseUtils.showToast(
                "Trail Successfully Removed ${args.currentTrail.shortName}!",
                Toast.LENGTH_SHORT
            )
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentTrail.shortName}?")
        builder.setMessage("Are your sure you want to delete ${args.currentTrail.shortName}?")
        builder.create().show()
    }
}