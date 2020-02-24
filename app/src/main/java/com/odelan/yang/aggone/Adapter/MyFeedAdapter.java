package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder> {
    Context context;
    List<Feed> feeds;
    EventListener listener;
    public MyFeedAdapter(Context context, List<Feed> feeds, EventListener listener) {
        this.context = context;
        this.feeds = feeds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_my_feed, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Feed feed = feeds.get(position);
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
        long delta = System.currentTimeMillis() / 1000 - feed.timestamp;
        if (BaseActivity.deltaTimeString(delta).isEmpty()) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(feed.timestamp * 1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            viewHolder.txt_time.setText(date);
        } else {
            viewHolder.txt_time.setText(BaseActivity.deltaTimeString(delta));
        }
        viewHolder.layout_bookmark.setOnClickListener(v -> {
            if (listener != null) listener.onClickBookmark(position);
        });
        viewHolder.layout_like.setOnClickListener(v -> {
            if (listener != null) listener.onClickLike(position);
        });
        viewHolder.btn_play.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
        if (!feed.user.id.equals(AppData.user.id)) {
            viewHolder.btn_menu.setVisibility(View.INVISIBLE);
            viewHolder.btn_report.setVisibility(View.VISIBLE);
        } else{
            viewHolder.btn_menu.setVisibility(View.VISIBLE);
            viewHolder.btn_report.setVisibility(View.INVISIBLE);
        }
        viewHolder.btn_menu.setOnClickListener(v -> {
            //if (listener != null) listener.onClickMenu(position);
            showMenu(v, position);
        });
        viewHolder.btn_report.setOnClickListener(v -> {
            if (listener != null) listener.onClickReport(position);
        });
        viewHolder.img_thumbnail.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    public void showMenu(View v, final int position)
    {
        PopupMenu popup = new PopupMenu(context,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_private: {
                        if (listener != null){
                            listener.onClickPrivate(position, true);
                        }
                    }
                    break;
                    case R.id.menu_public: {
                        if (listener != null){
                            listener.onClickPrivate(position, false);
                        }
                    }
                    break;
                    case R.id.menu_delete: {
                        // Edit Action
                        if (listener != null) listener.onClickDelete(position);
                    }
                    break;
                }
                return false;
            }
        });// to implement on click event on items of menu
        Feed feed = feeds.get(position);
        MenuInflater inflater = popup.getMenuInflater();
        if (feed.mode == Constants.FEED_PUBLIC){
            inflater.inflate(R.menu.video_tap_menu, popup.getMenu());
        } else{
            inflater.inflate(R.menu.video_tap_public, popup.getMenu());
        }
        popup.show();
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void setDataList(List<Feed> data){
        feeds = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_parent;
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
        ImageView btn_menu;
        ImageView btn_report;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            img_thumbnail   =     itemView.findViewById(R.id.img_thumbnail);
            txt_title       =     itemView.findViewById(R.id.txt_title);
            txt_desc        =     itemView.findViewById(R.id.txt_description);
            txt_view_count  =     itemView.findViewById(R.id.txt_view_count);
            layout_like     =     itemView.findViewById(R.id.layout_like);
            txt_like_count  =     itemView.findViewById(R.id.txt_like_count);
            layout_bookmark =     itemView.findViewById(R.id.layout_bookmark);
            img_bookmark    =     itemView.findViewById(R.id.img_bookmark);
            img_like        =     itemView.findViewById(R.id.img_like);
            txt_time        =     itemView.findViewById(R.id.txt_time);
            btn_play        =     itemView.findViewById(R.id.btn_play);
            btn_menu        =     itemView.findViewById(R.id.btn_menu);
            btn_report      =     itemView.findViewById(R.id.btn_report);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickLike(int index);
        void onClickBookmark(int index);
        void onClickDelete(int index);
        void onClickPrivate(int index, boolean flag);
        void onClickReport(int index);
    }
}
