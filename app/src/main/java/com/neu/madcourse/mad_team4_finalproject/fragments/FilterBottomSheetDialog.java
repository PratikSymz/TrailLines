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
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterBottomSheetDialog extends BottomSheetDialogFragment {
    BottomSheetBehavior bottomSheetBehavior;
    FilterScreenBinding mBinding;
    SharedPreferences sharedPreferences;

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

            case Constants.CLOSEST: mBinding.closest.setChecked(true);
            break;

            default: mBinding.topRated.setChecked(true);
        }

        mBinding.lengthSlider.setValues(sharedPreferences.getFloat(Constants.LENGTH_START, 0),
                sharedPreferences.getFloat(Constants.LENGTH_END, 80));

        mBinding.elevationSlider.setValues(sharedPreferences.getFloat(Constants.ELEVATION_START, 0),
                sharedPreferences.getFloat(Constants.ELEVATION_END, 1500));

        Set<String> difficultyPref = sharedPreferences.getStringSet(Constants.DIFFICULTY, new HashSet<>());
        if (difficultyPref.isEmpty()) {
            mBinding.easyCheckBox.setChecked(true);
            mBinding.medCheckBox.setChecked(true);
            mBinding.hardCheckBox.setChecked(true);
        } else {
            if (difficultyPref.contains(Constants.EASY)) {
                mBinding.easyCheckBox.setChecked(true);
            }
            if (difficultyPref.contains(Constants.MEDIUM)) {
                mBinding.medCheckBox.setChecked(true);
            }
            if (difficultyPref.contains(Constants.HARD)) {
                mBinding.hardCheckBox.setChecked(true);
            }
        }

        mBinding.ratingSlider.setValues(sharedPreferences.getFloat(Constants.RATING_START, 0),
                sharedPreferences.getFloat(Constants.RATING_END, 5));

        Set<String> preferencesPref = sharedPreferences.getStringSet(Constants.PREFERENCES, new HashSet<>());
        if (preferencesPref.isEmpty()) {
            mBinding.dog.setChecked(true);
            mBinding.kid.setChecked(true);
            mBinding.wheelchair.setChecked(true);
            mBinding.camping.setChecked(true);
            mBinding.biking.setChecked(true);
        } else {
            if (preferencesPref.contains(Constants.DOG)) {
                mBinding.dog.setChecked(true);
            }
            if (preferencesPref.contains(Constants.KID)) {
                mBinding.kid.setChecked(true);
            }
            if (preferencesPref.contains(Constants.WHEELCHAIR)) {
                mBinding.wheelchair.setChecked(true);
            }
            if (preferencesPref.contains(Constants.CAMPING)) {
                mBinding.camping.setChecked(true);
            }
            if (preferencesPref.contains(Constants.BIKING)) {
                mBinding.biking.setChecked(true);
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
            //length
            List<Float> lengthSliderValues = mBinding.lengthSlider.getValues();
            myEditor.putFloat(Constants.LENGTH_START, lengthSliderValues.get(0));
            myEditor.putFloat(Constants.LENGTH_END, lengthSliderValues.get(1));
            //elevation
            List<Float> elevationSliderValues = mBinding.elevationSlider.getValues();
            myEditor.putFloat(Constants.ELEVATION_START, elevationSliderValues.get(0));
            myEditor.putFloat(Constants.ELEVATION_END, elevationSliderValues.get(1));
            //difficulty
            Set<String> selectedDifficulty = new HashSet<>();
            if(mBinding.easyCheckBox.isChecked()) {
                selectedDifficulty.add(Constants.EASY);
            }
            if(mBinding.medCheckBox.isChecked()) {
                selectedDifficulty.add(Constants.MEDIUM);
            }
            if(mBinding.hardCheckBox.isChecked()) {
                selectedDifficulty.add(Constants.HARD);
            }
            sharedPreferences.getStringSet(Constants.DIFFICULTY, new HashSet<>());
            myEditor.putStringSet(Constants.DIFFICULTY, selectedDifficulty);
            //rating
            List<Float> ratingSliderValues = mBinding.ratingSlider.getValues();
            myEditor.putFloat(Constants.RATING_START, ratingSliderValues.get(0));
            myEditor.putFloat(Constants.RATING_END, ratingSliderValues.get(1));
            //preferences
            Set<String> selectedPreferences = new HashSet<>();
            if(mBinding.dog.isChecked()) {
                selectedPreferences.add(Constants.DOG);
            }
            if(mBinding.kid.isChecked()) {
                selectedPreferences.add(Constants.KID);
            }
            if(mBinding.wheelchair.isChecked()) {
                selectedPreferences.add(Constants.WHEELCHAIR);
            }
            if(mBinding.camping.isChecked()) {
                selectedPreferences.add(Constants.CAMPING);
            }
            if(mBinding.biking.isChecked()) {
                selectedPreferences.add(Constants.BIKING);
            }
            myEditor.putStringSet(Constants.PREFERENCES, selectedPreferences);
            myEditor.apply();
            dismiss();
        });

        mBinding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEditor = sharedPreferences.edit();
                //sort
                mBinding.bestMatched.setChecked(false);
                mBinding.closest.setChecked(false);
                mBinding.topRated.setChecked(true);
                myEditor.putString(Constants.SORT, Constants.TOP_RATED);
                //length
                mBinding.lengthSlider.setValues(0f, 80f);
                myEditor.putFloat(Constants.LENGTH_START, 0);
                myEditor.putFloat(Constants.LENGTH_END, 80);
                //elevation
                mBinding.lengthSlider.setValues(0f, 1500f);
                myEditor.putFloat(Constants.ELEVATION_START, 0);
                myEditor.putFloat(Constants.ELEVATION_END, 1500);
                //difficulty
                mBinding.easyCheckBox.setChecked(true);
                mBinding.medCheckBox.setChecked(true);
                mBinding.hardCheckBox.setChecked(true);
                myEditor.putStringSet(Constants.DIFFICULTY, new HashSet<>(Arrays.asList(Constants.EASY, Constants.MEDIUM, Constants.HARD)));
                //rating
                mBinding.lengthSlider.setValues(0f, 5f);
                myEditor.putFloat(Constants.RATING_START, 0);
                myEditor.putFloat(Constants.RATING_END, 5);
                //preferences
                mBinding.dog.setChecked(true);
                mBinding.kid.setChecked(true);
                mBinding.wheelchair.setChecked(true);
                mBinding.biking.setChecked(true);
                mBinding.camping.setChecked(true);
                myEditor.putStringSet(Constants.PREFERENCES, new HashSet<>(Arrays.asList(Constants.DOG, Constants.KID, Constants.WHEELCHAIR, Constants.CAMPING, Constants.BIKING)));
                myEditor.apply();

                dismiss();
            }
        });

        return bottomSheet;
    }
}
