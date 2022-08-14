package com.neu.madcourse.mad_team4_finalproject.models;

import com.google.gson.annotations.SerializedName;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;

import java.io.Serializable;
import java.util.Comparator;


public class Explore implements Serializable, Comparable<Explore> {
    @SerializedName("id")
    private final Park park;
    private final ReviewStat reviewStat;


    public Explore(Park park, ReviewStat reviewStat) {
        this.park = park;
        this.reviewStat = reviewStat;
    }

    public Park getPark() {
        return park;
    }

    public ReviewStat getReviewStat() {
        return reviewStat;
    }

//
//    @Override
//    public int compare(Explore o1, Explore o2) {
//        return Double.compare(o1.getReviewStat().getTotalStars(),
//                o2.getReviewStat().getTotalStars());
//    }

    @Override
    public int compareTo(Explore o) {
        return Double.compare(reviewStat.getTotalStars(),
                o.getReviewStat().getTotalStars());
    }
}
