package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.NewsActivity;
import com.odelan.yang.aggone.Activity.VideoActivity;
import com.odelan.yang.aggone.Activity.YoutubeVideoActivity;
import com.odelan.yang.aggone.Adapter.FeedAdapter;
import com.odelan.yang.aggone.Dialog.ReportDialog;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordedActivity extends BaseActivity {
    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    FeedAdapter adapter;
    List<Feed> feeds = new ArrayList<>();

    /**
     * Feed Adapter Event Listener
     * */
    FeedAdapter.EventListener feedListener = new FeedAdapter.EventListener() {
        @Override
        public void onClickItem(final int index) {
            API.addViewFeed(RecordedActivity.this, feeds.get(index), new APICallback<Feed>() {
                @Override
                public void onSuccess(Feed response) {
                    feeds.get(index).view_count = response.view_count;
                    adapter.notifyItemChanged(index);
                    if (feeds.get(index).type == Constants.NORMAL) {
                        Intent intent = new Intent(RecordedActivity.this, VideoActivity.class);
                        intent.putExtra(Constants.VIDEO_URL, feeds.get(index).video_url);
                        intent.putExtra(Constants.VIDEO_TITLE, feeds.get(index).title);
                        startActivity(intent);
                    } else if (feeds.get(index).type == Constants.YOUTUBE){
                        Intent intent = new Intent(RecordedActivity.this, YoutubeVideoActivity.class);
                        intent.putExtra(Constants.YOUTUBE_ID, feeds.get(index).video_url);
                        startActivity(intent);
                    } else{
                        Intent intent = new Intent(RecordedActivity.this, NewsActivity.class);
                        intent.putExtra(Constants.FEED, (Parcelable) feeds.get(index));
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(String error) {
                    //adapter.notifyItemChanged(index);
                    showToast(error);
                }
            });
        }
        @Override
        public void onClickMenu(View view, int index) {
            ReportDialog dialog = new ReportDialog(RecordedActivity.this, "Report", "");
            dialog.setListener(new ReportDialog.Listener() {
                @Override
                public void onClickOK(String value) {
                    showProgress();
                    API.reportFeed(RecordedActivity.this, feeds.get(index), value, new APICallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean response) {
                            dismissProgress();
                            showToast("Your report was sent to manager successful");
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            //showToast(error);
                        }
                    });
                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
        @Override
        public void onClickLike(final int index) {
            API.addLikeFeed(RecordedActivity.this, feeds.get(index), AppData.user, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    if (response) {
                        feeds.get(index).like_count++;
                        feeds.get(index).like = true;
                    } else {
                        feeds.get(index).like_count--;
                        feeds.get(index).like = false;
                    }
                    adapter.notifyItemChanged(index);
                    refresh_layout.autoRefresh();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                    feeds.get(index).bookmark = true;
                    adapter.notifyDataSetChanged();
                    refresh_layout.autoRefresh();
                }
            });
        }
        @Override
        public void onClickBookmark(int index) {
            API.saveBookmark(RecordedActivity.this, feeds.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    //showToast("Success");
                    feeds.get(index).bookmark = response;
                    adapter.notifyItemChanged(index);
                    refresh_layout.autoRefresh();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }

        @Override
        public void onClickProfile(int index) {
            Intent intent = new Intent(RecordedActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) feeds.get(index).user);
            startActivity(intent);
        }
        @Override
        public void onClickTagged(int index){

        }
    };

    /**
     * Refresh Recycler view
     * */
    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            API.getBookmarks(RecordedActivity.this, new APICallback<List<Feed>>() {
                @Override
                public void onSuccess(List<Feed> response) {
                    feeds.clear();
                    feeds.addAll(response);
                    adapter.notifyDataSetChanged();
                    refresh_layout.finishRefresh();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded);
        ButterKnife.bind(this);
        setViewController();
    }

    void setViewController() {
        adapter = new FeedAdapter(this, feeds, feedListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
