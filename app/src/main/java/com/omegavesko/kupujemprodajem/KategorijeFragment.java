package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link KategorijeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KategorijeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */



public class KategorijeFragment extends android.app.Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

//    private PlusOneButton mPlusOneButton;

    private OnFragmentInteractionListener mListener;

    private CardListView categoryCardList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KategorijeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KategorijeFragment newInstance() {
        KategorijeFragment fragment = new KategorijeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public KategorijeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kategorije, container, false);

        OmegaUtil.setTitleWithFont(
                getActivity(), "Kategorije", OmegaUtil.TITLE_FONT_NAME, OmegaUtil.dpToPixels(getActivity(), 25f));

//        getActivity().getActionBar().setTitle("Kategorije");

        categoryCardList = (CardListView) view.findViewById(R.id.categoryCardList);

        ArrayList<Card> cards = new ArrayList<Card>();
        List<WebsiteHandler.Category> allCategories = new DatabaseHandler(getActivity()).getAllCategories();

        if (allCategories.size() > 0)
        {
            for (final WebsiteHandler.Category cat : allCategories)
            {
                Card currentCard = new CategoryCard(getActivity(), cat);

                currentCard.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Log.w("CategoryCard", "Category " + cat.nameString + " clicked!");

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
//                                getActivity().getActionBar().setTitle(cat.nameString);
                                OmegaUtil.setTitleWithFont(
                                        getActivity(), cat.nameString, OmegaUtil.TITLE_FONT_NAME, OmegaUtil.dpToPixels(getActivity(), 25f));

                                SearchParams params = new SearchParams("all", cat.idString, "", "all", "relevance", "");

                                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                                android.app.Fragment fragment = ItemSearchFragment.newInstance(params);
                                ft.replace(R.id.container, fragment).addToBackStack("startSearch");
                                ft.commit();
                            }
                        }, 800);
                    }
                });

                cards.add(currentCard);
            }
        }

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        if (categoryCardList != null) categoryCardList.setAdapter(cardArrayAdapter);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
//        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
