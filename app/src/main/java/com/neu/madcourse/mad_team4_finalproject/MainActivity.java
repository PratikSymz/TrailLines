package com.neu.madcourse.mad_team4_finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

        Button mBottton = findViewById(R.id.button);
        mBottton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showBottomSheetDialog();
                FilterBottomSheetDialog bottomSheet = new FilterBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });


    }

    private void showBottomSheetDialog() {


//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        View sheetView;
//        sheetView = getLayoutInflater().inflate(R.layout.filter_screen,null);
//
//        bottomSheetDialog.setContentView(sheetView);
//
//        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                BottomSheetDialog d = (BottomSheetDialog) dialog;
//
//                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
//                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//        bottomSheetDialog.show();


    }
}