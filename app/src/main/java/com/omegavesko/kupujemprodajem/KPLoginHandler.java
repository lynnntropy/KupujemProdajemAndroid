package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.content.Intent;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//import java.io.BufferedWriter;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.util.HashMap;
import java.util.Map;

public class KPLoginHandler
{
    public static class KPCookies
    {
        public Map<String, String> cookies;

        // empty constructor
        public KPCookies() {}

        public KPCookies(Map<String, String> cookies)
        {
            this.cookies = cookies;
        }

        public String toString()
        {
            return cookies.toString();
        }

    }

//    public static void main(String[] args) throws Exception
//    {
//        System.out.println("Running login method..");
//        KPCookies loginCookies = login("omegavesko@gmail.com", "t357159");
//        System.out.println("Done!");
//
//        System.out.println("Downloading user page...");
//        Document userPage = getPageWithCookies(loginCookies, "https://www.kupujemprodajem.com/user.php?action=edit");
//        System.out.println("Download complete.");
//        System.out.println("User page title is: " + userPage.title());
//        System.out.println("User full name is: " + userPage.select("input").get(3).attr("value"));
//
//        // print downloaded page to file for debug purposes
//        BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("userpage.html"), "UTF-8"));
//        htmlWriter.write(userPage.toString());
//    }

    public static KPCookies login(String email, String password, Context context)
    {
        String loginURL = "https://www.kupujemprodajem.com/user.php?action=login";

        KPCookies returnCookies = new KPCookies();

        try
        {
            System.out.println("Sending POST request...");

            Connection.Response res;
            res = Jsoup.connect(loginURL)
                    .referrer("https://www.kupujemprodajem.com/user.php?action=login")
                    .data("submit[login]", "1", "action", "login", "return_url", "", "data[email]", email, "data[password]", password, "data[remember]", "yes", "submit[login]", "Ulogujte+se")
                    .method(Connection.Method.POST)
                    .execute();

            System.out.println("POST done.");
            System.out.println("POST response: " + res.statusMessage());

            if (!res.statusMessage().equals("OK"))
            {
                Intent loginIntent = new Intent(context, LoginActivity.class);
                context.startActivity(loginIntent);
            }

            returnCookies.cookies = res.cookies();

            System.out.println("Cookies returned from KP:\n" + returnCookies.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnCookies;
    }

    public static Document getPageWithCookies(KPCookies cookies, String url)
    {
        try
        {
            Document page = Jsoup.connect(url)
                    .cookies(cookies.cookies)
                    .timeout(0)
                    .get();

            return page;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new Document("");
    }
}
