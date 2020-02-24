package com.odelan.yang.aggone.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.odelan.yang.aggone.Adapter.Profile.YoutubeAdapter;
import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YoutubeSearchActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    YoutubeAdapter adapter;
    List<VideoItem> videos = new ArrayList<>();

    @BindView(R.id.edit_search) EditText edit_search;

    YoutubeAdapter.Listener youtubeListener = new YoutubeAdapter.Listener() {
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent();
            intent.putExtra(Constants.YOUTUBE_ITEM, (Parcelable) videos.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_search);

        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        adapter = new YoutubeAdapter(YoutubeSearchActivity.this, videos, youtubeListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchOnYoutube(s.toString());
            }
        });
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @OnClick(R.id.btn_search_clear) void onClickclear() {
        edit_search.setText("");
        searchOnYoutube("");
    }

    private void searchOnYoutube(final String keywords) {
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(YoutubeSearchActivity.this);
                videos.clear();
                videos.addAll(yc.search(keywords));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
}
