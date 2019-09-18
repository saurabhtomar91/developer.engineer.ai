package com.engai.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.engai.demo.R;
import com.engai.demo.model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {

    private Context activity;
    private ArrayList<UserModel> items = new ArrayList<>();
    private ImageArrayAdapter imageArrayAdapter;

    public UserAdapter(Context context, ArrayList<UserModel> items) {
        this.items = items;
        this.activity = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_common, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserModel myUser = items.get(position);

        if(myUser.getImageUrl() != null){
            Glide.with(activity)
                    .load(myUser.getImageUrl())
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.userImage);
        }
        if(myUser.getUserName() != null){
            holder.userName.setText(myUser.getUserName());
        }

        /*
        Odd/Even Login for Showing on UI
         */
        if(myUser.getImageList().size() % 2 != 0){

            holder.oddImage.setVisibility(View.VISIBLE); // Visible the odd image container when the imagelist size is odd
            //Check if 1 image size show single Odd image else show with three image set
            if(myUser.getImageList().size() == 1){
                Glide.with(activity)
                        .load(myUser.getImageList().get(0))
                        .useAnimationPool(true)
                        .into(holder.oddImage);
            }else{
                Glide.with(activity)
                        .load(myUser.getImageList().get(0))
                        .useAnimationPool(true)
                        .into(holder.oddImage);
                myUser.getImageList().remove(0); // Remove First Element from the Imagelist because we already shown it to Odd Image Container
                imageArrayAdapter = new ImageArrayAdapter(activity, myUser.getImageList()); // Rest imagelist pass to ImageArray Adapter for populating the View
                holder.gridView.setAdapter(imageArrayAdapter);
            }
        }else{
            //Hide Odd Image Container if ImageList size is even
            holder.oddImage.setVisibility(View.GONE);
            imageArrayAdapter = new ImageArrayAdapter(activity, myUser.getImageList());
            holder.gridView.setAdapter(imageArrayAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        Object listItem = items.get(position);
        return listItem.hashCode();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.clearAnimation();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private ImageView oddImage, userImage;
        private GridView gridView;

        public ViewHolder(View view) {
            super(view);

            userImage = (ImageView) view.findViewById(R.id.iv_logo);
            userName = (TextView) view.findViewById(R.id.tvUserName);
            oddImage = (ImageView) view.findViewById(R.id.iv_odd);
            gridView = (GridView) view.findViewById(R.id.gridView);
        }
    }

}
