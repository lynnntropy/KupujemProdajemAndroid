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
public class LoadMoreCard extends Card
{
    private Context context;

    private void init() {}

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

        TextView text = (TextView) parent.findViewById(R.id.loadMoreText);
        text.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
    }
}