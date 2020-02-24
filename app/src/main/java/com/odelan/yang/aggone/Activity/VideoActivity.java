package com.odelan.yang.aggone.Activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoActivity extends BaseActivity {

    @BindView(R.id.video_view) JzvdStd video_view;
    @BindView(R.id.layout_top) RelativeLayout layout_top;
    @BindView(R.id.txt_title) TextView txt_title;

    private String video_url;
    private String video_title;
    private String thumbnail_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);

        video_url = getIntent().getStringExtra(Constants.VIDEO_URL);
        video_title = getIntent().getStringExtra(Constants.VIDEO_TITLE);
        thumbnail_url = getIntent().getStringExtra(Constants.THUMBNAIL_URL);
        if (!video_url.contains("http")){
            video_url = API.baseUrl + API.videoDirUrl + video_url;
        }
        if(!thumbnail_url.contains("http")){
            thumbnail_url = API.baseUrl + API.imgDirUrl + thumbnail_url;
        }

        setActivity();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    void setActivity() {
        video_view.setUp(video_url, video_title , Jzvd.SCREEN_WINDOW_NORMAL);
        Glide.with(this).load(thumbnail_url).into(video_view.thumbImageView);
//        txt_title.setText(video_title);
//        layout_top.setVisibility(View.GONE);
    }
}
