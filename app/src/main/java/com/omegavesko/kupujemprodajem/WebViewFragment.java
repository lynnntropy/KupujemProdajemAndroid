package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.jsoup.nodes.Document;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class WebViewFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private String htmlToDisplay;
    private boolean useCookies;

    private WebView webView;

    // TODO: Rename and change types and number of parameters
    public static WebViewFragment newInstance(String htmlToDisplay, boolean useCookies) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();

        args.putString("HTML", htmlToDisplay);
        args.putBoolean("useCookies", useCookies);

        fragment.setArguments(args);
        return fragment;
    }
    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.htmlToDisplay = getArguments().getString("HTML");
            this.useCookies = getArguments().getBoolean("useCookies");
        }
    }

    ///////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);

        this.webView = (WebView) rootView.findViewById(R.id.webView);

        SharedPreferences prefs = getActivity().getSharedPreferences("sp", 0);
        String email = prefs.getString("email", "nouser");
        String password = prefs.getString("password", "nopass");

        if (email.equals("nouser"))
        {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            KPLoginHandler.KPCookies cookies = KPLoginHandler.login(email, password, getActivity());
            webView.setInitialScale(50);
            webView.getSettings().setUseWideViewPort(true);
            webView.loadData(htmlToDisplay, "text/html; charset=UTF-8", null);
        }

        return rootView;
    }

    //////////////////////

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
