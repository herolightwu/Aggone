package com.odelan.yang.aggone.Adapter.Message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Contact;
import com.odelan.yang.aggone.Model.Notification;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    List<Notification> notifications;
    EventListener listener;
    public NotificationAdapter(Context context, List<Notification> notifications, EventListener listener) {
        this.context = context;
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_notification, viewGroup, false);
        NotificationAdapter.ViewHolder vh = new NotificationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (notifications.get(position).sender.photo_url != null &&
                !notifications.get(position).sender.photo_url.isEmpty()) {
            if (notifications.get(position).sender.photo_url.contains("http")){
                Glide.with(context).load(notifications.get(position).sender.photo_url).into(viewHolder.img_avata);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + notifications.get(position).sender.photo_url).into(viewHolder.img_avata);
            }
        } else {
            viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
        }
        viewHolder.txt_name.setText(notifications.get(position).sender.username);

        if(notifications.get(position).type.equals("like")){
            viewHolder.txt_message.setText(context.getResources().getString(R.string.notification_like));
        } else if (notifications.get(position).type.equals("follow")){
            viewHolder.txt_message.setText(context.getResources().getString(R.string.notification_follow));
        } else if(notifications.get(position).type.equals("chat")){
            viewHolder.txt_message.setText(context.getResources().getString(R.string.notification_chat));
        } else{
            viewHolder.txt_message.setText(notifications.get(position).content_msg);
        }
        Long delta = System.currentTimeMillis() / 1000 - notifications.get(position).timestamp;
        if (BaseActivity.deltaTimeString(delta).isEmpty()) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(notifications.get(position).timestamp * 1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            viewHolder.txt_time.setText(date);
        } else {
            viewHolder.txt_time.setText(BaseActivity.deltaTimeString(delta));
        }
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
        viewHolder.btn_delete.setOnClickListener(v -> {
            if (listener != null) listener.onClickDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setDataList(List<Notification> data){
        notifications = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        CircleImageView img_avata;
        TextView txt_name;
        TextView txt_message;
        TextView txt_time;
        TextView txt_unread_count;
        LinearLayout layout_unread;
        LinearLayout btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            layout_parent       =     itemView.findViewById(R.id.layout_parent);
            img_avata           =     itemView.findViewById(R.id.img_avata);
            txt_name            =     itemView.findViewById(R.id.txt_name);
            txt_message         =     itemView.findViewById(R.id.txt_message);
            txt_time            =     itemView.findViewById(R.id.txt_time);
            txt_unread_count    =     itemView.findViewById(R.id.txt_unread_count);
            layout_unread       =     itemView.findViewById(R.id.layout_unread);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickDelete(int index);
    }
}
