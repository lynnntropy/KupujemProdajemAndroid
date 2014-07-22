package com.omegavesko.kupujemprodajem;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * General utility class. Mainly specific to the project it's contained in.
 */
public class OmegaUtil
{
    public static final String TITLE_FONT_NAME = "RobotoCondensed-Regular.ttf";

    public static void initNavigationDrawer(Activity activity, NavigationDrawerFragment drawerFragment, int fragmentId, DrawerLayout layout)
    {
        activity.getActionBar().setDisplayShowHomeEnabled(true);

        // Set up the drawer.
        drawerFragment.setUp(fragmentId, layout);
    }

    public static void setTitleWithFont(Activity activity, String title, String fontFileName, float textSize)
    {
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(activity, fontFileName, textSize), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = activity.getActionBar();
        actionBar.setTitle(s);

    }

    public static float dpToPixels(Context context, float dp)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;

        return  (int) (fpixels + 0.5f);
    }

    public static class TypefaceSpan extends MetricAffectingSpan
    {
        /** An <code>LruCache</code> for previously loaded typefaces. */
        private static LruCache<String, Typeface> sTypefaceCache =
                new LruCache<String, Typeface>(12);

        private Typeface mTypeface;
        private float fontSize;

        /**
         * Load the {@link Typeface} and apply to a {@link Spannable}.
         */
        public TypefaceSpan(Context context, String typefaceName, float fontSize) {
            mTypeface = sTypefaceCache.get(typefaceName);

            this.fontSize = fontSize;

            if (mTypeface == null) {
                mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                        .getAssets(), String.format("fonts/%s", typefaceName));

                // Cache the loaded Typeface
                sTypefaceCache.put(typefaceName, mTypeface);
            }
        }

        @Override
        public void updateMeasureState(TextPaint p) {
            p.setTypeface(mTypeface);
            p.setTextSize(fontSize);

            // Note: This flag is required for proper typeface rendering
            p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTypeface(mTypeface);
            tp.setTextSize(fontSize);

            // Note: This flag is required for proper typeface rendering
            tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
    }
}
