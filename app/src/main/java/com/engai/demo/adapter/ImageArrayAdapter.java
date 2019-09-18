package com.engai.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.engai.demo.R;
import com.engai.demo.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageArrayAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private MainActivity parentActivity;
    ViewHolder holder = null;
    private List<String> items = new ArrayList<>();
    //private ArrayList<Product> itemsInApp = new ArrayList<>();
    Context mContext;
    private float qntyValue = 1f;


    public ImageArrayAdapter(Context context, List<String> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
        if (context instanceof MainActivity) {
            parentActivity = (MainActivity) context;
            mContext = context;
        }
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return items;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = mInflater.inflate(R.layout.list_grid_view, parent, false);
            holder = new ViewHolder();
            holder.itemImage = (ImageView) row.findViewById(R.id.ivgridImage);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final String itemDetails = items.get(position);

        Glide.with(parentActivity)
                .load(itemDetails)
                .placeholder(R.drawable.ic_profile)
                .into(holder.itemImage);


        return row;
    }

    public static class ViewHolder {
        ImageView itemImage;
    }


}
