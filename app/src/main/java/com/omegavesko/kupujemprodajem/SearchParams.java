package com.omegavesko.kupujemprodajem;

import java.io.Serializable;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class SearchParams implements Serializable
{
    public String tipOglasaID;
    public String categoryID;
    public String locationID;
    public String stanjeID;
    public String sortID;

    public String searchTerms;

    public SearchParams(String tipOglasaID, String categoryID, String locationID, String stanjeID, String sortID, String searchTerms)
    {
        this.tipOglasaID = tipOglasaID;
        this.categoryID = categoryID;
        this.locationID = locationID;
        this.stanjeID = stanjeID;
        this.sortID = sortID;

        this.searchTerms = searchTerms;
    }

    public SearchParams()
    {
        this.tipOglasaID = "";
        this.categoryID = "";
        this.locationID = "";
        this.stanjeID = "";
        this.sortID = "";

        this.searchTerms = "";
    }

    public String toString()
    {
        return "[ " + tipOglasaID + " | " + categoryID + " | " + locationID+ " | " + stanjeID + " | " + sortID + " | " + searchTerms + " ]";
    }
}
