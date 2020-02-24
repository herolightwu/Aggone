package com.odelan.yang.aggone.Adapter.Message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Contact;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    Context context;
    List<Contact> contacts;
    EventListener listener;
    public ContactAdapter(Context context, List<Contact> contacts, EventListener listener) {
        this.context = context;
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contact, viewGroup, false);
        ContactAdapter.ViewHolder vh = new ContactAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (contacts.get(position).user.photo_url != null &&
                !contacts.get(position).user.photo_url.isEmpty()) {
            if(contacts.get(position).user.photo_url.contains("http")){
                Glide.with(context).load(contacts.get(position).user.photo_url).into(viewHolder.img_avata);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + contacts.get(position).user.photo_url).into(viewHolder.img_avata);
            }

        } else {
            viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
        }
        viewHolder.txt_name.setText(contacts.get(position).user.username);
        viewHolder.txt_message.setText(contacts.get(position).message);
        Long delta = System.currentTimeMillis() / 1000 - contacts.get(position).timestamp;
        if (BaseActivity.deltaTimeString(delta).isEmpty()) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(contacts.get(position).timestamp * 1000L);
            String date = DateFormat.format("dd-MM-yyyy", cal).toString();
            viewHolder.txt_time.setText(date);
        } else {
            viewHolder.txt_time.setText(BaseActivity.deltaTimeString(delta));
        }
        if (contacts.get(position).unread_count > 0) {
            viewHolder.layout_unread.setVisibility(View.VISIBLE);
            viewHolder.txt_unread_count.setText(String.valueOf(contacts.get(position).unread_count));
        } else {
            viewHolder.layout_unread.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
        viewHolder.btn_delete.setOnClickListener(v -> {
            if (listener != null) listener.onClickDelete(position);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
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
            btn_delete          =     itemView.findViewById(R.id.btn_delete);
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
