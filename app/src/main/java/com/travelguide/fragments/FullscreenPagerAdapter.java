package com.travelguide.fragments;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.helpers.DeviceDimensionsHelper;

import java.util.ArrayList;

/**
 * @author kprav
 *
 * History:
 *   11/03/2015     kprav       Initial Version
 */
class FullscreenPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mImages;

    public FullscreenPagerAdapter(Context context, ArrayList<String> imageUrlSet) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImages = imageUrlSet;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.fragment_fullscreen_pager_item, container, false);
        ImageView ivPagerImage = (ImageView) view.findViewById(R.id.ivPagerImage);
        Picasso.with(mContext).load(mImages.get(position))
                .resize(DeviceDimensionsHelper.getDisplayWidth(mContext), DeviceDimensionsHelper.getDisplayHeight(mContext))
                .into(ivPagerImage);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
