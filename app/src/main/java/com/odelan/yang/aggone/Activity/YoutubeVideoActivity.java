package com.odelan.yang.aggone.Activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class YoutubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.video_view) YouTubePlayerView video_view;
    @BindView(R.id.layout_top) RelativeLayout layout_top;
    @BindView(R.id.txt_title) TextView txt_title;

    private  String youtube_id;
    private YouTube youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video);
        ButterKnife.bind(this);

        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(this.getString(R.string.app_name)).build();

        layout_top.setVisibility(View.GONE);
        youtube_id = getIntent().getStringExtra(Constants.YOUTUBE_ID);
        video_view.initialize(Constants.DEVELOPER_KEY, this);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_download) void onClickDownload() {
    }
    @OnClick(R.id.btn_share) void onClickShare() {
    }
    @OnTouch(R.id.video_view) boolean onClickVideo() {
        layout_top.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_top.setVisibility(View.GONE);
            }
        }, 3000);
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
}
