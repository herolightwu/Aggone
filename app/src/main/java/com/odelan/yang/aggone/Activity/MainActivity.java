package com.odelan.yang.aggone.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.odelan.yang.aggone.Activity.Auth.SplashActivity;
import com.odelan.yang.aggone.Activity.LookUp.LookUpActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.Publish.PublishVideoActivity;
import com.odelan.yang.aggone.Activity.Publish.PublishYoutubeActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Activity.Story.PublishStoryActivity;
import com.odelan.yang.aggone.Activity.Story.ShowStoryActivity;
import com.odelan.yang.aggone.Adapter.FeedAdapter;
import com.odelan.yang.aggone.Adapter.SportSelectAdapter;
import com.odelan.yang.aggone.Adapter.TagUserAdapter;
import com.odelan.yang.aggone.Dialog.ReportDialog;
import com.odelan.yang.aggone.Dialog.UploadURLDialog;
import com.odelan.yang.aggone.Dialog.UploadVideoDialog;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.FileUtil;
import com.onesignal.shortcutbadger.ShortcutBadger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends BaseActivity {
    static int YOUTUBE_SEARCH = 96;

    @BindView(R.id.edit_search) EditText edit_search;
    @BindView(R.id.btn_filter) TextView btn_filter;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.layout_tab) LinearLayout layout_tab;
    @BindView(R.id.layout_mask) RelativeLayout layout_mask;
    @BindView(R.id.layout_panel) LinearLayout layout_panel;

    @BindView(R.id.layout_search) LinearLayout layout_search;
    @BindView(R.id.layout_title) RelativeLayout layout_title;
    @BindView(R.id.btn_profile)     ImageView btn_profile;


    @BindView(R.id.recycler_view_sports) RecyclerView recycler_view_sports;
    List<Sport> sports = new ArrayList<>();
    SportSelectAdapter sportAdapter;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    @BindView(R.id.layout_tag_mask) RelativeLayout tag_mask_view;
    @BindView(R.id.input_layout)  RelativeLayout  tag_panel;
    @BindView(R.id.recycler_tag)  RecyclerView recycler_tag;
    @BindView(R.id.edit_search_tag)  EditText edit_search_tag;

    FeedAdapter adapter;
    List<Feed> feeds = new ArrayList<>();
    List<Feed> total_feeds = new ArrayList<>();

    TagUserAdapter tagAdapter;

    private int page = 1;
    private int feedMode = Constants.WORLD;

    FFmpeg ffmpeg;

    /**
     * Tagged Adapter Event Listener
     * */
    TagUserAdapter.EventListener taggedListener = new TagUserAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {

        }

        @Override
        public void onClickProfile(User user) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
            onClickTagMask();
        }
    };
    /**
     * Feed Adapter Event Listener
     * */
    FeedAdapter.EventListener feedListener = new FeedAdapter.EventListener() {
        @Override
        public void onClickItem(final int index) {
            API.addViewFeed(MainActivity.this, feeds.get(index), new APICallback<Feed>() {
                @Override
                public void onSuccess(Feed response) {
                    feeds.get(index).view_count = response.view_count;
                    adapter.notifyItemChanged(index);
                    if (feeds.get(index).type == Constants.NORMAL) {
                        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                        intent.putExtra(Constants.VIDEO_URL, feeds.get(index).video_url);
                        intent.putExtra(Constants.VIDEO_TITLE, feeds.get(index).title);
                        intent.putExtra(Constants.THUMBNAIL_URL, feeds.get(index).thumbnail_url);
                        startActivity(intent);
                    } else if(feeds.get(index).type == Constants.YOUTUBE){
                        Intent intent = new Intent(MainActivity.this, YoutubeVideoActivity.class);
                        intent.putExtra(Constants.YOUTUBE_ID, feeds.get(index).video_url);
                        startActivity(intent);
                    } else{
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
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
            ReportDialog dialog = new ReportDialog(MainActivity.this, "Report", "");
            dialog.setListener(value -> {
                showProgress();
                API.reportFeed(MainActivity.this, feeds.get(index), value, new APICallback<Boolean>() {
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
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
        @Override
        public void onClickLike(final int index) {
            API.addLikeFeed(MainActivity.this, feeds.get(index), AppData.user, new APICallback<Boolean>() {
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
                    showToast(error);
                }
            });
        }
        @Override
        public void onClickBookmark(final int index) {
            API.saveBookmark(MainActivity.this, feeds.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    //showToast("Success");
                    feeds.get(index).bookmark = response;
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }

        @Override
        public void onClickProfile(int index) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) feeds.get(index).user);
            startActivity(intent);
        }

        @Override
        public void onClickTagged(int index){
            getTaggedUsers(feeds.get(index).tagged);
        }
    };

    /**
     * Sport Select Adapter Event Listener
     * */
    SportSelectAdapter.EventListener sportListener = new SportSelectAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            sports.get(index).selected = !sports.get(index).selected;
            Collections.sort(sports, new Comparator<Sport>() {
                @Override
                public int compare(Sport m1, Sport m2) {
                    int m1Int = m1.selected ? 2000 : 0;
                    int m2Int = m2.selected ? 2000 : 0;
                    if(m1.id == AppData.user.sport_id ) m1Int += 2000;
                    if(m2.id == AppData.user.sport_id ) m2Int += 2000;
                    return (m2Int - m2.id) - (m1Int - m1.id);
                }
            });
            sportAdapter.notifyDataSetChanged();
            refresh_layout.autoRefresh();
        }

        @Override
        public void onClickStory(Story st){
            Intent intent = new Intent(MainActivity.this, ShowStoryActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) st.user);
            startActivity(intent);
        }
    };

    /**
     * Refresh Recycler view
     * */
    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            String selectedSports = "";
            for (Sport sport : sports) {
                if (sport.selected) {
                    if (!selectedSports.isEmpty()) selectedSports += ",";
                    selectedSports += String.valueOf(sport.id);
                }
            }
            page = 1;
            if (feedMode == Constants.WORLD) {
                API.getSportsFeeds(MainActivity.this, page, selectedSports, AppData.user.id, AppData.user.id, new APICallback<List<Feed>>() {
                    @Override
                    public void onSuccess(List<Feed> response) {
                        refresh_layout.finishRefresh();
                        feeds.clear();
                        feeds.addAll(response);
                        total_feeds.clear();
                        total_feeds.addAll(feeds);
                        edit_search.setText("");
                        adapter.notifyDataSetChanged();
                        sportAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(String error) {
                        refresh_layout.finishRefresh();
                        showToast(error);
                    }
                });
            } else {
                API.getMyFeeds(MainActivity.this, page, selectedSports, AppData.user.id, AppData.user.id, new APICallback<List<Feed>>() {
                    @Override
                    public void onSuccess(List<Feed> response) {
                        refresh_layout.finishRefresh();
                        edit_search.setText("");
                        feeds.clear();
                        feeds.addAll(response);
                        total_feeds.clear();
                        total_feeds.addAll(feeds);
                        adapter.notifyDataSetChanged();
                        sportAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(String error) {
                        refresh_layout.finishRefresh();
                        showToast(error);
                    }
                });
            }
        }
    };

    /**
     * Load More Feeds
     * */
    OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            String selectedSports = "";
            for (Sport sport : sports) {
                if (sport.selected) {
                    if (!selectedSports.isEmpty()) selectedSports += ",";
                    selectedSports += String.valueOf(sport.id);
                }
            }
            if (feedMode == Constants.WORLD) {
                API.getSportsFeeds(MainActivity.this, page + 1, selectedSports, AppData.user.id, AppData.user.id, new APICallback<List<Feed>>() {
                    @Override
                    public void onSuccess(List<Feed> response) {
                        if (response.size() > 0) {
                            page++;
                            edit_search.setText("");
                            feeds.addAll(response);
                            total_feeds.clear();
                            total_feeds.addAll(feeds);
                            adapter.notifyDataSetChanged();
                            sportAdapter.notifyDataSetChanged();
                        }
                        refresh_layout.finishLoadMore();
                    }
                    @Override
                    public void onFailure(String error) {
                        refresh_layout.finishRefresh();
                        showToast(error);
                    }
                });
            } else {
                API.getMyFeeds(MainActivity.this, page + 1, selectedSports, AppData.user.id, AppData.user.id, new APICallback<List<Feed>>() {
                    @Override
                    public void onSuccess(List<Feed> response) {
                        if (response.size() > 0) {
                            page++;
                            edit_search.setText("");
                            feeds.addAll(response);
                            total_feeds.clear();
                            total_feeds.addAll(feeds);
                            adapter.notifyDataSetChanged();
                            sportAdapter.notifyDataSetChanged();
                        }
                        refresh_layout.finishLoadMore();
                    }
                    @Override
                    public void onFailure(String error) {
                        refresh_layout.finishRefresh();
                        showToast(error);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (AppData.user == null || AppData.user.id == null){
            startActivity(new Intent(this, SplashActivity.class));
            finish();
            return;
        }
        ShortcutBadger.applyCount(this, 0);
        ShortcutBadger.removeCount(this);
        loadMyPhoto();
        loadStories();
    }

    List<User> tagged_users = new ArrayList<>();

    private void getTaggedUsers(String tagged){
        if (tagged != null && tagged.length() > 0){
            API.getTaggedUsers(this, tagged, new APICallback<List<User>>() {
                @Override
                public void onSuccess(List<User> response) {
                    tagged_users.clear();
                    tagged_users.addAll(response);
                    showTaggedView();
                }

                @Override
                public void onFailure(String error) {

                }
            });
        }
    }

    private void showTaggedView(){
        tag_mask_view.setVisibility(View.VISIBLE);
        tag_panel.animate()
                .translationYBy(tag_panel.getHeight())
                .translationY(0).alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        tag_panel.setVisibility(View.VISIBLE);
                        tag_panel.setAlpha(0.0f);
                    }
                });
        tagAdapter.setDataList(tagged_users);
    }

    @OnClick(R.id.layout_mask) void onClickMask() {
        layout_mask.setVisibility(View.INVISIBLE);
        refresh_layout.autoRefresh();
    }

    @OnClick(R.id.layout_tag_mask) void onClickTagMask(){
        tag_panel.animate()
                .translationYBy(0)
                .translationY(tag_panel.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        tag_panel.setVisibility(View.GONE);
                        tag_mask_view.setVisibility(View.GONE);
                    }
                });
        MyApp.hideKeyboard(this);
    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }
                @Override
                public void onSuccess() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showToast(e.getLocalizedMessage());
        } catch (Exception e) {
            showToast(e.getLocalizedMessage());
        }
    }

    private void execFFmpegBinary(final String[] command, String output) {
        try {
            if (ffmpeg == null) {
                showToast("Video load failed. Please try again");
                return;
            }
            showProgress();
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    dismissProgress();
                    showToast(s);
                }
                @Override
                public void onSuccess(String s) {
                    dismissProgress();
                    video = new File(output);
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video.getPath(),  MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    thumbnail = FileUtil.fromBitmap(MainActivity.this, bitmap);
                    videoDialog.setUploadedVideo(true);
                }
                @Override
                public void onProgress(String s) {
                }
                @Override
                public void onStart() {
                }
                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
                showToast(e.getLocalizedMessage());
                dismissProgress();
        }
    }

    private void loadMyPhoto(){
        if (AppData.user.photo_url != null && !AppData.user.photo_url.isEmpty()) {
            if (AppData.user.photo_url.contains("http")){
                Glide.with(this).load(AppData.user.photo_url).into(btn_profile);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + AppData.user.photo_url).into(btn_profile);
            }
        } else {
            btn_profile.setImageResource(R.mipmap.profile);
        }

        Collections.sort(sports, new Comparator<Sport>() {
            @Override
            public int compare(Sport m1, Sport m2) {
                int m1Int = m1.selected ? 2000 : 0;
                int m2Int = m2.selected ? 2000 : 0;
                if(m1.id == AppData.user.sport_id ) m1Int += 2000;
                if(m2.id == AppData.user.sport_id ) m2Int += 2000;
                return (m2Int - m2.id) - (m1Int - m1.id);
            }
        });
        sportAdapter.notifyDataSetChanged();
        refresh_layout.autoRefresh();
    }

    private void loadStories(){
        API.getAllStory(this, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                sportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @SuppressLint("NewApi")
    void setActivity() {
        loadFFMpegBinary();

        urlDialog = new UploadURLDialog(this);
        videoDialog = new UploadVideoDialog(this);

        layout_search.setVisibility(View.INVISIBLE);
        layout_mask.setVisibility(View.INVISIBLE);

        adapter = new FeedAdapter(this, feeds, feedListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);
        recycler_view.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ViewAnimator
                            .animate(layout_tab)
                            .duration(300)
                            .dp().translationY(90)
                            .start();
                } else {
                    ViewAnimator
                            .animate(layout_tab)
                            .duration(300)
                            .dp().translationY(0)
                            .start();
                }
            }
        });

        sports =  getAllSports();
        Collections.sort(sports, new Comparator<Sport>() {
            @Override
            public int compare(Sport m1, Sport m2) {
                int m1Int = 0;
                int m2Int = 0;
                if(m1.id == AppData.user.sport_id ) m1Int += 2000;
                if(m2.id == AppData.user.sport_id ) m2Int += 2000;
                return (m2Int - m2.id) - (m1Int - m1.id);
            }
        });

        sportAdapter = new SportSelectAdapter(this, sports, sportListener);
        recycler_view_sports.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view_sports.setAdapter(sportAdapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.setOnLoadMoreListener(loadMoreListener);
        refresh_layout.autoRefresh();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                feeds.clear();
                feeds.addAll(filter(total_feeds, s.toString()));
                adapter.notifyDataSetChanged();
            }
        });

        tag_mask_view.setVisibility(View.GONE);
        tagAdapter = new TagUserAdapter(this, tagged_users, 1, taggedListener);
        recycler_tag.setLayoutManager(new LinearLayoutManager(this));
        recycler_tag.setAdapter(tagAdapter);

        edit_search_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tagAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<Feed> filter(List<Feed> copy, String charText) {
        List<Feed> result = new ArrayList<>();
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            result.addAll(copy);
        } else {
            for (Feed model : copy) {
                if (model.title
                        .toLowerCase(Locale.getDefault())
                        .contains(charText.toLowerCase(Locale.getDefault()))) {
                    result.add(model);
                }
            }
        }
        return result;
    }

    @OnClick(R.id.btn_profile) void onClickProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.USER, (Parcelable)AppData.user);
        startActivity(intent);
    }

    @OnClick(R.id.btn_filter) void onClickFilter() {
        layout_mask.setVisibility(View.VISIBLE);
        ViewAnimator
                .animate(layout_panel)
                .duration(300)
                .dp().height(200)
                .start();
    }

    @OnClick(R.id.btn_search) void onClickSearch() {
        layout_search.setVisibility(View.VISIBLE);
        layout_title.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_world) void onClickWorld() {
        layout_mask.setVisibility(View.INVISIBLE);
        btn_filter.setText(getString(R.string.world));
        feedMode = Constants.WORLD;
        refresh_layout.autoRefresh();
    }

    @OnClick(R.id.btn_my) void onClickMy() {
        layout_mask.setVisibility(View.INVISIBLE);
        btn_filter.setText(getString(R.string.my));
        feedMode = Constants.MY;
        refresh_layout.autoRefresh();
    }

    File video;
    File thumbnail;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(VideoPicker.EXTRA_VIDEO_PATH);

            String outputFileAbsolutePath = Environment.getExternalStorageDirectory() + File.separator + "output_video" + System.currentTimeMillis() + ".mp4";
            //String[] command = {"-y", "-i", mPaths.get(0), "-s", "480x640", "-r", "25", "-vcodec", "mpeg4", "-b:v", "512k", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputFileAbsolutePath};//"640x480"
            String[] command = {"-y", "-i", mPaths.get(0), "-s", "576x1024", "-r", "25", "-vcodec", "libx264", "-b:v", "512k", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputFileAbsolutePath};
            execFFmpegBinary(command, outputFileAbsolutePath);
        }
        if (requestCode == YOUTUBE_SEARCH && resultCode == RESULT_OK) {
            VideoItem youtube = data.getExtras().getParcelable(Constants.YOUTUBE_ITEM);
            urlDialog.setVideoItem(youtube);
        }
    }

    UploadURLDialog urlDialog;
    UploadVideoDialog videoDialog;
    UploadURLDialog.Listener uploadURLListener = new UploadURLDialog.Listener() {
        @Override
        public void onClickUpload() {
            videoDialog.setListener(uploadVideoListener);
            View decorView = videoDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            videoDialog.show();
        }
        @Override
        public void onClickSubmit(String title, VideoItem youtube) {
            Intent intent = new Intent(MainActivity.this, PublishYoutubeActivity.class);
            intent.putExtra(Constants.YOUTUBE_ID, youtube.id);
            intent.putExtra(Constants.VIDEO_TITLE, title);
            intent.putExtra(Constants.THUMBNAIL_URL, youtube.thumbnailURL);
            startActivity(intent);
        }
        @Override
        public void onClickUrl() {
            Intent intent = new Intent(MainActivity.this, YoutubeSearchActivity.class);
            startActivityForResult(intent, YOUTUBE_SEARCH);
        }
    };

    UploadVideoDialog.Listener uploadVideoListener = new UploadVideoDialog.Listener() {
        @Override
        public void onAddStory(){
            startActivity(new Intent(MainActivity.this, PublishStoryActivity.class));
        }

        @Override
        public void onClickURL() {
            urlDialog.setListener(uploadURLListener);
            View decorView = urlDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            urlDialog.show();
        }

        @Override
        public void onClickSubmit(final String title) {
            Intent intent = new Intent(MainActivity.this, PublishVideoActivity.class);
            intent.putExtra(Constants.VIDEO_URL, video.getAbsolutePath());
            intent.putExtra(Constants.VIDEO_TITLE, title);
            intent.putExtra(Constants.THUMBNAIL_URL, thumbnail.getAbsolutePath());
            startActivity(intent);
        }
        @Override
        public void onClickPlus() {
            new VideoPicker.Builder(MainActivity.this)
                    .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                    .directory(VideoPicker.Directory.DEFAULT)
                    .extension(VideoPicker.Extension.MP4)
                    .enableDebuggingMode(true)
                    .build();
        }
        @Override
        public void onError(String error) {
            showToast(error);
        }
    };

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    /**
     * Event for tab bar
     * */
    @OnClick(R.id.btn_lookup) void onClickLookup() {
        Intent intent = new Intent(this, LookUpActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_stats) void onClickStats() {
        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF ){
            Intent intent = new Intent(this, ClubChartActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) AppData.user);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, StatsActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) AppData.user);
            startActivity(intent);
        }
    }
    @OnClick(R.id.btn_chats) void onClickChats() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_camera) void onClickCamera() {
        videoDialog.setListener(uploadVideoListener);
        View decorView = videoDialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        videoDialog.show();
    }
    @OnClick(R.id.btn_search_clear) void onClickSearchClear() {
        edit_search.setText("");
        layout_search.setVisibility(View.INVISIBLE);
        layout_title.setVisibility(View.VISIBLE);
        feeds.clear();
        feeds.addAll(total_feeds);
        adapter.notifyDataSetChanged();
    }
}
