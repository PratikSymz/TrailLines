package com.neu.madcourse.mad_team4_finalproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

//        ImageButton closeFilterScreen = (ImageButton) view.findViewById(R.id.filter_close);
        mBinding.filterClose.setOnClickListener(v -> {
            Toast.makeText(getActivity(),
                            "Closed!!", Toast.LENGTH_SHORT)
                    .show();
            dismiss();
        });

        return bottomSheet;
    }
}
