package com.neu.madcourse.mad_team4_finalproject.models_nps;

import java.io.Serializable;

public class Image implements Serializable {
    private String credit;
    private String altText;
    private String title;
    private String caption;
    private String url;

    public String getCredit() {
        return credit;
    }

    public String getAltText() {
        return altText;
    }

    public String getTitle() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public String getUrl() {
        return url;
    }
}
