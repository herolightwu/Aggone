package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SportSelectAdapter extends RecyclerView.Adapter<SportSelectAdapter.ViewHolder> {
    Context context;
    List<Sport> sports;
    EventListener listener;
    public SportSelectAdapter(Context context, List<Sport> sports, EventListener listener) {
        this.context = context;
        this.sports = sports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SportSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sport_select, viewGroup, false);
        SportSelectAdapter.ViewHolder vh = new SportSelectAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Story fd = AppData.last_stories.get(sports.get(position).id);
        if (fd != null) {
            viewHolder.img_avatar.setVisibility(View.VISIBLE);
            if(fd.user != null && fd.user.photo_url != null && !fd.user.photo_url.isEmpty()){
//                viewHolder.img_avatar.setVisibility(View.VISIBLE);
                if (fd.user.photo_url.contains("http")){
                    Glide.with(context).load(fd.user.photo_url).into(viewHolder.img_avatar);
                } else{
                    Glide.with(context).load(API.baseUrl + API.imgDirUrl + fd.user.photo_url).into(viewHolder.img_avatar);
                }
            } else {
                viewHolder.img_avatar.setImageResource(R.mipmap.default_avata);
            }
        } else{
            viewHolder.img_avatar.setVisibility(View.GONE);
        }
        changeTextViewStatus(viewHolder.txt_select, sports.get(position).selected, sports.get(position).name);
        viewHolder.img_icon.setImageResource(sports.get(position).icon);
        viewHolder.txt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
        viewHolder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Story st = AppData.last_stories.get(sports.get(position).id);
                if (listener != null && st != null) {
                    if (st.user != null && st.timeend > System.currentTimeMillis()/1000)
                        listener.onClickStory(st);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        CircleImageView img_icon, img_avatar;
        TextView        txt_select;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            img_icon        =     itemView.findViewById(R.id.img_icon);
            img_avatar      =     itemView.findViewById(R.id.img_avatar);
            txt_select      =     itemView.findViewById(R.id.txt_select);
        }
    }

    private void changeTextViewStatus(TextView txt, boolean bSelect, String name){
        if(bSelect){
            txt.setBackgroundResource(R.drawable.rounded_selected);
            txt.setTextColor(Color.parseColor("#FFFFFF"));
            //txt.setText("Selected");
        } else{
            txt.setBackgroundResource(R.drawable.rounded_select);
            txt.setTextColor(Color.parseColor("#B8DB68"));
            //txt.setText("Select");
        }
        txt.setText(name);
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickStory(Story st);
    }
}
