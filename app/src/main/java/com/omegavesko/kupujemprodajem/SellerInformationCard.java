package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class SellerInformationCard extends Card
{
    public ItemPageData pageData;

//    @InjectView(R.id.sellerName)
    TextView mSellerName;
//    @InjectView(R.id.sellerLocation)
    TextView mSellerLocation;
//    @InjectView(R.id.sellerPhoneNumber)
    ImageView mSellerPhoneNumber;
//    @InjectView(R.id.sellerYesVotes)
    TextView mSellerYesVotes;
//    @InjectView(R.id.sellerNoVotes)
    TextView mSellerNoVotes;

    private Context context;

    private Typeface robotoLight;

    public SellerInformationCard(Context context, ItemPageData pageData)
    {
        super(context, R.layout.seller_information_card);
        this.pageData = pageData;
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

        mSellerName = (TextView) parent.findViewById(R.id.sellerName);
        mSellerLocation = (TextView) parent.findViewById(R.id.sellerLocation);
        mSellerPhoneNumber = (ImageView) parent.findViewById(R.id.sellerPhoneNumber);
        mSellerYesVotes = (TextView) parent.findViewById(R.id.sellerYesVotes);
        mSellerNoVotes = (TextView) parent.findViewById(R.id.sellerNoVotes);

        //Retrieve elements
        mSellerName.setText(pageData.memberName);
        mSellerName.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));

        mSellerLocation.setText(pageData.memberLocation);
        mSellerLocation.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));

        mSellerPhoneNumber.setImageBitmap(pageData.memberPhoneNumber);
        mSellerPhoneNumber.setScaleType(ImageView.ScaleType.FIT_START);

        mSellerYesVotes.setText(pageData.memberYesVotes);
        mSellerYesVotes.setTypeface(robotoLight);

        mSellerNoVotes.setText(pageData.memberNoVotes);
        mSellerNoVotes.setTypeface(robotoLight);

    }
}