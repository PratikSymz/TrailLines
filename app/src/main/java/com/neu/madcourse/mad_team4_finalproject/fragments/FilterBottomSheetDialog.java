package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.FilterScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.interfaces.ShowFiltersListener;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetBehavior bottomSheetBehavior;
    FilterScreenBinding mBinding;
    SharedPreferences sharedPreferences;
    private final ShowFiltersListener mListener;

    public FilterBottomSheetDialog(ShowFiltersListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.filter_screen, null);
        mBinding = FilterScreenBinding.bind(view);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        sharedPreferences = getActivity().getSharedPreferences("FilterSharedPreferences", Context.MODE_PRIVATE);
        switch (sharedPreferences.getString(Constants.SORT, "")) {
            case Constants.BEST_MATCHED: mBinding.bestMatched.setChecked(true);
            break;

            default: mBinding.topRated.setChecked(true);
        }

        mBinding.ratingSlider.setValues(sharedPreferences.getFloat(Constants.RATING_START, 0),
                sharedPreferences.getFloat(Constants.RATING_END, 5));

        Set<String> preferencesPref = sharedPreferences.getStringSet(Constants.PREFERENCES, new HashSet<>());
        if (preferencesPref.isEmpty()) {
            mBinding.hiking.setChecked(true);
            mBinding.biking.setChecked(true);
            mBinding.canyoneering.setChecked(true);
            mBinding.camping.setChecked(true);
            mBinding.caving.setChecked(true);
            mBinding.boating.setChecked(true);
            mBinding.paddling.setChecked(true);
            mBinding.surfing.setChecked(true);
            mBinding.scuba.setChecked(true);
            mBinding.snorkeling.setChecked(true);
            mBinding.skiing.setChecked(true);
            mBinding.waterskiing.setChecked(true);
            mBinding.fishing.setChecked(true);
            mBinding.climbing.setChecked(true);
        } else {
            if (preferencesPref.contains(Constants.ThingsToDoStrings.HIKING)) {
                mBinding.hiking.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.BIKING)) {
                mBinding.biking.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.CANYONEERING)) {
                mBinding.canyoneering.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.CAMPING)) {
                mBinding.camping.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.CAVING)) {
                mBinding.caving.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.BOATING)) {
                mBinding.boating.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.PADDLING)) {
                mBinding.paddling.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.SURFING)) {
                mBinding.surfing.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.SCUBA_DIVING)) {
                mBinding.scuba.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.SNORKELING)) {
                mBinding.snorkeling.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.SKIING)) {
                mBinding.skiing.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.WATER_SKIING)) {
                mBinding.waterskiing.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.FISHING)) {
                mBinding.fishing.setChecked(true);
            }
            if (preferencesPref.contains(Constants.ThingsToDoStrings.CLIMBING)) {
                mBinding.climbing.setChecked(true);
            }
        }


        mBinding.filterClose.setOnClickListener(v -> {
            dismiss();
        });

        mBinding.showTrails.setOnClickListener(v -> {
            SharedPreferences.Editor myEditor = sharedPreferences.edit();
            //sort
            int index = mBinding.radioGroup.indexOfChild( mBinding.radioGroup.findViewById(mBinding.radioGroup.getCheckedRadioButtonId()));
            RadioButton r = (RadioButton) mBinding.radioGroup.getChildAt(index);
            String sortValue = r.getText().toString();
            myEditor.putString(Constants.SORT, sortValue);
            //rating
            List<Float> ratingSliderValues = mBinding.ratingSlider.getValues();
            myEditor.putFloat(Constants.RATING_START, ratingSliderValues.get(0));
            myEditor.putFloat(Constants.RATING_END, ratingSliderValues.get(1));
            //preferences
            Set<String> selectedPreferences = new HashSet<>();
            if(mBinding.hiking.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.HIKING);
            }
            if(mBinding.caving.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.CAVING);
            }
            if(mBinding.canyoneering.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.CANYONEERING);
            }
            if(mBinding.camping.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.CAMPING);
            }
            if(mBinding.biking.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.BIKING);
            }
            if(mBinding.boating.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.BOATING);
            }
            if(mBinding.fishing.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.FISHING);
            }
            if(mBinding.paddling.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.PADDLING);
            }
            if(mBinding.snorkeling.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.SNORKELING);
            }
            if(mBinding.surfing.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.SURFING);
            }
            if(mBinding.climbing.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.CLIMBING);
            }
            if(mBinding.scuba.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.SCUBA_DIVING);
            }
            if(mBinding.skiing.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.SKIING);
            }
            if(mBinding.waterskiing.isChecked()) {
                selectedPreferences.add(Constants.ThingsToDoStrings.WATER_SKIING);
            }

            myEditor.putStringSet(Constants.PREFERENCES, selectedPreferences);
            myEditor.apply();
            dismiss();
            mListener.showFiltersClicked(true);
        });

        mBinding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEditor = sharedPreferences.edit();
                //sort
                mBinding.bestMatched.setChecked(false);
                mBinding.topRated.setChecked(true);
                myEditor.putString(Constants.SORT, Constants.TOP_RATED);
                //rating
                mBinding.ratingSlider.setValues(0f, 5f);
                myEditor.putFloat(Constants.RATING_START, 0);
                myEditor.putFloat(Constants.RATING_END, 5);
                //preferences
                mBinding.hiking.setChecked(true);
                mBinding.biking.setChecked(true);
                mBinding.camping.setChecked(true);
                mBinding.canyoneering.setChecked(true);
                mBinding.climbing.setChecked(true);
                mBinding.caving.setChecked(true);
                mBinding.boating.setChecked(true);
                mBinding.paddling.setChecked(true);
                mBinding.surfing.setChecked(true);
                mBinding.scuba.setChecked(true);
                mBinding.snorkeling.setChecked(true);
                mBinding.skiing.setChecked(true);
                mBinding.waterskiing.setChecked(true);
                mBinding.fishing.setChecked(true);

                myEditor.putStringSet(Constants.PREFERENCES, new HashSet<>(Arrays.asList(Constants.ThingsToDoStrings.HIKING,
                        Constants.ThingsToDoStrings.BIKING, Constants.ThingsToDoStrings.CAMPING, Constants.ThingsToDoStrings.CANYONEERING,
                        Constants.ThingsToDoStrings.CLIMBING, Constants.ThingsToDoStrings.CAVING, Constants.ThingsToDoStrings.BOATING,
                        Constants.ThingsToDoStrings.PADDLING, Constants.ThingsToDoStrings.SURFING, Constants.ThingsToDoStrings.SCUBA_DIVING,
                        Constants.ThingsToDoStrings.SNORKELING, Constants.ThingsToDoStrings.SKIING, Constants.ThingsToDoStrings.WATER_SKIING,
                        Constants.ThingsToDoStrings.FISHING)));
                myEditor.apply();

                dismiss();
                mListener.showFiltersClicked(false);
            }
        });

        return bottomSheet;
    }
}
