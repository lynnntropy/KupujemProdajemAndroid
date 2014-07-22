package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
import it.gmariotti.cardslib.library.internal.Card;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomActivity extends Activity {

//    @InjectView(R.id.zoomedImageView)
    ImageView mZoomedImageView;

    ImageLoader imgLoader;

    PhotoViewAttacher imageAttacher;

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... params)
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

            Bitmap image = null;

            ItemPageData result = new ItemPageData();

            try
            {
                image = imgLoader.loadImageSync(params[0]);
            }
            catch (Exception e) { Log.e("PageDownloader", e.toString()); }

            return image;
        }

        protected void onProgressUpdate(Integer... progress) {
            // do nothing
        }

        protected void onPostExecute(final Bitmap downloadedImage)
        {
            // we're done - do something with the downloaded page

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mZoomedImageView.setImageBitmap(downloadedImage);
                    imageAttacher.update();
                }
            });

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        OmegaUtil.setTitleWithFont(
                this, "Pregled slike", OmegaUtil.TITLE_FONT_NAME, OmegaUtil.dpToPixels(this, 25f));

        getActionBar().setDisplayShowHomeEnabled(false);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config);

        String imageURL = getIntent().getStringExtra("imageURL");

        mZoomedImageView = (ImageView) findViewById(R.id.zoomedImageView);
        imageAttacher = new PhotoViewAttacher(mZoomedImageView);

        new ImageDownloaderTask().execute(imageURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_zoom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
