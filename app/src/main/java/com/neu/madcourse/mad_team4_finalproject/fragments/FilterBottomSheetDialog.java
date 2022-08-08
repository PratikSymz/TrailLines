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
        switch (sharedPreferences.getString("sort", "")) {
            case "Best Matched": mBinding.bestMatched.setChecked(true);
            break;

            case "Closest": mBinding.closest.setChecked(true);
            break;

            default: mBinding.topRated.setChecked(true);
        }

        mBinding.lengthSlider.setValues(sharedPreferences.getFloat("lengthStart", 0),
                sharedPreferences.getFloat("lengthEnd", 80));

        mBinding.elevationSlider.setValues(sharedPreferences.getFloat("elevationStart", 0),
                sharedPreferences.getFloat("elevationEnd", 1500));

        Set<String> difficultyPref = sharedPreferences.getStringSet("difficulty", new HashSet<>());
        if (difficultyPref.isEmpty()) {
            mBinding.easyCheckBox.setChecked(true);
            mBinding.medCheckBox.setChecked(true);
            mBinding.hardCheckBox.setChecked(true);
        } else {
            if (difficultyPref.contains("Easy")) {
                mBinding.easyCheckBox.setChecked(true);
            }
            if (difficultyPref.contains("Medium")) {
                mBinding.medCheckBox.setChecked(true);
            }
            if (difficultyPref.contains("Hard")) {
                mBinding.hardCheckBox.setChecked(true);
            }
        }

        mBinding.ratingSlider.setValues(sharedPreferences.getFloat("ratingStart", 0),
                sharedPreferences.getFloat("ratingEnd", 5));

        Set<String> preferencesPref = sharedPreferences.getStringSet("preferences", new HashSet<>());
        if (preferencesPref.isEmpty()) {
            mBinding.dog.setChecked(true);
            mBinding.kid.setChecked(true);
            mBinding.wheelchair.setChecked(true);
            mBinding.camping.setChecked(true);
            mBinding.biking.setChecked(true);
        } else {
            if (preferencesPref.contains("Dog")) {
                mBinding.dog.setChecked(true);
            }
            if (preferencesPref.contains("Kid")) {
                mBinding.kid.setChecked(true);
            }
            if (preferencesPref.contains("Wheelchair")) {
                mBinding.wheelchair.setChecked(true);
            }
            if (preferencesPref.contains("Camping")) {
                mBinding.camping.setChecked(true);
            }
            if (preferencesPref.contains("Biking")) {
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
            myEditor.putString("sort", sortValue);
            //length
            List<Float> lengthSliderValues = mBinding.lengthSlider.getValues();
            myEditor.putFloat("lengthStart", lengthSliderValues.get(0));
            myEditor.putFloat("lengthEnd", lengthSliderValues.get(1));
            //elevation
            List<Float> elevationSliderValues = mBinding.elevationSlider.getValues();
            myEditor.putFloat("elevationStart", elevationSliderValues.get(0));
            myEditor.putFloat("elevationEnd", elevationSliderValues.get(1));
            //difficulty
            Set<String> selectedDifficulty = new HashSet<>();
            if(mBinding.easyCheckBox.isChecked()) {
                selectedDifficulty.add("Easy");
            }
            if(mBinding.medCheckBox.isChecked()) {
                selectedDifficulty.add("Medium");
            }
            if(mBinding.hardCheckBox.isChecked()) {
                selectedDifficulty.add("Hard");
            }
            sharedPreferences.getStringSet("difficulty", new HashSet<>());
            myEditor.putStringSet("difficulty", selectedDifficulty);
            //rating
            List<Float> ratingSliderValues = mBinding.ratingSlider.getValues();
            myEditor.putFloat("ratingStart", ratingSliderValues.get(0));
            myEditor.putFloat("ratingEnd", ratingSliderValues.get(1));
            //preferences
            Set<String> selectedPreferences = new HashSet<>();
            if(mBinding.dog.isChecked()) {
                selectedPreferences.add("Dog");
            }
            if(mBinding.kid.isChecked()) {
                selectedPreferences.add("Kid");
            }
            if(mBinding.wheelchair.isChecked()) {
                selectedPreferences.add("Wheelchair");
            }
            if(mBinding.camping.isChecked()) {
                selectedPreferences.add("Camping");
            }
            if(mBinding.biking.isChecked()) {
                selectedPreferences.add("Biking");
            }
            myEditor.putStringSet("preferences", selectedPreferences);
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
                myEditor.putString("sort", "Top Rated");
                //length
                mBinding.lengthSlider.setValues(0f, 80f);
                myEditor.putFloat("lengthStart", 0);
                myEditor.putFloat("lengthEnd", 80);
                //elevation
                mBinding.lengthSlider.setValues(0f, 1500f);
                myEditor.putFloat("elevationStart", 0);
                myEditor.putFloat("elevationEnd", 1500);
                //difficulty
                mBinding.easyCheckBox.setChecked(true);
                mBinding.medCheckBox.setChecked(true);
                mBinding.hardCheckBox.setChecked(true);
                myEditor.putStringSet("difficulty", new HashSet<>(Arrays.asList("Easy", "Medium", "Hard")));
                //rating
                mBinding.lengthSlider.setValues(0f, 5f);
                myEditor.putFloat("ratingStart", 0);
                myEditor.putFloat("ratingEnd", 5);
                //preferences
                mBinding.dog.setChecked(true);
                mBinding.kid.setChecked(true);
                mBinding.wheelchair.setChecked(true);
                mBinding.biking.setChecked(true);
                mBinding.camping.setChecked(true);
                myEditor.putStringSet("preferences", new HashSet<>(Arrays.asList("Dog", "Kid", "Wheelchair", "Camping", "Biking")));
                myEditor.apply();

                dismiss();
            }
        });

        return bottomSheet;
    }
}
