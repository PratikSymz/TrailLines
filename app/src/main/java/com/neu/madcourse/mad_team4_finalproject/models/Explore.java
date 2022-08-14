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

    @Override
    public int compareTo(Explore o) {
        return -Double.compare(round(reviewStat.getTotalStars()/reviewStat.getTotalReviewers(), 1),
                round(o.getReviewStat().getTotalStars()/o.getReviewStat().getTotalReviewers(), 1));
    }

    /* Reference: https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place */
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
