package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DrawerListAdapter extends ArrayAdapter<String> {
    private final Context context;

    class ActivityToLaunch
    {
        String name;
        Class<?> activity;

        public ActivityToLaunch(String name, Class<?> activity)
        {
            this.name = name;
            this.activity = activity;
        }
    }

    List<ActivityToLaunch> activitiesToList;
    private Typeface robotoLight;
    private SlidingMenu drawerInstance;

    public DrawerListAdapter(Context context)
    {
        super(context, R.layout.drawer_row);
        this.context = context;

        activitiesToList = new ArrayList<ActivityToLaunch>();
        activitiesToList.add(new ActivityToLaunch("Pretraga", HomeActivity.class));
        activitiesToList.add(new ActivityToLaunch("Kategorije", KategorijeActivity.class));

//        this.drawerInstance = drawerInstance;

        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    @Override
    public int getCount()
    {
        return activitiesToList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_row, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.activityName);
        textView.setText(activitiesToList.get(position).name);
        textView.setTypeface(robotoLight);

        if (context.getClass().equals(activitiesToList.get(position).activity))
        {
            // this is the current activity!

            LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.rowRootLayout);
            layout.setBackgroundResource(R.drawable.selected_drawer_item);
        }

        return rowView;
    }
}