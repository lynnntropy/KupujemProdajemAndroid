package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.component.CardShadowView;

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

                }
            });

            ItemPageData result = new ItemPageData();

            try
            {
                Log.i("", "Downloading page...");
                Document itemPage = Jsoup.connect(params[0]).get();
                String pageSource = itemPage.toString();
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

                List<Bitmap> downloadedImages = new ArrayList<Bitmap>();
                List<String> imageURLs = new ArrayList<String>();

                String photoRegex = "changePhoto\\('(.*?)',.*?;";
                Pattern pattern = Pattern.compile(photoRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher matcher = pattern.matcher(pageSource);

                while(matcher.find())
                {
                    String imageURL = "http:" + matcher.group(1);
                    Log.i("", "Attempting to download image " + imageURL);

                    try
                    {
                        if (imageURL.length() > 0 && !imageURL.trim().equals("") && imageURL.length() > 10) // prevent false empty matches
                        {
                            if (!imageURLs.contains(imageURL)) // prevent duplicate image downloads
                            {
                                Bitmap currentPhotoBitmap = imgLoader.loadImageSync(imageURL);
                                downloadedImages.add(currentPhotoBitmap);
                                imageURLs.add(imageURL);
                            }
                        }
                    }
                    catch (Exception e) { Log.e("ImageDownload", e.toString() + "Image download exception! Image: " + imageURL); }
                }

                if (downloadedImages.size() == 0 || imageURLs.size() == 0)
                {
                    // no images found, see if there's only one image and download that
                    Log.i("", "No images found. Checking if the item only has a single image.");

                    String singlePhotoRegex = "src=\\\"(//www\\.kupujemprodajem\\.com/.*?big.*?\\.jpg)\"";
                    Pattern singlePattern = Pattern.compile(singlePhotoRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                    Matcher singleMatcher = singlePattern.matcher(pageSource);


                    while (singleMatcher.find())
                    {
                        String imageURL = "http:" + singleMatcher.group(1);
                        Log.i("", "Attempting to download image " + imageURL);

                        try
                        {
                            if (imageURL.length() > 0 && !imageURL.trim().equals("") && imageURL.length() > 10) // prevent false empty matches
                            {
                                if (!imageURLs.contains(imageURL)) // prevent duplicate image downloads
                                {
                                    Bitmap currentPhotoBitmap = imgLoader.loadImageSync(imageURL);
                                    downloadedImages.add(currentPhotoBitmap);
                                    imageURLs.add(imageURL);
                                }
                            }
                        }
                        catch (Exception e) { Log.e("ImageDownload", e.toString() + "Image download exception! Image: " + imageURL); }
                    }
                }

                itemPhotos = downloadedImages;
                itemPhotoURLs = imageURLs;
            }
            catch (Exception e) { Log.e("PageDownloader", e.toString() + " | " + e.getMessage() + " | " + e.getCause()); }

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
                    Card descriptionCard = new WebViewCard(getApplicationContext(), result.descriptionHTML);
                    Card sellerInfoCard = new SellerInformationCard(getApplicationContext(), result);

                    ImageGalleryCard galleryCard = new ImageGalleryCard(getApplicationContext(), itemPhotos, itemPhotoURLs);
                    itemGalleryCard.setCard(galleryCard);

                    mSellerInformationCard.setCard(sellerInfoCard);
                    mItemDescriptionCard.setCard(descriptionCard);

                    itemPrice.setText(result.price);

                    itemName.animate().alpha(1f).setDuration(800).setListener(null);
                    itemPrice.animate().alpha(1f).setDuration(800).setListener(null);
                    mSellerInformationCard.animate().alpha(1f).setDuration(800).setListener(null);
                    itemGalleryCard.animate().alpha(1f).setDuration(800).setListener(null);
                    mItemDescriptionCard.animate().alpha(1f).setDuration(800).setListener(null);
                    sellerCardShadow.animate().alpha(1f).setDuration(800).setListener(null);

                    progressBar.animate().alpha(0f).setDuration(500).setListener(null);
                }
            });

        }
    }

    private SearchResult result;

    private ImageLoader imgLoader;


    private TextView itemName;

    private TextView itemPrice;

    private CardView itemGalleryCard;

    private List<Bitmap> itemPhotos;
    private List<String> itemPhotoURLs;

    private CardShadowView sellerCardShadow;

    private SmoothProgressBar progressBar;

    private Intent shareItemIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_PROGRESS);


        setContentView(R.layout.activity_item_page);

//        setProgressBarIndeterminateVisibility(true);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);

        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("Pregled predmeta");

        result = (SearchResult) getIntent().getSerializableExtra(ItemSearchFragment.SEARCH_RESULT_EXTRA_KEY);

        shareItemIntent = new Intent();
        shareItemIntent.setAction(Intent.ACTION_SEND);
        shareItemIntent.setType("text/plain");
        shareItemIntent.putExtra(Intent.EXTRA_TEXT, result.itemURL);


        itemName = (TextView) findViewById(R.id.itemName);
//        itemDesc = (TextView) findViewById(R.id.itemDesc);
//        downloadIndicator = (ProgressBar) findViewById(R.id.downloadIndicator);

        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemPrice.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

        mSellerInformationCard = (CardView) findViewById(R.id.sellerInformationCard);
        mItemDescriptionCard = (CardView) findViewById(R.id.itemDescriptionCard);

        itemName.setText(result.itemName);
        itemName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));

        itemGalleryCard = (CardView) findViewById(R.id.imageGalleryCard);

        sellerCardShadow = (CardShadowView) findViewById(R.id.sellerCardShadow);

        progressBar = (SmoothProgressBar) findViewById(R.id.downloadBar);

        itemName.setAlpha(0f);
        itemPrice.setAlpha(0f);
        mSellerInformationCard.setAlpha(0f);
        itemGalleryCard.setAlpha(0f);
        mItemDescriptionCard.setAlpha(0f);
        sellerCardShadow.setAlpha(0f);

        progressBar.setAlpha(0f);
        progressBar.animate().alpha(1f).setDuration(500).setListener(null);

        new PageDownloaderTask().execute(result.itemURL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_page, menu);

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) shareItem.getActionProvider();

        if (shareActionProvider != null)
            shareActionProvider.setShareIntent(shareItemIntent);

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
