package com.omegavesko.kupujemprodajem;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import info.hoang8f.widget.FButton;


public class HomeActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks , KategorijeFragment.OnFragmentInteractionListener, ItemSearchFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        // do nothing? :P
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Properly set up the sliding menu!

        SlidingMenu navDrawer = new SlidingMenu(this);
        navDrawer.setMode(SlidingMenu.LEFT);
        navDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        navDrawer.setShadowWidthRes(R.dimen.shadow_width);
//        navDrawer.setShadowDrawable(R.drawable.shadow);
        navDrawer.setBehindWidth(200);
        navDrawer.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        navDrawer.setFadeDegree(0.35f);
        navDrawer.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        navDrawer.setMenu(R.layout.navdrawer_layout);

        getActionBar().setDisplayShowHomeEnabled(false);

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
//
//        // Set up the drawer.
//        mNavigationDrawerFragment.setUp(
//                R.id.navigation_drawer,
//                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(final int position) {
        // update the main content by replacing fragments
        final FragmentManager fragmentManager = getFragmentManager();

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (position == 0)
                        {
                            mTitle = "Kupujem Prodajem";

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                                    .commit();
                        }
                        else if (position == 1)
                        {
                            mTitle = "Kategorije";

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, KategorijeFragment.newInstance())
                                    .commit();
                        }
                    }
                }, 400);
            }
        });


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
//                mTitle = getString(R.string.title_section1);
                mTitle = "Kupujem Prodajem";
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.home, menu);
//            restoreActionBar();
//            return true;
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        class PageDownloaderTask extends AsyncTask<Void, Void, Document>
        {

            private List<WebsiteHandler.Category> categories;
            private List<WebsiteHandler.Location> locations;

            private org.jsoup.nodes.Document downloadedPage;

            protected org.jsoup.nodes.Document doInBackground(Void... params) {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        downloadButton.setClickable(false);

                        downloadButton.animate().alpha(0.2f).setDuration(300).setListener(null);
                        downloadBar.animate().alpha(1f).setDuration(300).setListener(null);
                    }
                });


                DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
                dbHandler.ClearTables();

                dbHandler.addCategory(new WebsiteHandler.Category("Sve kategorije", ""));
                dbHandler.addLocation(new WebsiteHandler.Location("Sva mesta", ""));


                // scrape categories and put them into a List<T>

                Log.i("PageDownloaderTask", "Starting webpage download!");

                categories = WebsiteHandler.scrapeCategories();

                Log.i("PageDownloaderTask", "Page downloader complete, parsing categories:");

                for(WebsiteHandler.Category cat: categories)
                {
                    Log.i("PageDownloaderTask", cat.nameString + " | " + cat.idString);
                    dbHandler.addCategory(cat);
                }

                // scrape locations and put them into a List<T>

                Log.i("PageDownloaderTask", "Starting webpage download!");

                locations = WebsiteHandler.scrapeLocations();

                Log.i("PageDownloaderTask", "Page downloader complete, parsing locations:");

                for(WebsiteHandler.Location loc: locations)
                {
                    Log.i("PageDownloaderTask", loc.nameString + " | " + loc.idString);
                    dbHandler.addLocation(loc);
                }

                return new org.jsoup.nodes.Document("");
            }

            protected void onProgressUpdate(Integer... progress) {
                // do nothing
            }

            protected void onPostExecute(org.jsoup.nodes.Document result)
            {
                final DatabaseHandler dbHandler = new DatabaseHandler(getActivity());

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        kategorijaSpinner.setAdapter(new StyledSpinnerCategoryAdapter(getActivity(), dbHandler.getAllCategories()));
                        mestoSpinner.setAdapter(new StyledSpinnerLocationAdapter(getActivity(), dbHandler.getAllLocations()));

                        downloadButton.animate().alpha(1f).setDuration(300).setListener(null);
                        downloadBar.animate().alpha(0f).setDuration(300).setListener(null);

//                        kategorijaSpinner.setAdapter(new StyledSpinnerStringAdapter(getActivity(), categories.toArray(new WebsiteHandler.Category[categories.size()])));

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                downloadDialog.dismiss();
                            }
                        }, 600);
                    }
                });

            }
        }

        private static final String ARG_SECTION_NUMBER = "section_number";

        private TextView logoText1;
        private TextView logoText2;

        private EditText pretragaEditText;

        private TextView tipOglasaLabel;
        private TextView kategorijaLabel;
        private TextView mestoLabel;
        private TextView stanjeLabel;
        private TextView prikaziPrvoLabel;

        private Spinner tipOglasaSpinner;
        private Spinner kategorijaSpinner;
        private Spinner mestoSpinner;
        private Spinner stanjeSpinner;
        private Spinner prikaziPrvoSpinner;

        private FButton searchButton;

        private Typeface robotoLight;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            getActivity().getActionBar().setTitle("Kupujem Prodajem");

            // TODO: Debug, shouldn't get called every time
            showTutorial();

            SharedPreferences settings = getActivity().getSharedPreferences("preferences", 0);
            boolean firstLaunch = settings.getBoolean("firstLaunch", true);

            if (firstLaunch)
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("firstLaunch", false);
                editor.commit();
            }

            DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
            if (dbHandler.getAllCategories().size() == 0 || dbHandler.getAllCategories().size() == 0) OpenDownloadDialog();

            robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

            logoText1 = (TextView) rootView.findViewById(R.id.logoText1);
            logoText2 = (TextView) rootView.findViewById(R.id.logoText2);

            pretragaEditText = (EditText) rootView.findViewById(R.id.pretragaEditText);

            tipOglasaLabel = (TextView) rootView.findViewById(R.id.tipOglasaLabel);
            tipOglasaSpinner = (Spinner) rootView.findViewById(R.id.tipOglasaSpinner);

            kategorijaLabel = (TextView) rootView.findViewById(R.id.kategorijaLabel);
            kategorijaSpinner = (Spinner) rootView.findViewById(R.id.kategorijaSpinner);

            mestoLabel = (TextView) rootView.findViewById(R.id.mestoLabel);
            mestoSpinner = (Spinner) rootView.findViewById(R.id.mestoSpinner);

            stanjeLabel = (TextView) rootView.findViewById(R.id.stanjeLabel);
            stanjeSpinner = (Spinner) rootView.findViewById(R.id.stanjeSpinner);

            prikaziPrvoLabel = (TextView) rootView.findViewById(R.id.prikaziPrvoLabel);
            prikaziPrvoSpinner = (Spinner) rootView.findViewById(R.id.prikaziPrvoSpinner);

            searchButton = (FButton) rootView.findViewById(R.id.searchButton);

            logoText1.setTypeface(robotoLight);
            logoText2.setTypeface(robotoLight);

            pretragaEditText.setTypeface(robotoLight);
            pretragaEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            pretragaEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
                {
                    if (i == EditorInfo.IME_ACTION_SEARCH)
                    {
                        // close the keyboard!
                        pretragaEditText.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pretragaEditText.getWindowToken(), 0);

                        // run the search
                        PerformSearch();
                    }

                    return false;
                }
            });

            searchButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    PerformSearch();
                }
            });

            kategorijaLabel.setTypeface(robotoLight);
            mestoLabel.setTypeface(robotoLight);
            prikaziPrvoLabel.setTypeface(robotoLight);
            stanjeLabel.setTypeface(robotoLight);
            tipOglasaLabel.setTypeface(robotoLight);

            List<WebsiteHandler.GenericID> tipoviOglasa
                    = Arrays.asList(
                    new WebsiteHandler.GenericID("Svi oglasi", "all"),
                    new WebsiteHandler.GenericID("Nudi se", "sell"),
                    new WebsiteHandler.GenericID("Traži se", "buy"));

            List<WebsiteHandler.GenericID> stanja
                    = Arrays.asList(
                    new WebsiteHandler.GenericID("Novo i polovno", "all"),
                    new WebsiteHandler.GenericID("Samo novo", "new"),
                    new WebsiteHandler.GenericID("Samo polovno", "used"));

            List<WebsiteHandler.GenericID> prikaziPrvo
                    = Arrays.asList(
                    new WebsiteHandler.GenericID("Jeftinije", "price"),
                    new WebsiteHandler.GenericID("Skuplje", "price+desc"),
                    new WebsiteHandler.GenericID("Novije oglase", "posted+desc"),
                    new WebsiteHandler.GenericID("Popularnije", "view_count+desc"),
                    new WebsiteHandler.GenericID("Relevantnije", "relevance"));

            StyledSpinnerGenericAdapter tipoviAdapter = new StyledSpinnerGenericAdapter(getActivity(), tipoviOglasa);
            StyledSpinnerCategoryAdapter kategorijeAdapter = new StyledSpinnerCategoryAdapter(getActivity(), dbHandler.getAllCategories());
            StyledSpinnerLocationAdapter mestaAdapter = new StyledSpinnerLocationAdapter(getActivity(), dbHandler.getAllLocations());
            StyledSpinnerGenericAdapter stanjaAdapter = new StyledSpinnerGenericAdapter(getActivity(), stanja);
            StyledSpinnerGenericAdapter prikaziPrvoAdapter = new StyledSpinnerGenericAdapter(getActivity(), prikaziPrvo);

            tipOglasaSpinner.setAdapter(tipoviAdapter);
            kategorijaSpinner.setAdapter(kategorijeAdapter);
            mestoSpinner.setAdapter(mestaAdapter);
            stanjeSpinner.setAdapter(stanjaAdapter);
            prikaziPrvoSpinner.setAdapter(prikaziPrvoAdapter);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private FButton downloadButton;
        private SmoothProgressBar downloadBar;

        private Dialog downloadDialog;

        public void OpenDownloadDialog()
        {
            downloadDialog = new Dialog(getActivity());

            downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            downloadDialog.setContentView(R.layout.download_dialog);

            downloadDialog.setCancelable(false);
            downloadDialog.setCanceledOnTouchOutside(false);

            TextView textView1 = (TextView) downloadDialog.findViewById(R.id.textView1);
            textView1.setTypeface(robotoLight);

            TextView textView2 = (TextView) downloadDialog.findViewById(R.id.sellerLocation);
            textView2.setTypeface(robotoLight);

            downloadBar = (SmoothProgressBar) downloadDialog.findViewById(R.id.downloadBar);
            downloadBar.setAlpha(0f);

            downloadButton = (FButton) downloadDialog.findViewById(R.id.downloadButton);
            downloadButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    new PageDownloaderTask().execute();
                }
            });
            
            downloadDialog.show();
        }

        public void PerformSearch()
        {
            // try to prevent the keyboard randomly popping up again
            pretragaEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pretragaEditText.getWindowToken(), 0);

            logoText1.animate().alpha(0f).setDuration(800).setListener(null);
            logoText2.animate().alpha(0f).setDuration(800).setListener(null);

            pretragaEditText.animate().alpha(0f).setDuration(800).setListener(null);

            tipOglasaLabel.animate().alpha(0f).setDuration(800).setListener(null);
            tipOglasaSpinner.animate().alpha(0f).setDuration(800).setListener(null);

            kategorijaLabel.animate().alpha(0f).setDuration(800).setListener(null);
            kategorijaSpinner.animate().alpha(0f).setDuration(800).setListener(null);

            mestoLabel.animate().alpha(0f).setDuration(800).setListener(null);
            mestoSpinner.animate().alpha(0f).setDuration(800).setListener(null);

            stanjeLabel.animate().alpha(0f).setDuration(800).setListener(null);
            stanjeSpinner.animate().alpha(0f).setDuration(800).setListener(null);

            prikaziPrvoLabel.animate().alpha(0f).setDuration(800).setListener(null);
            prikaziPrvoSpinner.animate().alpha(0f).setDuration(800).setListener(null);

            searchButton.animate().alpha(0f).setDuration(800).setListener(null);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    getActivity().getActionBar().setTitle("Rezultati pretrage");

                    SearchParams params = new SearchParams
                            (((WebsiteHandler.GenericID) tipOglasaSpinner.getSelectedItem()).idString,
                                    ((WebsiteHandler.Category) kategorijaSpinner.getSelectedItem()).idString,
                                    ((WebsiteHandler.Location) mestoSpinner.getSelectedItem()).idString,
                                    ((WebsiteHandler.GenericID) stanjeSpinner.getSelectedItem()).idString,
                                    ((WebsiteHandler.GenericID) prikaziPrvoSpinner.getSelectedItem()).idString,
                                    pretragaEditText.getText().toString().replace(" ", "+"));

                    FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                    Fragment fragment = ItemSearchFragment.newInstance(params);
                    ft.replace(R.id.container, fragment).addToBackStack("startSearch");
                    ft.commit();
                }
            }, 800);
        }

        public void showTutorial()
        {
            // get the screen dimensions so we know where to put the marker
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            new ShowcaseView.Builder(getActivity())
                    .setTarget(new PointTarget(0, height / 2))
                    .setContentTitle("Fioka na izvlačenje")
                    .setContentText("Povucite prstom na desno da biste otvorili meni sa dodatnim opcijama.")
                    .hideOnTouchOutside()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .build();
        }
    }

}
