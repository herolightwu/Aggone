package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 1/5/2018.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private Listener mListener;

    private List<VideoItem> listData;

    public YoutubeAdapter(Context context, List<VideoItem> data, Listener listener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listData = data;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_youtube, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(listData.get(position).thumbnailURL).into(holder.video_thumbnail);
        holder.video_title.setText(listData.get(position).title);
        holder.video_description.setText(listData.get(position).description);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setDataList(List<VideoItem> data){
        listData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView video_thumbnail;
        TextView video_title;
        TextView video_description;
        public ViewHolder(View itemView) {
            super(itemView);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            video_title = itemView.findViewById(R.id.video_title);
            video_description = itemView.findViewById(R.id.video_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) mListener.onItemClick(getAdapterPosition());
        }
    }

    public interface Listener {
        void onItemClick(int position);
    }
}