package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String email;
    private String pass;

    private EditText emailField;
    private EditText passField;

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        emailField = (EditText) rootView.findViewById(R.id.emailField);
        passField = (EditText) rootView.findViewById(R.id.passwordField);
        TextView topText = (TextView) rootView.findViewById(R.id.topMessage);

        topText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf"));

        ((Button) rootView.findViewById(R.id.submitButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submitLoginDetails();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    public void submitLoginDetails()
    {
        Log.i("", "Submit button clicked!");

        SharedPreferences settings = getActivity().getSharedPreferences("sp", 0);
        SharedPreferences.Editor editor = settings.edit();

        this.email = emailField.getText().toString();
        this.pass = passField.getText().toString();

        editor.putString("email", this.email);
        editor.putString("password", this.pass);
//        editor.putString("email", "debug email");
//        editor.putString("password", "debug pass");
        editor.commit();

        settings = getActivity().getSharedPreferences("sp", 0);
        Log.i("", "Email is now: " + settings.getString("email", "omegavesko@mailinator.com"));
        Log.i("", "Password is now: " + settings.getString("password", "nopassword"));

        // send the user to the profile activity
        Intent profileIntent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(profileIntent);
    }

}
