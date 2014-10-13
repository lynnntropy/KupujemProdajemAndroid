package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.InjectView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by omega_000 on 7/17/2014.
 */
public class UserProfileCard extends Card
{
    TextView mUserName;
    TextView mUserEmail;
    TextView mUserNegativeVotes;
    TextView mUserPositiveVotes;

    private Context context;
    private Typeface robotoLight;

    private UserPersonalInfo userPersonalInfo;

    public UserProfileCard(Context context, UserPersonalInfo userPersonalInfo)
    {
        super(context, R.layout.card_user_profile);
        this.context = context;
        this.robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        this.userPersonalInfo = userPersonalInfo;

        Log.i("", "Setting up profile card using data:\n" + userPersonalInfo.toString());
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view)
    {
        mUserName = (TextView) parent.findViewById(R.id.userName);
        mUserEmail = (TextView) parent.findViewById(R.id.userEmail);
        mUserNegativeVotes = (TextView) parent.findViewById(R.id.userNegativeVotes);
        mUserPositiveVotes = (TextView) parent.findViewById(R.id.userPositiveVotes);

        mUserName.setText(userPersonalInfo.userFullName);
        mUserName.setTypeface(robotoLight);

        mUserEmail.setText(userPersonalInfo.userEmail);

        mUserNegativeVotes.setTypeface(robotoLight);
        mUserNegativeVotes.setTextColor(context.getResources().getColor(R.color.main_red));
        mUserPositiveVotes.setTypeface(robotoLight);
        mUserPositiveVotes.setTextColor(context.getResources().getColor(R.color.main_green));

        mUserNegativeVotes.setText("\u25BC" + userPersonalInfo.userNegativeVotes);
        mUserPositiveVotes.setText("\u25B2" + userPersonalInfo.userPositiveVotes);

        TextView phoneNumberLabel = (TextView) parent.findViewById(R.id.phoneNumberLabel);
        TextView phoneNumber = (TextView) parent.findViewById(R.id.phoneNumber);
//        TextView locationLabel = (TextView) parent.findViewById(R.id.locationLabel);
//        TextView location = (TextView) parent.findViewById(R.id.location);

        phoneNumberLabel.setTypeface(robotoLight);
//        locationLabel.setTypeface(robotoLight);
        phoneNumber.setTypeface(robotoLight);
//        location.setTypeface(robotoLight);

        phoneNumber.setText(userPersonalInfo.userPhoneNumber);
//        location.setText(userPersonalInfo.userLocation);
    }
}