package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.StoryMsg;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.odelan.yang.aggone.Utils.Constants.REPLY_TYPE_TEXT;

public class StoryMsgAdapter extends RecyclerView.Adapter<StoryMsgAdapter.ViewHolder> {
    Context context;
    List<StoryMsg> data;

    public StoryMsgAdapter(Context context, List<StoryMsg> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_story_message, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        StoryMsg one = data.get(position);
        User other = one.user;
        if (other.photo_url != null && !other.photo_url.isEmpty()) {
            if (other.photo_url.contains("http")){
                Glide.with(context).load(other.photo_url).into(viewHolder.img_avatar);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + other.photo_url).into(viewHolder.img_avatar);
            }
        } else {
            viewHolder.img_avatar.setImageResource(R.mipmap.default_avata);
        }

        long diff = System.currentTimeMillis()/1000 - one.timestamp;
        viewHolder.txt_time.setText("reply:" + MyApp.getMsgPassedTime(diff));

        if(one.reply_type == REPLY_TYPE_TEXT){
            viewHolder.txt_content.setText(one.content);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setDataList(List<StoryMsg> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_avatar;
        TextView        txt_time;
        TextView        txt_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar       =     itemView.findViewById(R.id.img_avatar);
            txt_time           =     itemView.findViewById(R.id.txt_time);
            txt_content            =     itemView.findViewById(R.id.txt_content);
        }
    }
}
