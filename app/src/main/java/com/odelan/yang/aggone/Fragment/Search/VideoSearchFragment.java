package com.odelan.yang.aggone.Fragment.Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.NewsActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.VideoActivity;
import com.odelan.yang.aggone.Activity.YoutubeSearchActivity;
import com.odelan.yang.aggone.Activity.YoutubeVideoActivity;
import com.odelan.yang.aggone.Adapter.FeedAdapter;
import com.odelan.yang.aggone.Adapter.Profile.YoutubeAdapter;
import com.odelan.yang.aggone.Dialog.ReportDialog;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoSearchFragment extends Fragment {
    private Context context;
    private Listener mListener;

    @BindView(R.id.recycler_view)    RecyclerView recycler_view;

    FeedAdapter adapter;
    List<Feed> feeds = new ArrayList<>();

    /**
     * Feed Adapter Event Listener
     * */
    FeedAdapter.EventListener feedListener = new FeedAdapter.EventListener() {
        @Override
        public void onClickItem(final int index) {
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
                    } else if(feeds.get(index).type == Constants.YOUTUBE){
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

                }
            });
        }
        @Override
        public void onClickMenu(View view, int index) {
            ReportDialog dialog = new ReportDialog(context, "Report", "");
            dialog.setListener(value -> {
                API.reportFeed(context, feeds.get(index), value, new APICallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        Toast.makeText(context, "Your report was sent to manager successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        //showToast(error);
                    }
                });
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
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
                }
            });
        }
        @Override
        public void onClickBookmark(final int index) {
            API.saveBookmark(context, feeds.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    feeds.get(index).bookmark = response;
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {

                }
            });
        }

        @Override
        public void onClickProfile(int index) {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) feeds.get(index).user);
            startActivity(intent);
        }

        @Override
        public void onClickTagged(int index){
        }
    };

    public VideoSearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public VideoSearchFragment(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragment();
    }

    void setFragment() {
        adapter = new FeedAdapter(context, feeds, feedListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);
    }

    public void setDataList(List<Feed> data){
        feeds = data;
        adapter.setDataList(data);
    }

    public interface Listener {
        void onPlay(VideoItem item);
    }
}
