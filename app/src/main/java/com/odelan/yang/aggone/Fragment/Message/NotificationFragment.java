package com.odelan.yang.aggone.Fragment.Message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.aggone.Adapter.Message.ContactAdapter;
import com.odelan.yang.aggone.Adapter.Message.NotificationAdapter;
import com.odelan.yang.aggone.Model.Contact;
import com.odelan.yang.aggone.Model.Notification;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {
    private Context context;
    private Listener mListener;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    NotificationAdapter adapter;
    List<Notification> notifications = new ArrayList<>();

    NotificationAdapter.EventListener notificationListener = new NotificationAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            if (mListener != null) mListener.onClickProfile(notifications.get(index).sender);
            API.removeNotification(context, notifications.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    loadData();
                }

                @Override
                public void onFailure(String error) {
                    loadData();
                }
            });
        }
        @Override
        public void onClickDelete(int index) {
            API.removeNotification(context, notifications.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    loadData();
                }

                @Override
                public void onFailure(String error) {
                    loadData();
                }
            });
        }
    };

    public NotificationFragment() {
    }

    @SuppressLint("ValidFragment")
    public NotificationFragment(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    void setFragment() {
        adapter = new NotificationAdapter(context, notifications, notificationListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);
        loadData();
//        FirebaseDatabase.getInstance().getReference()
//                .child(Constants.FIREBASE_NOTIFICATION)
//                .child(AppData.user.id)
//                .orderByChild(Constants.FIREBASE_TIMESTAMP)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChildren()) {
//                            notifications.clear();
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                Notification notification = snapshot.getValue(Notification.class);
//                                notifications.add(0, notification);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void loadData(){
        API.getAllNotifications(context, new APICallback<List<Notification>>() {
            @Override
            public void onSuccess(List<Notification> response) {
                notifications.clear();
                notifications.addAll(response);
                adapter.setDataList(notifications);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragment();
    }

    public interface Listener {
        void onClickProfile(User user);
    }
}
