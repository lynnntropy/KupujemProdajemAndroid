package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

public class ItemSearchFragment extends Fragment
{
    public static final String SEARCH_RESULT_EXTRA_KEY = "com.omegavesko.kupujemprodajem.SEARCH_RESULT";

    class PageDownloaderTask extends AsyncTask<String, Void, List<SearchResult>>
    {

        private List<WebsiteHandler.Category> categories;
        private List<WebsiteHandler.Location> locations;

        private org.jsoup.nodes.Document downloadedPage;

        protected List<SearchResult> doInBackground(String... params)
        {

            // update the UI to show that we're downloading stuff

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
//                    downloadButton.setClickable(false);
//
//                    downloadButton.animate().alpha(0.2f).setDuration(300).setListener(null);
//                    downloadBar.animate().alpha(1f).setDuration(300).setListener(null);
                }
            });

            // download the page and extract the elements containing results

            List<SearchResult> results = new ArrayList<SearchResult>();

            boolean finishedCleanly = false;
            int tryNumber = 0;

            while (!finishedCleanly)
            {
                tryNumber++;

                if (tryNumber == 2)
                {
                    // fade in the 'trying again' message
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tryAgainText.animate().alpha(1f).setDuration(800).setListener(null);
                        }
                    });
                }

                try
                {
                    Document searchPage = Jsoup.connect(params[0]).get();

                    Elements searchResults = searchPage.select("div.item.clearfix");

                    for (Element elem : searchResults)
                    {
                        String itemName = elem.select("a.adName").text();
                        String itemLink = elem.select("a.adName").attr("abs:href");
                        String itemDesc = elem.select("p.adDescription").text();

                        String itemThumbnailURL = elem.select("div.adImgHolder").select("img").attr("abs:src");
                        Bitmap itemThumbnail = imgLoader.loadImageSync(itemThumbnailURL);

                        String itemPrice = elem.select("span.adPrice").text();
                        String itemLocation = elem.select("section.locationSec").text();

                        results.add(new SearchResult(itemName, itemDesc, itemLink, itemThumbnail, itemPrice, itemLocation));
                    }

                    finishedCleanly = true;
                }
                catch (Exception e)
                {
                    Log.e("PageDownloader", e.toString());
                }
            }

            return results;
        }

        protected void onProgressUpdate(Integer... progress) {
            // do nothing
        }

        protected void onPostExecute(final List<SearchResult> results)
        {
            // we're done - do something with the downloaded page

            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {

                    tryAgainText.animate().alpha(0f).setDuration(500).setListener(null);

                    ArrayList<Card> cards = new ArrayList<Card>();

                    Log.i("", "Search results retrieved:");
                    for (SearchResult res: results)
                    {
                        if (res.itemName.length() > 0 && res.itemLocation.length() > 0 && !res.itemLocation.trim().isEmpty() && !res.itemLocation.equals("\u00A0") && res.itemLocation != null)
                        {
                            Log.i("", res.toString());

                            SearchResultCard resultCard = new SearchResultCard(getActivity(), res);

                            resultCard.setOnClickListener(new Card.OnCardClickListener() {
                                @Override
                                public void onClick(final Card card, View view) {

                                    // delay the onClick action to let the fading animation finish

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            Intent intent = new Intent(getActivity(), ItemPageActivity.class);
                                            SearchResult result = ((SearchResultCard) card).result;
                                            intent.putExtra(SEARCH_RESULT_EXTRA_KEY, result);
                                            startActivity(intent);
                                        }
                                    }, 800);
                                }
                            });

                            cards.add(resultCard);
                        }
                    }

                    LoadMoreCard moreResultsCard = new LoadMoreCard(getActivity());
                    // TODO: Add an on click listener to the card
                    cards.add(moreResultsCard);

                    CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
                    searchCardList.setAdapter(cardArrayAdapter);

                    searchCardList.animate().alpha(1f).setDuration(800).setListener(null);
                    downloadBar.animate().alpha(0f).setDuration(800).setListener(null);
                }
            });

        }
    }

    private static final String SEARCH_PARAMS_KEY = "searchParamsKey";
    private SearchParams searchParams;

    // Views contained in the fragment
    private CardListView searchCardList;
    private SmoothProgressBar downloadBar;
    private TextView tryAgainText;

    // Variables and constants the Fragment uses to function
    private String queryURL;

    private OnFragmentInteractionListener mListener;

    private ImageLoader imgLoader;


    public static ItemSearchFragment newInstance(SearchParams params) {
        ItemSearchFragment fragment = new ItemSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(SEARCH_PARAMS_KEY, params);
        fragment.setArguments(args);
        return fragment;
    }
    public ItemSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext()).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);

        this.searchParams = (SearchParams) getArguments().getSerializable(SEARCH_PARAMS_KEY);

        Log.w("ItemSearchFragment", "Received new search params: " + searchParams.toString());

        this.queryURL
                = "http://www.kupujemprodajem.com/search.php?action=list&data%5Bpage%5D=1&data%5Btip_oglasa%5D=" + searchParams.tipOglasaID + "&data%5Bprev_keywords%5D=keywords&data%5Bcategory_id%5D=" + searchParams.categoryID + "&data%5Bgroup_id%5D=&data%5Blocation_id%5D=" + searchParams.locationID + "&data%5Bcondition%5D=" + searchParams.stanjeID + "&data%5Bperiod%5D=&data%5Border%5D=" + searchParams.sortID + "&data%5Bkeywords%5D=" + searchParams.searchTerms + "&data%5Bprice_from%5D=&data%5Bprice_to%5D=&submit%5Bsearch%5D=Tra%C5%BEi";


        Log.w("ItemSearchFragment", "Generated query URL: " + queryURL);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_item_search, container, false);

        searchCardList = (CardListView) rootView.findViewById(R.id.searchCardList);
        searchCardList.setAlpha(0f);

        downloadBar = (SmoothProgressBar) rootView.findViewById(R.id.downloadBar);
        downloadBar.setAlpha(0f);
        downloadBar.animate().alpha(1f).setDuration(500).setListener(null);

        tryAgainText = (TextView) rootView.findViewById(R.id.tryAgainText);
        tryAgainText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf"));
        tryAgainText.setAlpha(0f);

        // TODO: debug!
        new PageDownloaderTask().execute(this.queryURL);

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
        public void onFragmentInteraction(Uri uri);
    }

}
