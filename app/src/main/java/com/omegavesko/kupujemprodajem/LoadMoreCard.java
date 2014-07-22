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

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/19/2014.
 */
public class LoadMoreCard extends Card
{
    private Context context;

    private void init() {}

    public TextView promptText;
    public SmoothProgressBar progressBar;

    public LoadMoreCard(Context context)
    {
        super(context, R.layout.card_loadmore);
        this.context = context;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        // er, nothing to set up? :P
        // maybe just change the font..

        promptText = (TextView) parent.findViewById(R.id.loadMoreText);
        promptText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));

        progressBar = (SmoothProgressBar) parent.findViewById(R.id.loadMoreProgressBar);
        progressBar.setAlpha(0f);

//        this.getCardView().setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Log.i("", "Load more card clicked!");
//            }
//        });
    }
}