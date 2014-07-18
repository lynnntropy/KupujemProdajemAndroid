package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
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

            try
            {
                Document searchPage = Jsoup.connect(params[0]).get();

                Elements searchResults = searchPage.select("div.item.clearfix");

                for (Element elem: searchResults)
                {
                    String itemName = elem.select("a.adName").text();
                    String itemLink = elem.select("a.adName").attr("abs:href");
                    String itemDesc = elem.select("p.adDescription").text();

                    String itemThumbnailURL = elem.select("div.adImgHolder").select("img").attr("abs:src");
                    Bitmap itemThumbnail = imgLoader.loadImageSync(itemThumbnailURL);

                    String itemPrice = elem.select("span.adPrice").text();
                    String itemLocation = elem.select("section.locationSec").text();

                    results.add(new SearchResult(itemName, itemDesc, itemLink, itemThumbnail, itemPrice, itemLocation));

//                    Log.i("PageDownloader", "Found search result: " + itemName + " | " + itemDesc + " | " + itemLink);
                }
            }
            catch (Exception e) { Log.e("PageDownloader", e.toString()); }

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
//                    kategorijaSpinner.setAdapter(new StyledSpinnerCategoryAdapter(getActivity(), dbHandler.getAllCategories()));
//                    mestoSpinner.setAdapter(new StyledSpinnerLocationAdapter(getActivity(), dbHandler.getAllLocations()));
//
//                    downloadButton.animate().alpha(1f).setDuration(300).setListener(null);
//                    downloadBar.animate().alpha(0f).setDuration(300).setListener(null);
//
////                        kategorijaSpinner.setAdapter(new StyledSpinnerStringAdapter(getActivity(), categories.toArray(new WebsiteHandler.Category[categories.size()])));
//
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            downloadDialog.dismiss();
//                        }
//                    }, 600);

                    ArrayList<Card> cards = new ArrayList<Card>();

                    Log.i("", "Search results retrieved:");
                    for (SearchResult res: results)
                    {
                        if (res.itemName.length() > 0)
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

                    CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
                    searchCardList.setAdapter(cardArrayAdapter);
                }
            });

        }
    }

    private static final String SEARCH_PARAMS_KEY = "searchParamsKey";
    private SearchParams searchParams;

    // Views contained in the fragment
    private CardListView searchCardList;

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

        // TODO: debug!
        new PageDownloaderTask().execute(this.queryURL);

//        ArrayList<Card> cards = new ArrayList<Card>();
//
//        for (int i = 0; i < 500; i++)
//        {
//            Card card = new Card(getActivity());
//
//            CardHeader header = new CardHeader(getActivity());
//            header.setTitle("Predmet broj " + i);
//            card.addCardHeader(header);
//
//            CardThumbnail thumb = new CardThumbnail(getActivity());
//            thumb.setDrawableResource(R.drawable.ic_launcher);
//            card.addCardThumbnail(thumb);
//
//            cards.add(card);
//        }
//
//        CardArrayAdapter cardArrayAdapter= new CardArrayAdapter(getActivity(), cards);
//        searchCardList.setAdapter(cardArrayAdapter);

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
