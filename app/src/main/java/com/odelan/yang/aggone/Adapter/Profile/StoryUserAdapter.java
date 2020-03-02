package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.StoryView;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryUserAdapter extends RecyclerView.Adapter<StoryUserAdapter.ViewHolder> {
    Context context;
    List<StoryView> data;
    EventListener listener;
    public StoryUserAdapter(Context context, List<StoryView> data, EventListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_story_view_user, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        StoryView one = data.get(position);
        if (one.user!= null && one.user.photo_url != null && !one.user.photo_url.isEmpty()) {
            if (one.user.photo_url.contains("http")){
                Glide.with(context).load(one.user.photo_url).into(viewHolder.img_avata);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + one.user.photo_url).into(viewHolder.img_avata);
            }
        } else {
            viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
        }
        if(one.reply_count > 0){
            viewHolder.img_msg.setVisibility(View.VISIBLE);
        } else{
            viewHolder.img_msg.setVisibility(View.GONE);
        }
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setDataList(List<StoryView> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        CircleImageView img_avata;
        ImageView       img_msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent       =     itemView.findViewById(R.id.layout_parent);
            img_avata           =     itemView.findViewById(R.id.img_photo);
            img_msg            =     itemView.findViewById(R.id.img_msg);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
