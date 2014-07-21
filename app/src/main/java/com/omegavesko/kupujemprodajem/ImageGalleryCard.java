package com.omegavesko.kupujemprodajem;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/19/2014.
 */
public class ImageGalleryCard extends Card
{
    public List<Bitmap> bitmapsToDisplay;
    public List<String> imageURLs;

    ScrollView galleryScrollView;

    private Context context;

    private LinearLayout imageHolder;

    private void init() {}

    public ImageGalleryCard(Context context, List<Bitmap> bitmaps, List<String> imageURLs)
    {
        super(context, R.layout.gallery_card);

        this.bitmapsToDisplay = bitmaps;
        this.context = context;

        this.imageURLs = imageURLs;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        imageHolder = (LinearLayout) parent.findViewById(R.id.imageHolderLayout);


        if (bitmapsToDisplay.size() > 0)
        {
            Log.i("ImageGalleryCard", "----- Adding images to ScrollView -----");

            int i = 0;
            for (final Bitmap image : bitmapsToDisplay)
            {
                final int imageMarginInDp = 5;
                final String currentImageURL = imageURLs.get(i);

                Log.i("ImageGalleryCard", "Adding image: " + currentImageURL);

                Resources r = context.getResources();
                int marginInPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        imageMarginInDp,
                        r.getDisplayMetrics()
                );

                ImageView imageView = new ImageView(context);
                imageView.setAdjustViewBounds(true);
                imageView.setImageBitmap(image);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                params.setMargins(0, 0, marginInPx, 0);
                imageView.setLayoutParams(params);

                final int currenti = i;
                imageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String imageURL = currentImageURL;

                        Intent intent = new Intent(context.getApplicationContext(), ImageZoomActivity.class);
                        intent.putExtra("imageURL", imageURL);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

                imageHolder.addView(imageView);

                i++;
            }
        }
        else
        {
            // put a generic 'no photos' message in the card instead

            parent.removeAllViews();

            RelativeLayout centerLayout = new RelativeLayout(context);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            centerLayout.setLayoutParams(layoutParams);

            parent.addView(centerLayout);

            TextView messageView = new TextView(context);
            messageView.setText("Predmet nema fotografije");
            messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            messageView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
//            messageView.setPadding(500, 500, 500, 500);
            messageView.setGravity(Gravity.CENTER);

            RelativeLayout.LayoutParams params
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            messageView.setLayoutParams(params);

            centerLayout.addView(messageView);
        }
    }
}
