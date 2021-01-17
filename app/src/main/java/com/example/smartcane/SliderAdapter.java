package com.example.smartcane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    //arrays
    public int[] slide_image = {
            R.drawable.gpss,
            R.drawable.alerticon,
            R.drawable.circlecropped
    };

    public String[] slide_headings = {
            "LOCATE BLIND PERSON               " +
                    "              " +
                    "             " +
                    "With just a single press of SmartCane app, the blind's location can easily be tracked",
            "GET NOTIFIED BY BLIND              " +
                    "                   " +
                    "You will be notified if the person under your care is in trouble",
            "EMERGENCY CONTACTS AND CALL         " +
                    "                " +
                    "               " +
                    "Make emergency calls to emergency department as well as add contact details of your close family" +
                    "and friends so that they are also aware of the person under your care's risk"

    };

    public String[] slide_descs = {
            "LOCATE BLIND PERSON\n\n\n With just a single press of\n SmartCane app, the blind's\n location can easily be tracked",
            "GET NOTIFIED BY BLIND\n\n\n You will be notified if the person under your care is in trouble",
            "EMERGENCY CONTACTS AND CALL \n\n Add contact details of your close family and friends so that they are also aware of the person under your care's risk"

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_headings);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_descs);

        slideImageView.setImageResource(slide_image[position]);
        slideHeading.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }
}
