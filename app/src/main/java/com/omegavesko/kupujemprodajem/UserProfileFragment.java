package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by omega_000 on 7/23/2014.
 */
public class UserProfileFragment extends Fragment
{
    class LoginTask extends AsyncTask<Void, Void, KPLoginHandler.KPCookies>
    {
        @Override
        protected KPLoginHandler.KPCookies doInBackground(Void... voids)
        {
            KPLoginHandler.KPCookies loginCookies = new KPLoginHandler.KPCookies();

            int numberOfTries = 0;
            boolean finishedCleanly = false;

            while (!finishedCleanly)
            {
                numberOfTries++;

                if (numberOfTries == 5)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            errorMessage.animate().alpha(1f).setDuration(750).setListener(null);
                        }
                    });

                    break;
                }

                try
                {
                    loginCookies =
                            KPLoginHandler.login(loginEmail, loginPassword, getActivity());

                    page_mojNalog
                            = KPLoginHandler.getPageWithCookies(loginCookies, "https://www.kupujemprodajem.com/user.php?action=edit");

                    page_Ocene
                            = KPLoginHandler.getPageWithCookies(loginCookies, "https://www.kupujemprodajem.com/review.php?action=list");

                    userPersonalInfo = new UserPersonalInfo();

                    try
                    {
                        userPersonalInfo.userFullName = page_mojNalog.select("input").get(3).attr("value");
                    }
                    catch (Exception e)
                    {
                        // not logged in - throw the user back to the login page
                        returnToLogin();
                        return null;
                    }


                    Pattern emailPattern = Pattern.compile("\\\".*?E-mail: (.*?@.*?\\..*?) ");
                    Matcher matcher = emailPattern.matcher(page_mojNalog.toString());
                    if (matcher.find()) userPersonalInfo.userEmail = matcher.group(1);
                    else userPersonalInfo.userEmail = loginEmail;

                    userPersonalInfo.userNegativeVotes = page_Ocene.select("span.negativeColor").first().text();
                    userPersonalInfo.userPositiveVotes = page_Ocene.select("span.positiveColor").first().text();

                    userPersonalInfo.userPhoneNumber
                            = page_mojNalog.select("input").attr("type", "text").attr("name", "data[phone]").attr("maxlength", "100").get(4).attr("value");

                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            userProfileCard.setCard(new UserProfileCard(getActivity(), userPersonalInfo));
                            userProfileCard.animate().alpha(1f).setDuration(500).setListener(null);
                        }
                    });

                    finishedCleanly = true;

                } catch (Exception e)
                {
                    Log.e("", "Exception caught", e);
                }
            }

            return loginCookies;
        }

        @Override
        protected void onPostExecute(KPLoginHandler.KPCookies kpCookies)
        {
            if (kpCookies != null)
            {
                loginCookies = kpCookies;

                downloadBar.animate().alpha(0f).setDuration(300).setListener(null);
            }
        }
    }
    private CardView userProfileCard;
    private UserPersonalInfo userPersonalInfo;

    private TextView errorMessage;

    private SmoothProgressBar downloadBar;

    private String loginEmail;
    private String loginPassword;
    private KPLoginHandler.KPCookies loginCookies;

    private Document page_mojNalog;
    private Document page_Ocene;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static UserProfileFragment newInstance(int sectionNumber) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("sp", 0);
        loginEmail = prefs.getString("email", "nouser");
        loginPassword = prefs.getString("password", "nopassword");

        if (loginEmail.equals("nouser"))
        {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            new LoginTask().execute();
        }


        this.userProfileCard = (CardView) rootView.findViewById(R.id.userProfileCard);
        this.userProfileCard.setAlpha(0f);

        this.errorMessage = (TextView) rootView.findViewById(R.id.errorMessage);
        errorMessage.setAlpha(0f);
        errorMessage.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf"));

        this.downloadBar = (SmoothProgressBar) rootView.findViewById(R.id.downloadBar);

        ((Button) rootView.findViewById(R.id.logoutButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logOut();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((UserProfileActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void returnToLogin()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Niste uneli tačan e-mail ili lozinku.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(loginIntent);
    }

    private void logOut()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences("sp", 0);
//        loginEmail = prefs.getString("email", "nouser");
//        loginPassword = prefs.getString("password", "nopassword");

        prefs.edit().remove("email").commit();
        prefs.edit().remove("password").commit();

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Odjavili ste se sa Kupujem Prodajem.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(loginIntent);
    }
}