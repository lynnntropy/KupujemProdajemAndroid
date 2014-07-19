package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/19/2014.
 */
public class ImageGalleryCard extends Card
{
    public List<Bitmap> bitmapsToDisplay;

    ScrollView galleryScrollView;

    private Context context;

    private LinearLayout imageHolder;

    private void init() {}

    public ImageGalleryCard(Context context, List<Bitmap> bitmaps)
    {
        super(context, R.layout.gallery_card);


        this.bitmapsToDisplay = bitmaps;
        this.context = context;

        // TODO: DEBUG CODE REMOVE ASAP

        for (int i = 0; i < 5; i++)
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // scale the image down so we don't run out of memory for a stupidly large bitmap

            Bitmap icon = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.large_debug_image, options);
            bitmapsToDisplay.add(icon);
        }
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        imageHolder = (LinearLayout) parent.findViewById(R.id.imageHolderLayout);

        for (Bitmap image: bitmapsToDisplay)
        {
            final int imageMarginInDp = 5;

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

            imageHolder.addView(imageView);
        }
    }
}
