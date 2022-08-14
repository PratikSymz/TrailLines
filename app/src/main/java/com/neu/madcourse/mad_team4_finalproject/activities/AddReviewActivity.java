package com.neu.madcourse.mad_team4_finalproject.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.GenericAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityAddReviewBinding;
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemSelectedImageBinding;
import com.neu.madcourse.mad_team4_finalproject.models.ReviewStat;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AddReviewActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = AddReviewActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Fragment layout view binding reference */
    private ActivityAddReviewBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Firebase Database reference -> "reviews" */
    private DatabaseReference mReviewsDatabaseRef;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Firebase Storage reference */
    private StorageReference mFirebaseStorage;

    /* The Recycler view adapter reference */
    private GenericAdapter<Uri> mAdapter;

    /* The Review images selected list reference */
    private Set<Uri> mSelectedImageList = new HashSet<>();

    /* The Review images selected url list reference */
    private Set<String> mSelectedImageUrlList = new HashSet<>();

    /* The Intent Park model reference */
    private Park mPark;

    /* The Trail ID reference */
    // private final String mTrailID = "102";

    /* The Recommendation status boolean reference */
    private Boolean mRecommendationStatus = false;

    /* The "Recommended" and "Not Recommended" highlight status */
    private boolean mRHighlighted = false, mNRHighlighted = false;

    /* The Image picker intent request code */
    private final int REQUEST_CODE_SELECT_MULTIPLE = 101;

    /* The External storage access permission request code */
    private final int REQUEST_CODE_ACCESS_STORAGE = 102;

    private final int REQUEST_IMAGE_CAPTURE = 103;

    /* name of the file saved by the camera */
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Inflate the layout
        mBinding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        // Set the root view
        setContentView(mBinding.getRoot());

        // Retrieve the Park information from the intent
        mPark = (Park) getIntent().getSerializableExtra("park_details");

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Instantiate the firebase database reference -> "reviews"
        mReviewsDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.ReviewKeys.KEY_TLO);

        // Instantiate the firebase storage reference
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();

        /* Set the selected images recycler view parameters */
        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.recyclerViewSelectedImages.setLayoutManager(layoutManager);

        // Instantiate the adapter class
        mAdapter = new GenericAdapter<>(mContext, new ArrayList<>(mSelectedImageList),
                ItemSelectedImageBinding.inflate(getLayoutInflater()));

        // Set the adapter on the recycler view
        mBinding.recyclerViewSelectedImages.setAdapter(mAdapter);

        /* Set the "add images" button onClick action */
        // Alert Dialog for the permission for to access Camera and gallery
        mBinding.viewButtonAddImages.setOnClickListener(view -> {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(mContext);
            myAlertDialog.setTitle("Upload Pictures Option");
            myAlertDialog.setMessage("How do you want to set your picture?");
            myAlertDialog.setPositiveButton("Gallery",
                    (dialogInterface, position) -> {
                        // Open the image picker screen
                        openImagePicker();
                    });

            myAlertDialog.setNegativeButton("Camera",
                    (dialogInterface, position) -> {
                        // Open the camera app and extract image uri
                        dispatchTakePictureIntent();
                    });

            myAlertDialog.show();
        });

        /* Set the park name */
        mBinding.viewReviewTrailName.setText(mPark.getFullName());

        /* Setup the "Not recommended" button */
        setupNotRecommendedButton();

        /* Setup the "Recommended" button onClick action */
        mBinding.viewButtonRecommended.getRoot().setOnClickListener(view -> {
            // Deselection
            if (mRecommendationStatus && mRHighlighted) {
                // Set the recommendation status as "false"
                mRecommendationStatus = false;
                // Set the highlight status as false
                mRHighlighted = false;
                // Unhighlight the recommended button
                unhighlightButton(mBinding.viewButtonRecommended.labelRecommendStatus.getText().toString());
            } else {
                if (!mRecommendationStatus && !mNRHighlighted) {
                    // Set the recommendation status as "true"
                    mRecommendationStatus = true;
                    // Highlight the recommendation button
                    highlightButton(mBinding.viewButtonRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as true
                    mRHighlighted = true;
                } else if (!mRecommendationStatus) {
                    // Set the recommendation status as "true"
                    mRecommendationStatus = true;
                    // Highlight the recommendation button
                    highlightButton(mBinding.viewButtonRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as true
                    mRHighlighted = true;
                    // Unhighlight the not recommendation button
                    unhighlightButton(mBinding.viewNotRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as false
                    mNRHighlighted = false;
                }
            }
        });

        /* Setup the "Not recommended" button onClick action */
        mBinding.viewNotRecommended.getRoot().setOnClickListener(view -> {
            // Deselection
            if (!mRecommendationStatus && mNRHighlighted) {
                // Set the recommendation status as "false"
                mRecommendationStatus = false;
                // Set the highlight status as false
                mNRHighlighted = false;
                // Unhighlight the not recommended button
                unhighlightButton(mBinding.viewNotRecommended.labelRecommendStatus.getText().toString());
            } else {
                if (mRecommendationStatus && mRHighlighted && !mNRHighlighted) {
                    // Set the recommendation status as "false"
                    mRecommendationStatus = false;
                    // Highlight the not recommendation button
                    highlightButton(mBinding.viewNotRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as true
                    mNRHighlighted = true;
                    // Unhighlight the recommendation button
                    unhighlightButton(mBinding.viewButtonRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as false
                    mRHighlighted = false;
                } else if (!mRecommendationStatus && !mNRHighlighted) {
                    // Keep the recommendation status as "false"
                    // Highlight the not recommendation button
                    highlightButton(mBinding.viewNotRecommended.labelRecommendStatus.getText().toString());
                    // Set the highlight status as true
                    mNRHighlighted = true;
                }
            }
        });

        /* Set the "submit" review button onClick action */
        mBinding.viewSubmitReviewButton.setOnClickListener(view -> {
            // Extract the rating
            float rating = mBinding.viewAddRating.getRating();
            // Extract the review title
            String title = Objects.requireNonNull(mBinding.viewInputReviewTitle.getText()).toString().trim();
            // Extract the review message
            String message = Objects.requireNonNull(mBinding.viewInputReviewBody.getText()).toString().trim();
            // Extract the recommendation status
            if (!mRHighlighted && !mNRHighlighted) {
                mRecommendationStatus = null;
            } else {
                mRecommendationStatus = mRHighlighted;
            }

            // Verify information as non-null
            if (rating > 0 && !mBaseUtils.isEmpty(title)) {
                // Iterate through the image URI's and add them to the Firebase Storage
                if (!mSelectedImageList.isEmpty()) {
                    StorageReference reviewImageRef = mFirebaseStorage.child(String.format("images/%s/reviews", mFirebaseUser.getUid()));
                    // Clear the selected image url list
                    mSelectedImageUrlList.clear();

                    // Iterate
                    for (Uri imageUri : mSelectedImageList) {
                        // TODO: Fix file sub-directories
                        Log.d(LOG_TAG, imageUri.getPath());
                        // Extract the file name
                        String fileName = String.format("%s.jpg", imageUri.getPath());
                        // Add the image with the file name as the root
                        reviewImageRef.child(fileName).putFile(imageUri).addOnSuccessListener(imageSnapshot -> {
                            reviewImageRef.child(fileName).getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                // Add the url to the selected image url list
                                mSelectedImageUrlList.add(downloadUri.toString());

                                // Once all images have been uploaded, create the review on firebase
                                if (mSelectedImageUrlList.size() == mSelectedImageList.size()) {
                                    submitReview(rating, title, message, mRecommendationStatus, new ArrayList<>(mSelectedImageUrlList));
                                }
                            });
                        });
                    }
                } else {
                    submitReview(rating, title, message, mRecommendationStatus, new ArrayList<>());
                }
            }

            // IF "rating" is zero
            if (rating == 0) {
                mBaseUtils.showToast("Please add a rating before submitting!", Toast.LENGTH_SHORT);
            }

            // IF "title" is empty
            if (mBaseUtils.isEmpty(title)) {
                mBinding.viewInputReviewTitle.setError("Empty review title");
            }
        });
    }

    private void submitReview(float rating, String title, String message,
                              Boolean recommendationStatus, List<String> selectedImageUrlList) {
        // Create a data map
        Map<String, Object> dataMap = new HashMap<>();
        // Add the review rating
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_RATING, rating);
        // Add the review title
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_TITLE, title);
        // Add the review body
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_MESSAGE, message);
        // Add the review timestamp
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_TIMESTAMP, ServerValue.TIMESTAMP);
        // Add the review recommend status
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_RECOMMEND_STATUS, recommendationStatus);
        // Add the review trail ID
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_TRAIL_ID, mPark.getParkID());
        // Add the review image IDs
        dataMap.put(Constants.ReviewKeys.ReviewMessages.KEY_IMAGES, selectedImageUrlList);

        // Update the "reviews" messages reference for the given Trail ID
        mReviewsDatabaseRef.child(mPark.getParkID())
                .child(Constants.ReviewKeys.ReviewMessages.KEY_TLO)
                // for the logged in user
                .child(mFirebaseUser.getUid())
                .setValue(dataMap)
                .addOnCompleteListener(reviewTask -> {
                    if (reviewTask.isSuccessful()) {
                        mBaseUtils.showToast("Added review successfully!", Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        mBaseUtils.showToast("Failed adding review!", Toast.LENGTH_SHORT);
                    }
                });

        /* Load the "reviews" stats reference for the given Trail ID */
        mReviewsDatabaseRef.child(mPark.getParkID())
                .child(Constants.ReviewKeys.ReviewStats.KEY_TLO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot statsSnapshot) {
                        // Extract the review stats
                        ReviewStat reviewStats = statsSnapshot.getValue(ReviewStat.class);
                        if (reviewStats == null) reviewStats = new ReviewStat();

                        // Create a stats map
                        Map<String, Object> statsMap = new HashMap<>();
                        // Add the total review count
                        statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_REVIEWERS, reviewStats.getTotalReviewers() + 1);
                        // Add the total stars count
                        statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_TOTAL_STARS, reviewStats.getTotalStars() + rating);
                        // Add the excellence rating count
                        // TERRIBLE
                        if (rating > 0 && rating <= 1.0) {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_TERRIBLE, reviewStats.getNumTerrible() + 1);
                        } else {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_TERRIBLE, reviewStats.getNumTerrible());
                        }

                        // POOR
                        if (rating > 1.0 && rating <= 2.0) {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_POOR, reviewStats.getNumPoor() + 1);
                        } else {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_POOR, reviewStats.getNumPoor());
                        }

                        // AVERAGE
                        if (rating > 2.0 && rating <= 3.0) {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_AVERAGE, reviewStats.getNumAverage() + 1);
                        } else {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_AVERAGE, reviewStats.getNumAverage());
                        }

                        // GREAT
                        if (rating > 3.0 && rating <= 4.0) {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_GREAT, reviewStats.getNumGreat() + 1);
                        } else {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_GREAT, reviewStats.getNumGreat());
                        }

                        // EXCELLENT
                        if (rating > 4.0 && rating <= 5.0) {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_EXCELLENT, reviewStats.getNumExcellent() + 1);
                        } else {
                            statsMap.put(Constants.ReviewKeys.ReviewStats.KEY_NUM_EXCELLENT, reviewStats.getNumExcellent());
                        }

                        // Update the "reviews" stats reference for the given Trail ID
                        mReviewsDatabaseRef.child(mPark.getParkID())
                                .child(Constants.ReviewKeys.ReviewStats.KEY_TLO)
                                .setValue(statsMap)
                                .addOnCompleteListener(statsTask -> {
                                    if (statsTask.isSuccessful()) {
                                        mBaseUtils.showToast("Updated stats successfully!", Toast.LENGTH_SHORT);
                                        finish();
                                    } else {
                                        mBaseUtils.showToast("Failed updating review stats!", Toast.LENGTH_SHORT);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mBaseUtils.showToast(error.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * Override method to select multiple images from the user's phone gallery for the trail reviews
     * This method also will ask user permission to access local image storage for multi-image selection
     */
    public void openImagePicker() {
        // Permission already granted
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            initiateSelectorIntent();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ACCESS_STORAGE
            );
        }
    }

    /* Override method to check and request permissions */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACCESS_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateSelectorIntent();
            } else {
                mBaseUtils.showToast(getString(R.string.permission_required), Toast.LENGTH_SHORT);
            }
        }
    }

    /* Override method to retrieve selected image uri's */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check the intent request code
        if (requestCode == REQUEST_CODE_SELECT_MULTIPLE && resultCode == RESULT_OK && data != null) {
            // Clear the selected image list
            // mSelectedImageList.clear();

            if (data.getClipData() != null) {
                // Iterate and extract the images from the "data"
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    // Extract the image uri
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    // Add uri to the adapter list
                    mSelectedImageList.add(imageUri);
                }
            } else {
                // Get the single selected image
                Uri imageUri = data.getData();
                // Add uri to the adapter list
                mSelectedImageList.add(imageUri);
            }

            // Update the recycler view adapter
            mAdapter.updateDataList(new ArrayList<>(mSelectedImageList));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Get the single selected image
        }
    }

    /* Helper method to initiate the multiple image selector intent */
    private void initiateSelectorIntent() {
        // Initialize the intent
        Intent intent = new Intent();
        // Setting the intent content type
        intent.setType("image/*");
        // Set multiple image selection flags
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_SELECT_MULTIPLE);
    }

    /* Helper method to launch camera intent */
    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error handling occurred while creating the File
                mBaseUtils.showToast("Something went wrong!", Toast.LENGTH_SHORT);
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.neu.madcourse.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                // Add uri to the adapter list
                mSelectedImageList.add(photoURI);
                // Update the recycler view adapter
                mAdapter.updateDataList(new ArrayList<>(mSelectedImageList));
            }
        }
    }

    /* Helper method to save image from the camera on the device */
    private File createImageFile() throws IOException {
        // Create an image file name
        // timestamp made to make every file unique and dated
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory of where the file goes */
        );

        // Save file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Helper method to highlight the views of the recommended and not recommended buttons (mark as selected)
     *
     * @param buttonText The recommendation button title
     */
    private void highlightButton(String buttonText) {
        if (buttonText.equals("Recommended")) {
            // Extract the background drawable
            Drawable drawable = mBinding.viewButtonRecommended.getRoot().getBackground();
            // Wrap the drawable
            drawable = DrawableCompat.wrap(drawable);
            // Set the drawable properties
            DrawableCompat.setTint(drawable,
                    ContextCompat.getColor(getApplicationContext(), R.color.blue)
            );
            // Set the updated background
            mBinding.viewButtonRecommended.getRoot().setBackground(drawable);

            // Set the recommended text and icon color as white
            mBinding.viewButtonRecommended.labelRecommendStatus.setTextColor(Color.WHITE);
            DrawableCompat.setTint(
                    mBinding.viewButtonRecommended.iconRecommendStatus.getDrawable(),
                    ContextCompat.getColor(getApplicationContext(), R.color.white)
            );
        } else {
            // Extract the background drawable
            Drawable drawable = mBinding.viewNotRecommended.getRoot().getBackground();
            // Wrap the drawable
            drawable = DrawableCompat.wrap(drawable);
            // Set the drawable properties
            DrawableCompat.setTint(drawable,
                    ContextCompat.getColor(getApplicationContext(), R.color.red)
            );
            // Set the updated background
            mBinding.viewNotRecommended.getRoot().setBackground(drawable);

            // Set the recommended text and icon color as white
            mBinding.viewNotRecommended.labelRecommendStatus.setTextColor(Color.WHITE);
            DrawableCompat.setTint(
                    mBinding.viewNotRecommended.iconRecommendStatus.getDrawable(),
                    ContextCompat.getColor(getApplicationContext(), R.color.white)
            );
        }
    }

    /**
     * Helper method to unhighlight the views of the recommended and not recommended buttons (mark as selected)
     *
     * @param buttonText The recommendation button title
     */
    private void unhighlightButton(String buttonText) {
        if (buttonText.equals("Recommended")) {
            // Extract the background drawable
            Drawable drawable = mBinding.viewButtonRecommended.getRoot().getBackground();
            // Wrap the drawable
            drawable = DrawableCompat.wrap(drawable);
            // Set the drawable properties
            DrawableCompat.setTint(drawable,
                    ContextCompat.getColor(getApplicationContext(), R.color.grey_tint)
            );

            // Set the updated background
            mBinding.viewButtonRecommended.getRoot().setBackground(drawable);

            // Set the recommended text and icon color as black
            mBinding.viewButtonRecommended.labelRecommendStatus.setTextColor(Color.BLACK);
            DrawableCompat.setTint(
                    mBinding.viewButtonRecommended.iconRecommendStatus.getDrawable(),
                    ContextCompat.getColor(getApplicationContext(), R.color.black)
            );
        } else {
            // Extract the background drawable
            Drawable drawable = mBinding.viewNotRecommended.getRoot().getBackground();
            // Wrap the drawable
            drawable = DrawableCompat.wrap(drawable);
            // Set the drawable properties
            DrawableCompat.setTint(drawable,
                    ContextCompat.getColor(getApplicationContext(), R.color.grey_tint)
            );
            // Set the updated background
            mBinding.viewNotRecommended.getRoot().setBackground(drawable);

            // Set the recommended text and icon color as white
            mBinding.viewNotRecommended.labelRecommendStatus.setTextColor(Color.BLACK);
            DrawableCompat.setTint(
                    mBinding.viewNotRecommended.iconRecommendStatus.getDrawable(),
                    ContextCompat.getColor(getApplicationContext(), R.color.black)
            );
        }
    }

    /* Helper method to set up the "Not Recommended" button on the activity view */
    private void setupNotRecommendedButton() {
        mBinding.viewNotRecommended.labelRecommendStatus.setText(R.string.not_recommended);
        mBinding.viewNotRecommended.iconRecommendStatus.setImageResource(R.drawable.ic_thumb_down);
    }
}