package com.odelan.yang.aggone.Activity.LookUp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.Message.ChatActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.YoutubeSearchActivity;
import com.odelan.yang.aggone.Adapter.PagerAdapter;
import com.odelan.yang.aggone.Adapter.Search.PeopleSearchAdapter;
import com.odelan.yang.aggone.Fragment.Message.MessageFragment;
import com.odelan.yang.aggone.Fragment.Message.NotificationFragment;
import com.odelan.yang.aggone.Fragment.Search.PeopleFragment;
import com.odelan.yang.aggone.Fragment.Search.VideoSearchFragment;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.YoutubeConnector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.txt_people)      TextView txt_people;
    @BindView(R.id.txt_video)       TextView txt_video;
    @BindView(R.id.view_pager)      ViewPager view_pager;
    @BindView(R.id.edit_search)    EditText edit_search;

    PagerAdapter adapter;
    Fragment[] fragments;

    List<Feed> feeds = new ArrayList<>();
    List<User> users = new ArrayList<>();

    PeopleFragment.Listener peopleListener = new PeopleFragment.Listener() {
        @Override
        public void onClick(User user) {
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    };

    VideoSearchFragment.Listener videoListener = new VideoSearchFragment.Listener() {
        @Override
        public void onPlay(VideoItem item) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        fragments = new Fragment[] {
                new PeopleFragment(this, peopleListener),
                new VideoSearchFragment(this, videoListener),
        };
        adapter = new PagerAdapter(this, fragments, getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                setTabTitle(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        view_pager.setCurrentItem(0);
        setTabTitle(0);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (view_pager.getCurrentItem() == 1 && editable.toString().length() > 1){
                    searchOnFeeds(editable.toString());
                } else if (view_pager.getCurrentItem() == 0 && editable.toString().length() > 1){
                    searchUsers(editable.toString());
                }
            }
        });
    }

    void setTabTitle(int index) {
        if (index == 0) {
            txt_people.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            txt_video.setTextColor(ContextCompat.getColor(this, R.color.tab_txt_normal));
        } else if (index == 1) {
            txt_people.setTextColor(ContextCompat.getColor(this, R.color.tab_txt_normal));
            txt_video.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        edit_search.setText(edit_search.getText().toString().trim());
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @OnClick(R.id.layout_people) void onClickMessage() {
        edit_search.setText("");
        view_pager.setCurrentItem(0);
    }
    @OnClick(R.id.layout_video) void onClickNotification() {
        edit_search.setText("");
        view_pager.setCurrentItem(1);
    }

    @OnClick(R.id.btn_search_clear) void onClickSearchClear() {
        edit_search.setText("");
        feeds.clear();
        ((VideoSearchFragment)fragments[1]).setDataList(feeds);
        users.clear();
        ((PeopleFragment)fragments[0]).setDataList(users);
    }

    @OnClick(R.id.btn_cancel) void onClickCancel(){
        edit_search.setText("");
        feeds.clear();
        ((VideoSearchFragment)fragments[1]).setDataList(feeds);
        users.clear();
        ((PeopleFragment)fragments[0]).setDataList(users);
    }

    @OnClick(R.id.btn_back) void onBack(){
        finish();
    }

    private void searchUsers(final String keywords){
        API.searchUsers(this, keywords, new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> response) {
                users.clear();
                users.addAll(response);
                ((PeopleFragment) fragments[0]).setDataList(users);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void searchOnFeeds(final String keywords) {
        API.searchVideoFeeds(this, keywords, new APICallback<List<Feed>>() {
            @Override
            public void onSuccess(List<Feed> response) {
                feeds.clear();
                feeds.addAll(response);
                ((VideoSearchFragment) fragments[1]).setDataList(feeds);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
