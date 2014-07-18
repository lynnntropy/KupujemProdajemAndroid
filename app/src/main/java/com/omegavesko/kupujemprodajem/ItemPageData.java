package com.omegavesko.kupujemprodajem;

import android.content.ClipData;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by omega_000 on 7/18/2014.
 */
public class ItemPageData
{
    public String title;
    public String price;
    public String viewCount;

    public String description;

    public List<Bitmap> photos;

    public String memberName;
    public String memberYesVotes;
    public String memberNoVotes;
    public String memberLocation;
    public Bitmap memberPhoneNumber;

    public ItemPageData() {} // empty constructor
}
