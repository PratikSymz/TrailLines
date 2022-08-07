package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.FilterScreenBinding;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetBehavior bottomSheetBehavior;
    FilterScreenBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.filter_screen, null);
        mBinding = FilterScreenBinding.bind(view);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBinding.filterClose.setOnClickListener(v -> {
            Toast.makeText(getActivity(),
                            "Closed!!", Toast.LENGTH_SHORT)
                    .show();
            dismiss();
        });

        return bottomSheet;
    }
}
