package com.neu.madcourse.mad_team4_finalproject.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentTrailOverviewBinding
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils

class TrailOverviewFragment(park: Park) : Fragment() {
    /* The Fragment log tag */
    private val LOG_TAG = TrailOverviewFragment::class.java.simpleName

    /* The corresponding Activity context */
    private lateinit var mActivityContext: Context

    /* The Fragment layout binding reference */
    private lateinit var mBinding: FragmentTrailOverviewBinding

    /* The Base Utils reference */
    private lateinit var mBaseUtils: BaseUtils

    /* The Park model reference */
    private val mPark = park

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        mBinding = FragmentTrailOverviewBinding.inflate(layoutInflater, container, false)

        // Set the activity context
        mActivityContext = requireActivity()

        // Instantiate the base utils reference
        mBaseUtils = BaseUtils(mActivityContext)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Set the Park overview information */

        // Set the description
        mBinding.viewOverviewDescription.text = mPark.description
        mBinding.viewOverviewAddress.text = mPark.addressList.get(0).line1 + " " + mPark.addressList.get(0).line2 + mPark.addressList.get(0).line3 + ", " + mPark.addressList.get(0).city + ", " + mPark.addressList.get(0).stateCode + " " + mPark.addressList.get(0).postalCode + " \n Phone: " + mPark.parkContact.phoneNumbersList.get(0).phoneNumber + " \n Email: " + mPark.parkContact.emailAddressesList.get(0).emailAddress
        mBinding.viewOverviewWeather.text = mPark.weatherInfo
        // TODO: Set the length and elevation

        // TODO: Set the static map
        val mapUrl: String = "https://maps.googleapis.com/maps/api/staticmap?center=" + mPark.latitude + "," + mPark.longitude + "&zoom=14&scale=2&size=400x400&maptype=roadmap&key=AIzaSyBG6sBP1e1UXr-PIY06i7PLV9jmdLy8cIg"
        Log.d(LOG_TAG, mapUrl);
        Glide.with(mActivityContext)
            .load(mapUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_downloading)
            .error(R.drawable.ic_error)
            .into(mBinding.viewOverviewMap)
    }
}