package com.omegavesko.kupujemprodajem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by omega_000 on 7/15/2014.
 */
public class StyledSpinnerStringAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final String[] values;

    private Typeface robotoLight;

    // prevent out of bounds exception at the end of the list
    @Override
    public int getCount() {
        return values.length;
    }

    public StyledSpinnerStringAdapter(Context context, String[] values)
    {
        super(context, R.layout.kp_spinner_dropdown_item, R.id.textView1, values);
        this.context = context;
        this.values = values;

        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spinnerItemView = inflater.inflate(R.layout.kp_spinner_item, parent, false);

        TextView text = (TextView) spinnerItemView.findViewById(R.id.textView1);
        text.setText(values[position]);
        text.setTypeface(robotoLight);

        return spinnerItemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.kp_spinner_dropdown_item, parent, false);
        TextView text = (TextView) row.findViewById(R.id.textView1);

        text.setTypeface(robotoLight);
        text.setText(values[position]);
        return row;
    }

}
