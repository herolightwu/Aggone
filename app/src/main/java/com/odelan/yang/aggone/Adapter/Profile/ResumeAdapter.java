package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.Resume;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;

import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ViewHolder> {

    Context context;
    public User user;
    List<Resume> resumes;
    EventListener listener;
    public ResumeAdapter(Context context, List<Resume> data, EventListener listener) {
        this.context = context;
        this.resumes = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_resume, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (position >= resumes.size()){
            viewHolder.add_more.setVisibility(View.VISIBLE);
            viewHolder.parent.setVisibility(View.INVISIBLE);
            viewHolder.add_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) listener.onAddResume();
                }
            });
            return;
        }
        viewHolder.add_more.setVisibility(View.GONE);
        viewHolder.parent.setVisibility(View.VISIBLE);
        if (user.photo_url != null && !user.photo_url.isEmpty()) {
            if (user.photo_url.contains("http")){
                Glide.with(context).load(user.photo_url).into(viewHolder.iv_image);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + user.photo_url).into(viewHolder.iv_image);
            }
        } else {
            viewHolder.iv_image.setImageResource(R.mipmap.profile);
        }
        viewHolder.tv_name.setText(user.username);
        if (resumes.get(position).user_id.equals(AppData.user.id)) {
            viewHolder.iv_image.setOnLongClickListener(v -> {
                if (listener != null) listener.onLongClickItem(position);
                return false;
            });
            viewHolder.btn_down.setVisibility(View.GONE);
        } else{
            viewHolder.btn_down.setVisibility(View.VISIBLE);
            viewHolder.btn_down.setOnClickListener(v -> {
                if (listener != null) listener.onClickItem(position);
            });
        }
    }

    public void setDataList(List<Resume> data){
        resumes = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (user.id.equals(AppData.user.id)){
            return resumes.size() + 1;
        }
        return resumes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_name;
        Button btn_down;
        ImageView add_more;
        RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image   =     itemView.findViewById(R.id.img_photo);
            tv_name    =     itemView.findViewById(R.id.txt_name);
            btn_down   =     itemView.findViewById(R.id.btn_download);
            add_more   =     itemView.findViewById(R.id.add_more);
            parent     =     itemView.findViewById(R.id.info_layout);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onLongClickItem(int index);
        void onAddResume();
    }
}
