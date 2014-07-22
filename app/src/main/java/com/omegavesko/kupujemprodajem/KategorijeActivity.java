package com.omegavesko.kupujemprodajem;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.omegavesko.kupujemprodajem.R;

public class KategorijeActivity extends Activity  implements KategorijeFragment.OnFragmentInteractionListener, ItemSearchFragment.OnFragmentInteractionListener
{

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        // do nothing, lol
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategorije);

        getActionBar().setDisplayShowHomeEnabled(false);

        // set up SlidingMenu
//        SlidingMenu navDrawer = new SlidingMenu(this);
//        navDrawer.setMode(SlidingMenu.LEFT);
//        navDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
////        navDrawer.setShadowWidthRes(R.dimen.shadow_width);
////        navDrawer.setShadowDrawable(R.drawable.drawer_shadow);
////        navDrawer.setBehindWidth(200);
//        navDrawer.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        navDrawer.setFadeDegree(0.35f);
//        navDrawer.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        navDrawer.setMenu(R.layout.navdrawer_layout);
//
//        ListView drawerList =  (ListView) navDrawer.findViewById(R.id.drawerList);
//        drawerList.setAdapter(
//                new DrawerListAdapter(this, navDrawer)
//        );

        OmegaUtil.initSlidingMenu(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new KategorijeFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kategorije, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
