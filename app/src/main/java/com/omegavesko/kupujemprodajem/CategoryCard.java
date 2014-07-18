package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class CategoryCard extends Card
{
    private WebsiteHandler.Category category;
    private Context context;

    private Typeface robotoLight;

    public CategoryCard(Context context, WebsiteHandler.Category categoryToDisplay)
    {
        super(context, R.layout.category_card_layout);
        this.category = categoryToDisplay;
        this.context = context;

        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    private void init()
    {
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Log.w("CategoryCard", "Category " + category.nameString + " clicked!");
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView categoryLabel = (TextView) parent.findViewById(R.id.categoryName);

        if (categoryLabel != null)
        {
            categoryLabel.setText(category.nameString);
            categoryLabel.setTypeface(robotoLight);
        }
    }
}