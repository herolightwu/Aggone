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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odelan.yang.aggone.Adapter.Message.ContactAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Contact;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageFragment extends Fragment {
    private Context context;
    private Listener mListener;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ContactAdapter adapter;
    List<Contact> contacts = new ArrayList<>();

    ContactAdapter.EventListener contactListener = new ContactAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            if (mListener != null) mListener.onChat(contacts.get(index).user);
        }
        @Override
        public void onClickDelete(int index) {
            final Contact one = contacts.get(index);
            final int ind = index;
            DeleteDialog dialog = new DeleteDialog(context);
            dialog.setListener(new DeleteDialog.Listener() {
                @Override
                public void onClickDelete() {
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child(Constants.FIREBASE_CONTACT).child(AppData.user.id).child(one.user.id).setValue(null);
                    contacts.remove(ind);
                    adapter.notifyDataSetChanged();
                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    public MessageFragment() {
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(Context context, Listener listener) {
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragment();
    }

    void setFragment() {
        adapter = new ContactAdapter(context, contacts, contactListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_CONTACT)
                .child(AppData.user.id)
                .orderByChild(Constants.FIREBASE_TIMESTAMP)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    contacts.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Contact contact = snapshot.getValue(Contact.class);
                        contacts.add(0, contact);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public interface Listener {
        void onChat(User user);
    }
}
