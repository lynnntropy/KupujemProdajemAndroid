package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.omegavesko.kupujemprodajem.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;

public class ItemPageActivity extends Activity {

    class PageDownloaderTask extends AsyncTask<String, Void, ItemPageData>
    {

        private List<WebsiteHandler.Category> categories;
        private List<WebsiteHandler.Location> locations;

        private org.jsoup.nodes.Document downloadedPage;

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

                // loop through <p> elements to preserve newlines
                result.description = "";

                if (itemPage.select("div.roundBorder").first().select("p").size() > 0)
                {
                    for (Element e : itemPage.select("div.roundBorder").first().select("p"))
                    {
                        result.description += e.html().replace("<br/>", "\n").replace("<br />", "\n").replaceAll("<.*?>", "").replace("&nbsp;", "") + "\n\n";
                    }
                }
                else
                {
                    result.description += itemPage.select("div.roundBorder").first().html().replace("<br/>", "\n").replace("<br />", "\n").replaceAll("<.*?>", "").replace("&nbsp;", "");
                }

                result.price = itemPage.select("div.price").first().text();
                result.viewCount = itemPage.select("td.lineSeparator").first().select("div").get(1).text();

                result.memberLocation = itemPage.select("td.lineSepar1ator").select("tr").get(4).text();
                result.memberName = itemPage.select("td.lineSepar1ator").select("tr").get(1).text().replace(" offline", "");
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

                    downloadIndicator.animate().alpha(0f).setDuration(300);

                    itemPrice.setText(result.price);

                    sellerNameAndVotes.setText(result.memberName + " (+" + result.memberYesVotes + ", -" + result.memberNoVotes + ")");
                    sellerLocation.setText(result.memberLocation);
                    sellerPhoneNumber.setImageBitmap(result.memberPhoneNumber);

                    itemDesc.setText(result.description);
                    itemDesc.animate().alpha(1f).setDuration(500).setListener(null);

                    itemPrice.animate().alpha(1f).setDuration(500).setListener(null);
                    sellerNameAndVotes.animate().alpha(1f).setDuration(500).setListener(null);
                    sellerPhoneNumber.animate().alpha(1f).setDuration(500).setListener(null);
                    sellerLocation.animate().alpha(1f).setDuration(500).setListener(null);
                }
            });

        }
    }

    private SearchResult result;

    private ImageLoader imgLoader;


    private TextView itemName;
    private TextView itemDesc;
    private ProgressBar downloadIndicator;

    private TextView sellerNameAndVotes;
    private TextView sellerLocation;
    private ImageView sellerPhoneNumber;

    private TextView itemPrice;

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
        itemDesc = (TextView) findViewById(R.id.itemDesc);
        downloadIndicator = (ProgressBar) findViewById(R.id.downloadIndicator);

        sellerNameAndVotes = (TextView) findViewById(R.id.sellerNameAndVotes);
        sellerLocation = (TextView) findViewById(R.id.sellerLocation);
        sellerPhoneNumber = (ImageView) findViewById(R.id.sellerPhoneNumberImage);

        itemPrice = (TextView) findViewById(R.id.itemPrice);

        itemName.setText(result.itemName);
        itemDesc.setAlpha(0f);

        itemPrice.setAlpha(0f);
        sellerNameAndVotes.setAlpha(0f);
        sellerPhoneNumber.setAlpha(0f);
        sellerLocation.setAlpha(0f);

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
