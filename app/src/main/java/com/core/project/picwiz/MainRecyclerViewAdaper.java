package com.core.project.picwiz;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sanchit Samuel on 20-09-2015.
 */
public class MainRecyclerViewAdaper extends RecyclerView.Adapter<MainRecyclerViewAdaper.MainRecyclerViewHolder> {

    private LayoutInflater mainRecyclerViewInflater;
    List<MainRecyclerView> data = Collections.emptyList();
    public MainRecyclerViewAdaper (Context context, List<MainRecyclerView> data) {
         mainRecyclerViewInflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View root = mainRecyclerViewInflater.inflate(R.layout.main_recycler_view_layout, parent, false);
        //READ THIS: ^ here its not accepting the work parent i don't know what else to put;
        //MainRecyclerViewHolder mainRecyclerViewHolder = new MainRecyclerViewHolder(root);
        //return mainRecyclerViewHolder;
        return null;
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder viewHolder, int i) {
        //
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView post;
        TextView location;
        TextView caption;
        public MainRecyclerViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.rvl_username);
            post = (ImageView) itemView.findViewById(R.id.rvl_post);
            location = (TextView) itemView.findViewById(R.id.rvl_location);
            caption = (TextView) itemView.findViewById(R.id.rvl_caption);
        }
    }
}
