package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    Context context;
    List<Feed> feeds;
    EventListener listener;
    public FeedAdapter(Context context, List<Feed> feeds, EventListener listener) {
        this.context = context;
        this.feeds = feeds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_feed_constrain, viewGroup, false);
        FeedAdapter.ViewHolder vh = new FeedAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        Feed feed = feeds.get(position);
        if(feed.user != null){
            if (feed.user.photo_url != null && !feed.user.photo_url.isEmpty()) {
                if (feed.user.photo_url.contains("http")){
                    Glide.with(context).load(feed.user.photo_url).into(viewHolder.img_avata);
                } else{
                    Glide.with(context).load(API.baseUrl + API.imgDirUrl + feed.user.photo_url).into(viewHolder.img_avata);
                }
            } else {
                viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
            }
            viewHolder.txt_name.setText(feed.user.username);
        }

        if (feed.like) {
            viewHolder.img_like.setImageResource(R.mipmap.main_like_active);
        } else {
            viewHolder.img_like.setImageResource(R.mipmap.main_like_normal);
        }
        if (feed.bookmark) {
            viewHolder.img_bookmark.setImageResource(R.mipmap.main_bookmark_active);
        } else {
            viewHolder.img_bookmark.setImageResource(R.mipmap.main_bookmark_normal);
        }

        if(feed.type == Constants.NEWS){
            viewHolder.btn_play.setVisibility(View.GONE);
            if(feed.articles != null && feed.articles.length() > 0){
                String[] paths = feed.articles.split(",");
                if (paths[0].contains("http")){
                    Glide.with(context).load(paths[0]).into(viewHolder.img_thumbnail);
                } else{
                    Glide.with(context).load(API.baseUrl + API.articleDirUrl + paths[0]).into(viewHolder.img_thumbnail);
                }
            }

        } else{
            viewHolder.btn_play.setVisibility(View.VISIBLE);
            if (feed.thumbnail_url.contains("http")){
                Glide.with(context).load(feed.thumbnail_url).into(viewHolder.img_thumbnail);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + feed.thumbnail_url).into(viewHolder.img_thumbnail);
            }
        }

        viewHolder.txt_title.setText(feed.title);
        if(feed.desc_str == null || feed.desc_str.length() == 0) {
            viewHolder.txt_desc.setVisibility(View.GONE);
        } else{
            viewHolder.txt_desc.setVisibility(View.VISIBLE);
            viewHolder.txt_desc.setText(feed.desc_str);
        }
        viewHolder.txt_view_count.setText(String.valueOf(feed.view_count));
        viewHolder.txt_like_count.setText(String.valueOf(feed.like_count));
        long delta = System.currentTimeMillis() / 1000 - feed.timestamp;
        if (BaseActivity.deltaTimeString(delta).isEmpty()) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(feed.timestamp * 1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            viewHolder.txt_time.setText(date);
        } else {
            viewHolder.txt_time.setText(BaseActivity.deltaTimeString(delta));
        }
        viewHolder.img_avata.setOnClickListener(v -> {
            if (listener != null) listener.onClickProfile(position);
        });
        viewHolder.txt_name.setOnClickListener(v -> {
            if (listener != null) listener.onClickProfile(position);
        });
        viewHolder.btn_menu.setOnClickListener(v -> {
            if (listener != null) listener.onClickMenu(viewHolder.btn_menu, position);
        });
        viewHolder.layout_bookmark.setOnClickListener(v -> {
            if (listener != null) listener.onClickBookmark(position);
        });
        viewHolder.layout_like.setOnClickListener(v -> {
            if (listener != null) listener.onClickLike(position);
        });
        viewHolder.btn_play.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
        if (feed.tagged != null && feed.tagged.length() > 0){
            viewHolder.btn_tagged.setVisibility(View.VISIBLE);
        } else{
            viewHolder.btn_tagged.setVisibility(View.GONE);
        }
        viewHolder.btn_tagged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) listener.onClickTagged(position);
            }
        });
        viewHolder.img_thumbnail.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void setDataList(List<Feed> data){
        feeds.clear();
        feeds.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_parent;
        CircleImageView img_avata;
        TextView txt_name;
        ImageView btn_menu;
        ImageView img_thumbnail;
        TextView txt_title;
        TextView txt_desc;
        TextView txt_view_count;
        LinearLayout layout_like;
        TextView txt_like_count;
        LinearLayout layout_bookmark;
        ImageView img_bookmark;
        ImageView img_like;
        TextView txt_time;
        ImageView btn_play;
        LinearLayout btn_tagged;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            img_avata       =     itemView.findViewById(R.id.img_avata);
            txt_name        =     itemView.findViewById(R.id.txt_name);
            btn_menu        =     itemView.findViewById(R.id.btn_menu);
            img_thumbnail   =     itemView.findViewById(R.id.img_thumbnail);
            txt_title       =     itemView.findViewById(R.id.txt_title);
            txt_desc        =     itemView.findViewById(R.id.txt_description);
            txt_view_count  =     itemView.findViewById(R.id.txt_view_count);
            layout_like     =     itemView.findViewById(R.id.layout_like);
            txt_like_count  =     itemView.findViewById(R.id.txt_like_count);
            layout_bookmark =     itemView.findViewById(R.id.layout_bookmark);
            img_like        =     itemView.findViewById(R.id.img_like);
            img_bookmark    =     itemView.findViewById(R.id.img_bookmark);
            txt_time        =     itemView.findViewById(R.id.txt_time);
            btn_play        =     itemView.findViewById(R.id.btn_play);
            btn_tagged      =     itemView.findViewById(R.id.btn_tagged);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickMenu(View view, int index);
        void onClickLike(int index);
        void onClickBookmark(int index);
        void onClickProfile(int index);
        void onClickTagged(int index);
    }
}
