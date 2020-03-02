package com.odelan.yang.aggone.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsActivity extends BaseActivity {

    @BindView(R.id.img_article)    ImageView img_article;
    @BindView(R.id.img_forword)    ImageView img_forword;
    @BindView(R.id.img_back)    ImageView img_back;
    @BindView(R.id.img_avata)    CircleImageView img_avatar;
    @BindView(R.id.txt_name)    TextView txt_name;
    @BindView(R.id.txt_time)    TextView txt_time;
    @BindView(R.id.txt_title)    TextView txt_title;
    @BindView(R.id.txt_description)    TextView txt_description;

    int page_num = 0;
    int page_max = 1;

    Feed feed;
    String[] img_paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);
        feed = getIntent().getExtras().getParcelable(Constants.FEED);
        setActivity();
    }

    void setActivity(){
        if (feed.articles != null && feed.articles.length() > 0){
            img_paths = feed.articles.split(",");
            page_max = img_paths.length;
        } else{
            img_article.setImageDrawable(null);
        }
        if (feed.user != null && feed.user.photo_url != null && !feed.user.photo_url.isEmpty()) {
            if (feed.user.photo_url.contains("http")){
                Glide.with(this).load(feed.user.photo_url).into(img_avatar);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + feed.user.photo_url).into(img_avatar);
            }
        } else {
            img_avatar.setImageResource(R.mipmap.profile);
        }

        if(feed.user != null){
            txt_name.setText(feed.user.username);
        }
        txt_description.setText(feed.desc_str);
        txt_title.setText(feed.title);
        txt_time.setText(getDate(feed.timestamp));

        img_back.setVisibility(View.GONE);
        if(page_max>1)
            img_forword.setVisibility(View.VISIBLE);
        else
            img_forword.setVisibility(View.GONE);
        refreshArticles();
    }

    void refreshArticles(){
        if (!img_paths[page_num].isEmpty()) {
            if (img_paths[page_num].contains("http")){
                Glide.with(this).load(img_paths[page_num]).into(img_article);
            } else{
                Glide.with(this).load(API.baseUrl + API.articleDirUrl + img_paths[page_num]).into(img_article);
            }
        } else {
            img_article.setImageResource(R.mipmap.thumbnail1);
        }
    }

    @OnClick(R.id.img_forword) void onClickForword(){
        page_num ++;
        if(page_num == page_max - 1) {
            img_forword.setVisibility(View.GONE);
        }
        if(page_max > 1){
            img_back.setVisibility(View.VISIBLE);
        }
        refreshArticles();
    }
    @OnClick(R.id.img_back) void onClickImgBack(){
        page_num--;
        if(page_num == 0){
            img_back.setVisibility(View.GONE);
            if(page_max > 1){
                img_forword.setVisibility(View.VISIBLE);
            }
        }
        refreshArticles();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }
}
