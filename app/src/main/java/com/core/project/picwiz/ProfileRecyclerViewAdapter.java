package com.core.project.picwiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sanchit on 20-09-2015.
 */
public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileRecyclerViewHolder> {

    private LayoutInflater profileRecyclerViewInflater;
    List<ProfileRecyclerView> data = Collections.emptyList();
    public ProfileRecyclerViewAdapter (Context context, List<ProfileRecyclerView> data) {
        profileRecyclerViewInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ProfileRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = profileRecyclerViewInflater.inflate(R.layout.main_recycler_view_layout, viewGroup, false);
        ProfileRecyclerViewHolder profileRecyclerViewHolder = new ProfileRecyclerViewHolder(root);
        return profileRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileRecyclerViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView post;
        public ProfileRecyclerViewHolder(View itemView) {
            super(itemView);
            post = (ImageView) itemView.findViewById(R.id.rvl_post_profile);
        }
    }
}
