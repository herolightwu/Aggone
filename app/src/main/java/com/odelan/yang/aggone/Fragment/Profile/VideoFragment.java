package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.NewsActivity;
import com.odelan.yang.aggone.Activity.VideoActivity;
import com.odelan.yang.aggone.Activity.YoutubeVideoActivity;
import com.odelan.yang.aggone.Adapter.MyFeedAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {
    private Context context;
    private Listener mListener;
    private User user;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    MyFeedAdapter adapter;
    List<Feed> feeds = new ArrayList<>();
    int page = 1;

    MyFeedAdapter.EventListener feedListener = new MyFeedAdapter.EventListener() {
        @Override
        public void onClickItem(final int index) {
            //feeds.get(index).view_count++;
            API.addViewFeed(context, feeds.get(index), new APICallback<Feed>() {
                @Override
                public void onSuccess(Feed response) {
                    feeds.get(index).view_count = response.view_count;
                    adapter.notifyItemChanged(index);
                    if (feeds.get(index).type == Constants.NORMAL) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra(Constants.VIDEO_URL, feeds.get(index).video_url);
                        intent.putExtra(Constants.VIDEO_TITLE, feeds.get(index).title);
                        intent.putExtra(Constants.THUMBNAIL_URL, feeds.get(index).thumbnail_url);
                        startActivity(intent);
                    } else if (feeds.get(index).type == Constants.YOUTUBE){
                        Intent intent = new Intent(context, YoutubeVideoActivity.class);
                        intent.putExtra(Constants.YOUTUBE_ID, feeds.get(index).video_url);
                        startActivity(intent);
                    } else{
                        Intent intent = new Intent(context, NewsActivity.class);
                        intent.putExtra(Constants.FEED, (Parcelable) feeds.get(index));
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(String error) {
                    //feeds.get(index).view_count--;
                    //adapter.notifyItemChanged(index);
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onClickLike(final int index) {
            API.addLikeFeed(context, feeds.get(index), AppData.user, new APICallback<Boolean>() {
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
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public void onClickBookmark(final int index) {
            API.saveBookmark(context, feeds.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    feeds.get(index).bookmark = response;
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClickDelete(final int index) {
            DeleteDialog dialog = new DeleteDialog(context);
            dialog.setListener(() -> API.deleteFeed(context, feeds.get(index), new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    feeds.remove(index);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }));
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }

        @Override
        public void onClickPrivate(int index, boolean flag){
            API.privateFeed(context, feeds.get(index).id, flag, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    feeds.get(index).mode = flag?Constants.FEED_PRIVATE:Constants.FEED_PUBLIC;
                    adapter.setDataList(feeds);
                }

                @Override
                public void onFailure(String error) {

                }
            });
        }

        @Override
        public void onClickReport(int index){
            if (mListener != null) mListener.sendReport(feeds.get(index));
        }
    };

    public VideoFragment() {
    }

    @SuppressLint("ValidFragment")
    public VideoFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.mListener = listener;
        this.user = user;
    }

    private Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        ctx = getContext();
        adapter = new MyFeedAdapter(context, feeds, feedListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
//        refresh_layout.setOnLoadMoreListener(loadmoreListener);
        refresh_layout.autoRefresh();
        return view;
    }

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getAllFeedsByUserid(ctx, page, user.id, AppData.user.id, new APICallback<List<Feed>>() {
                @Override
                public void onSuccess(List<Feed> response) {
                    feeds.clear();
                    feeds.addAll(response);
                    adapter.notifyDataSetChanged();
                    refresh_layout.finishRefresh();
                }
                @Override
                public void onFailure(String error) {
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    OnLoadMoreListener loadmoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            API.getAllFeedsByUserid(ctx, page + 1, user.id, AppData.user.id, new APICallback<List<Feed>>() {
                @Override
                public void onSuccess(List<Feed> response) {
                    page++;
                    feeds.addAll(response);
                    adapter.notifyDataSetChanged();
                    refresh_layout.finishRefresh();
                }
                @Override
                public void onFailure(String error) {
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    public interface Listener {
        void sendReport(Feed feed);
    }
}
