package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class WebViewCard extends Card
{
    public String viewHTML;

    WebView mCardWebView;

    private Context context;

    private Typeface robotoLight;

    public WebViewCard(Context context, String viewHTML)
    {
        super(context, R.layout.webview_card);
        this.viewHTML = viewHTML;
        this.context = context;

        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    private void init()
    {
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Log.w("CategoryCard", "Category " + pageData.title + " clicked!");
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        mCardWebView = (WebView) parent.findViewById(R.id.cardWebView);
//        mCardWebView.getSettings().setLoadWithOverviewMode(true);
//        mCardWebView.getSettings().setUseWideViewPort(true);
        mCardWebView.loadData(viewHTML, "text/html; charset=UTF-8", null);
    }
}