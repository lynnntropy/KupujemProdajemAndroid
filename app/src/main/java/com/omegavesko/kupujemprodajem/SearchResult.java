package com.omegavesko.kupujemprodajem;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SearchResult implements Serializable
{
    public String itemName;
    public String itemDesc;

    public String itemURL;

    public transient Bitmap itemThumbnail;

    public String itemPrice;
    public String itemLocation;

    public SearchResult(String itemName, String itemDesc, String itemURL, Bitmap itemThumb, String itemPrice, String itemLoc)
    {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemURL = itemURL;

        this.itemThumbnail = itemThumb;

        this.itemPrice = itemPrice;
        this.itemLocation = itemLoc;
    }

    public String toString()
    {
        return "[ " + itemName + " | " + itemDesc + " | " + itemURL + " ]";
    }
}