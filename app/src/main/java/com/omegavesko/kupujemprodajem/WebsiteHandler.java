package com.omegavesko.kupujemprodajem;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by omega_000 on 7/15/2014.
 */
public class WebsiteHandler
{
    public final static String WEBSITE_URL = "http://www.kupujemprodajem.com/";
    public final static String LOG_TAG = "WebsiteHandler";

    public static class Category
    {
        public String nameString;
        public String idString;

        public Category(String name, String id)
        {
            this.nameString = name;
            this.idString = id;
        }
    }

    public static class Location
    {
        public String nameString;
        public String idString;

        public Location(String name, String id)
        {
            this.nameString = name;
            this.idString = id;
        }
    }

    public static class GenericID
    {
        public String nameString;
        public String idString;

        public GenericID(String name, String id)
        {
            this.nameString = name;
            this.idString = id;
        }
    }

    public static List<Category> scrapeCategories()
    {
        List<Category> categoryList = new ArrayList<Category>();

        try
        {
            Document websiteHome = Jsoup.connect(WEBSITE_URL).get();
            String websiteSource = websiteHome.toString();

            String categoryRegex = "\\{\"category_id\":\"([0-9]*)\",\"name\":\"(.*?)\"\\}";

            Pattern pattern = Pattern.compile(categoryRegex);
            Matcher matcher = pattern.matcher(websiteSource);

            while (matcher.find())
            {
                Category currentCategory = new Category(fixEscapeCodes(matcher.group(2)), matcher.group(1));
                categoryList.add(currentCategory);
            }


        }
        catch (Exception e) { Log.e(LOG_TAG, e.toString()); }

        return categoryList;
    }

    public static List<Location> scrapeLocations()
    {
        List<Location> locationList = new ArrayList<Location>();

        try
        {
            Document websiteHome = Jsoup.connect(WEBSITE_URL).get();
            String websiteSource = websiteHome.toString();

            String categoryRegex = "\\{\"location_id\":\"([0-9]*)\",\"name\":\"(.*?)\"\\}";

            Pattern pattern = Pattern.compile(categoryRegex);
            Matcher matcher = pattern.matcher(websiteSource);

            while (matcher.find())
            {
                Location currentLocation = new Location(fixEscapeCodes(matcher.group(2)), matcher.group(1));
                locationList.add(currentLocation);
            }
        }
        catch (Exception e) { Log.e(LOG_TAG, e.toString()); }

        return locationList;
    }

    public static String fixEscapeCodes(String input)
    {
        String s = input;
        StringBuffer buf = new StringBuffer();
        Matcher m = Pattern.compile("\\\\u([0-9A-Fa-f]{4})").matcher(s);
        while (m.find()) {
            try {
                int cp = Integer.parseInt(m.group(1), 16);
                m.appendReplacement(buf, "");
                buf.appendCodePoint(cp);
            } catch (NumberFormatException e) {
            }
        }
        m.appendTail(buf);
        s = buf.toString();

        return s;
    }
}
