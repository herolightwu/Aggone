package com.odelan.yang.aggone.Adapter.LookUp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.DetailsAdapter;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LookingPlayerAdapter extends RecyclerView.Adapter<LookingPlayerAdapter.ViewHolder> {
    Context context;
    List<User> users = new ArrayList<>();
    EventListener listener;
    public LookingPlayerAdapter(Context context, List<User> data, EventListener listener) {
        this.context = context;
        this.users.addAll(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_looking_player, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final User user = users.get(position);
        if (user.photo_url != null && !user.photo_url.isEmpty()) {
            if (user.photo_url.contains("http")){
                Glide.with(context).load(user.photo_url).into(viewHolder.img_avata);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + user.photo_url).into(viewHolder.img_avata);
            }
        } else {
            viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
        }
        viewHolder.txt_name.setText(user.username);
        viewHolder.btn_sport.setText(((BaseActivity)context).getSportName(user.sport_id));
        viewHolder.btn_group.setText(((BaseActivity)context).getTypeName(user.group_id));
        viewHolder.btn_specialty.setText(user.contract);

        final List<Pair<String, String>> data = new ArrayList<>();
        data.add(Pair.create(context.getString(R.string.club), user.club));
        data.add(Pair.create("", ""));
        data.add(Pair.create(context.getString(R.string.category), user.category));
        data.add(Pair.create("", ""));
        data.add(Pair.create(context.getString(R.string.position), user.position));
        data.add(Pair.create("", ""));
        viewHolder.setList(data);
        API.getSportResultSummary(context, user, new APICallback<List<Pair<String, Integer>>>() {
            @Override
            public void onSuccess(List<Pair<String, Integer>> response) {
                List<Skill> results = ((BaseActivity)context).getSportSkills(user.sport_id, response);
                data.set(1, Pair.create(
                        results.get(0).description,
                        String.valueOf(results.get(0).value)));
                data.set(3, Pair.create(
                        results.get(1).description,
                        String.valueOf(results.get(1).value)));
                data.set(5, Pair.create(
                        results.get(2).description,
                        String.valueOf(results.get(2).value)));
                viewHolder.setList(data);
            }
            @Override
            public void onFailure(String error) {
            }
        });

        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setData(List<User> data) {
        users.clear();
        for (int i = 0; i < data.size(); i++){
            if(data.get(i).available_club == 1){
                users.add(data.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        RecyclerView recycler_view;
        DetailsAdapter adapter;
        CircleImageView img_avata;
        TextView txt_name;
        Button btn_group;
        Button btn_sport;
        Button btn_specialty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            recycler_view   =     itemView.findViewById(R.id.recycler_view);
            img_avata   =     itemView.findViewById(R.id.img_avata);
            txt_name   =     itemView.findViewById(R.id.txt_name);
            btn_group   =     itemView.findViewById(R.id.btn_position);
            btn_sport   =     itemView.findViewById(R.id.btn_sport);
            btn_specialty   =     itemView.findViewById(R.id.btn_club);

            adapter = new DetailsAdapter(context, new ArrayList<>(), null);
            recycler_view.setLayoutManager(new GridLayoutManager(context, 2));
            recycler_view.setAdapter(adapter);
        }

        public void setList(List<Pair<String, String>> value) {
            adapter.setDetails(value);
            adapter.notifyDataSetChanged();
        }
    }

    public interface EventListener {
        void onClickItem(User user);
    }
}
