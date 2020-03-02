package com.odelan.yang.aggone.Fragment.Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odelan.yang.aggone.Adapter.Search.PeopleSearchAdapter;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeopleFragment extends Fragment {

    private Context context;
    private Listener mListener;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    PeopleSearchAdapter adapter;
    List<User> people = new ArrayList<>();

    PeopleSearchAdapter.EventListener peopleListener = new PeopleSearchAdapter.EventListener() {
        @Override
        public void onClickProfile(int index) {
            if (mListener != null) mListener.onClick(people.get(index));
        }
    };

    public PeopleFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public PeopleFragment(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragment();
    }

    void setFragment() {
        adapter = new PeopleSearchAdapter(context, people, peopleListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);
    }

    public void setDataList(List<User> data){
        people = data;
        adapter.setDataList(people);
    }

    public interface Listener {
        void onClick(User user);
    }
}
