package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class SearchResultCard extends Card
{
    public SearchResult result;
    private Context context;

    private Typeface robotoLight;

    public SearchResultCard(Context context, SearchResult resultToDisplay)
    {
        super(context, R.layout.searchresult_card_layout);
        this.result = resultToDisplay;
        this.context = context;

        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    private void init()
    {
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Log.w("CategoryCard", "Category " + result.itemName + " clicked!");
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView itemDesctiption = (TextView) parent.findViewById(R.id.itemDescription);
        TextView itemNameView = (TextView) parent.findViewById(R.id.itemNameView);
        ImageView itemThumbView = (ImageView) parent.findViewById(R.id.itemThumbnailView);

        TextView itemLocation = (TextView) parent.findViewById(R.id.itemLocation);
        TextView itemPrice = (TextView) parent.findViewById(R.id.itemPrice);

        if (itemDesctiption != null)
        {
            itemThumbView.setImageBitmap(result.itemThumbnail);
            itemThumbView.setScaleType(ImageView.ScaleType.FIT_START);
            itemThumbView.setAdjustViewBounds(true);

            itemDesctiption.setText(result.itemDesc);
            itemDesctiption.setTypeface(robotoLight);

            itemNameView.setText(result.itemName);
            itemNameView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"));

            itemLocation.setText("Mesto: " + result.itemLocation);
            itemLocation.setTypeface(robotoLight);

            itemPrice.setText(result.itemPrice);
            itemPrice.setTypeface(robotoLight);
        }
    }
}