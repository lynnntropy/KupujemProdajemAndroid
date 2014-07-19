package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

public class ItemPageActivity extends Activity {

//    @InjectView(R.id.sellerInformationCard)
    CardView mSellerInformationCard;
//    @InjectView(R.id.itemDescriptionCard)
    CardView mItemDescriptionCard;

    class PageDownloaderTask extends AsyncTask<String, Void, ItemPageData>
    {

        private List<WebsiteHandler.Category> categories;
        private List<WebsiteHandler.Location> locations;

        private Document downloadedPage;

        protected ItemPageData doInBackground(String... params)
        {

            // update the UI to show that we're downloading stuff

            runOnUiThread(new Runnable()
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

            ItemPageData result = new ItemPageData();

            try
            {
                Log.i("", "Downloading page...");
                Document itemPage = Jsoup.connect(params[0]).get();
                Log.i("", "Download complete. Starting parse.");

                result.title = itemPage.select("div.oglasHolder").select("h1").first().text();

                // now passing HTML to a WebViewCard instead of trying to parse it ourselves
                result.descriptionHTML = itemPage.select("div.roundBorder").first().html();

                result.price = itemPage.select("div.price").first().text();
                result.viewCount = itemPage.select("td.lineSeparator").first().select("div").get(1).text();

                result.memberLocation = itemPage.select("td.lineSepar1ator").select("tr").get(4).text();
                result.memberName = itemPage.select("td.lineSepar1ator").select("tr").get(1).text().replace(" offline", "").replace(" online", "");
                result.memberYesVotes = itemPage.select("td.lineSepar1ator").select("tr").get(2).select("span.positiveColor").text();
                result.memberNoVotes = itemPage.select("td.lineSepar1ator").select("tr").get(2).select("span.negativeColor").text();

                String phoneNumberImageURL = itemPage.select("div.phone-number").select("img").get(1).attr("abs:src");
                result.memberPhoneNumber = imgLoader.loadImageSync(phoneNumberImageURL);

                // TODO: Load full-size photos from the page and put them into the List<>

            }
            catch (Exception e) { Log.e("PageDownloader", e.toString()); }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            // do nothing
        }

        protected void onPostExecute(final ItemPageData result)
        {
            // we're done - do something with the downloaded page

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
//                    // TODO

                    Card descriptionCard = new WebViewCard(getApplicationContext(), result.descriptionHTML);
                    Card sellerInfoCard = new SellerInformationCard(getApplicationContext(), result);

                    mSellerInformationCard.setCard(sellerInfoCard);
                    mItemDescriptionCard.setCard(descriptionCard);

                    downloadIndicator.animate().alpha(0f).setDuration(300);

                    itemPrice.setText(result.price);

                    itemPrice.animate().alpha(1f).setDuration(500).setListener(null);
                }
            });

        }
    }

    private SearchResult result;

    private ImageLoader imgLoader;


    private TextView itemName;
    private ProgressBar downloadIndicator;

    private TextView itemPrice;

    private CardView itemGalleryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("Pregled predmeta");

        result = (SearchResult) getIntent().getSerializableExtra(ItemSearchFragment.SEARCH_RESULT_EXTRA_KEY);

        itemName = (TextView) findViewById(R.id.itemName);
//        itemDesc = (TextView) findViewById(R.id.itemDesc);
        downloadIndicator = (ProgressBar) findViewById(R.id.downloadIndicator);

        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemPrice.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

        mSellerInformationCard = (CardView) findViewById(R.id.sellerInformationCard);
        mItemDescriptionCard = (CardView) findViewById(R.id.itemDescriptionCard);

        itemName.setText(result.itemName);
        itemName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
        itemPrice.setAlpha(0f);

        itemGalleryCard = (CardView) findViewById(R.id.imageGalleryCard);

        ImageGalleryCard galleryCard = new ImageGalleryCard(this, new ArrayList<Bitmap>());
        itemGalleryCard.setCard(galleryCard);

        new PageDownloaderTask().execute(result.itemURL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_page, menu);
        return true;
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
}
