package com.neu.madcourse.mad_team4_finalproject.database.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentUpdateBinding.inflate(inflater, container, false)

        // Instantiate the Base utils reference
        mBaseUtils = BaseUtils(requireActivity())


        return mBinding.root;
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
            //mSavedTrailViewModel.deleteTrail() TODO args is needed here
            mBaseUtils.showToast(
                "Trail Successfully Removed!",
                Toast.LENGTH_SHORT
            ) //TODO need to add "${args.currentUser.firstName}?" part
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete") //TODO need to add "${args.currentUser.firstName}?" part
        builder.setMessage("Are your sure you want to delete?") //TODO need to add "${args.currentUser.firstName}?" part
        builder.create().show()
    }
}