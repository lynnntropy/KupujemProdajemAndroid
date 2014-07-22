package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * General utility class. Mainly specific to the project it's contained in.
 */
public class OmegaUtil
{
    public static void initSlidingMenu(final Context context)
    {
        final SlidingMenu navDrawer = new SlidingMenu(context);
        navDrawer.setMode(SlidingMenu.LEFT);
        navDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        navDrawer.setShadowWidthRes(R.dimen.shadow_width);
//        navDrawer.setShadowDrawable(R.drawable.drawer_shadow);
//        navDrawer.setBehindWidth(200);
        navDrawer.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        navDrawer.setFadeDegree(0.35f);
        navDrawer.attachToActivity((Activity) context, SlidingMenu.SLIDING_CONTENT);
        navDrawer.setMenu(R.layout.navdrawer_layout);

        ListView drawerList =  (ListView) navDrawer.findViewById(R.id.drawerList);
        final DrawerListAdapter drawerListAdapter = new DrawerListAdapter(context, navDrawer);
        drawerList.setAdapter(drawerListAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                navDrawer.toggle();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(context, drawerListAdapter.activitiesToList.get(i).activity);
                        context.startActivity(intent);
                    }
                }, 500);
            }
        });
    }
}
