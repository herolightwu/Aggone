package com.odelan.yang.aggone.Activity.Publish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.aggone.Adapter.TagUserAdapter;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PublishYoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    @BindView(R.id.video_view)    YouTubePlayerView video_view;
    @BindView(R.id.layout_top)    RelativeLayout layout_top;
    @BindView(R.id.txt_title)    TextView txt_title;
    @BindView(R.id.input_layout)    RelativeLayout input_layout;
    @BindView(R.id.input_panel)    RelativeLayout input_panel;
    @BindView(R.id.tag_layout)    LinearLayout tag_layout;
    @BindView(R.id.desc_layout)    LinearLayout desc_layout;
    @BindView(R.id.edit_description)    EditText edit_description;
    @BindView(R.id.edit_search)         EditText edit_search;
    @BindView(R.id.recycler_view)    RecyclerView recyclerView;

    private  String youtube_id;
    private YouTube youtube;
    private String video_title;
    private String thumbnail_url;
    private int pub_mode = Constants.FEED_PUBLIC;
//    private String desc_video = "";

    TagUserAdapter adapter;
    List<User> all_users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_youtube);
        ButterKnife.bind(this);

        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(this.getString(R.string.app_name)).build();

        //layout_top.setVisibility(View.GONE);
        youtube_id = getIntent().getStringExtra(Constants.YOUTUBE_ID);
        thumbnail_url = getIntent().getStringExtra(Constants.THUMBNAIL_URL);
        video_title = getIntent().getStringExtra(Constants.VIDEO_TITLE);
        video_view.initialize(Constants.DEVELOPER_KEY, this);

        setActivity();
        getAllUsers();
    }

    @OnClick(R.id.btn_publish) void onClickPublish(){
        showProgress();
        Feed feed = new Feed();
        feed.type = Constants.YOUTUBE;
        feed.user = AppData.user;
        feed.sport_id = AppData.user.sport_id;
        feed.title = video_title;
        feed.video_url = youtube_id;
        feed.thumbnail_url = thumbnail_url;
        feed.like_count = 0;
        feed.timestamp = System.currentTimeMillis() / 1000;
        feed.shared = 1;
        feed.desc_str = edit_description.getText().toString();
        feed.mode = pub_mode;
        feed.tagged = getTagged();
        API.saveFeed(PublishYoutubeActivity.this, feed, new APICallback<Feed>() {
            @Override
            public void onSuccess(Feed response) {
                dismissProgress();
                finish();
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    String getTagged(){
        Map<String, Boolean> uu_ids = adapter.sel_users;
        String taged_str = "";
        for (Map.Entry<String, Boolean> entry : uu_ids.entrySet()) {
            if(entry.getValue()){
                if (taged_str.length() == 0){
                    taged_str = entry.getKey();
                } else{
                    taged_str = taged_str + "," + entry.getKey();
                }
            }
        }
        return taged_str;
    }

    @OnClick(R.id.btn_description) void onClickDescription(){
        /*AddDescriptionDlg dlg = new AddDescriptionDlg(this, getString(R.string.description), desc_video);
        dlg.setListener(new AddDescriptionDlg.Listener() {
            @Override
            public void onClickOK(String value) {
                if(value.length() == 0){
                    Toast.makeText(PublishYoutubeActivity.this, "Invalid Description", Toast.LENGTH_SHORT).show();
                    return;
                }
                desc_video = value;
            }
        });
        View decorView = dlg.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dlg.show();*/
        input_panel.setVisibility(View.VISIBLE);
        tag_layout.setVisibility(View.GONE);
        desc_layout.setVisibility(View.VISIBLE);
        input_layout.animate()
                .translationYBy(input_layout.getHeight())
                .translationY(0).alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        input_layout.setVisibility(View.VISIBLE);
                        input_layout.setAlpha(0.0f);
                    }
                });
    }

    @OnClick(R.id.btn_tag) void onClickTag(){
        input_panel.setVisibility(View.VISIBLE);
        tag_layout.setVisibility(View.VISIBLE);
        desc_layout.setVisibility(View.GONE);
        input_layout.animate()
                .translationYBy(input_layout.getHeight())
                .translationY(0).alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        input_layout.setVisibility(View.VISIBLE);
                        input_layout.setAlpha(0.0f);
                    }
                });
    }

    @OnClick(R.id.btn_private) void onClickPrivate(){
        if(pub_mode == Constants.FEED_PUBLIC){
            pub_mode = Constants.FEED_PRIVATE;
            Toast.makeText(this, "This video is private", Toast.LENGTH_SHORT).show();
        } else{
            pub_mode = Constants.FEED_PUBLIC;
            Toast.makeText(this, "This video is public", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.input_panel) void onClickPanel(){
        input_layout.animate()
                .translationYBy(0)
                .translationY(input_layout.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        input_layout.setVisibility(View.GONE);
                        input_panel.setVisibility(View.GONE);
                        tag_layout.setVisibility(View.GONE);
                        desc_layout.setVisibility(View.GONE);
                    }
                });
        MyApp.hideKeyboard(this);
    }

    @OnClick(R.id.panel_down) void onClickPanelDown(){
        input_layout.animate()
                .translationYBy(0)
                .translationY(input_layout.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        input_layout.setVisibility(View.GONE);
                        input_panel.setVisibility(View.GONE);
                        tag_layout.setVisibility(View.GONE);
                        desc_layout.setVisibility(View.GONE);
                    }
                });
        MyApp.hideKeyboard(this);
    }

    @OnClick(R.id.btn_search_clear) void onClickClear(){
        edit_search.setText("");
    }


    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_download) void onClickDownload() {
    }
    @OnClick(R.id.btn_share) void onClickShare() {
    }
    @OnTouch(R.id.video_view) boolean onClickVideo() {
//        layout_top.setVisibility(View.VISIBLE);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                layout_top.setVisibility(View.GONE);
//            }
//        }, 3000);
        return true;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(youtube_id);
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }

    void getAllUsers(){
        API.getAllUsers(this, new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> response) {
                all_users.clear();
                all_users.addAll(response);
                adapter.setDataList(all_users);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    void setActivity() {
        adapter = new TagUserAdapter(this, all_users, 0, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        txt_title.setText(video_title);
        input_panel.setVisibility(View.GONE);
        tag_layout.setVisibility(View.GONE);
        desc_layout.setVisibility(View.GONE);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private KProgressHUD hud;

    public void showProgress() {
        showProgress("Please wait");
    }

    public void showProgress(String message) {
        hud = KProgressHUD.create(this).setLabel(message);
        hud.show();
    }

    public void dismissProgress() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
