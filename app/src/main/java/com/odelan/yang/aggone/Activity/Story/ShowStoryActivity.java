package com.odelan.yang.aggone.Activity.Story;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Profile.StoryMsgAdapter;
import com.odelan.yang.aggone.Adapter.Profile.StoryUserAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Dialog.ReportDialog;
import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.StoryMsg;
import com.odelan.yang.aggone.Model.StoryView;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowStoryActivity extends BaseActivity {

    @BindView(R.id.progressBar)     ProgressBar progressBar;
    @BindView(R.id.txt_resttime)    TextView    txt_resttime;
    @BindView(R.id.txt_name)    TextView    txt_name;
    @BindView(R.id.img_photo)    CircleImageView img_photo;
    @BindView(R.id.img_story)       ImageView img_story;
    @BindView(R.id.edit_message)    EditText edit_msg;
    @BindView(R.id.layout_self)     LinearLayout layout_self;
    @BindView(R.id.txt_view_count)  TextView view_count;
    @BindView(R.id.recycler_view)   RecyclerView recyclerView;

    @BindView(R.id.btn_down)        ImageView btn_down;
    @BindView(R.id.recycler_msg)    RecyclerView recyclerMsg;
    @BindView(R.id.layout_msg)      RelativeLayout layout_msg;

    @BindView(R.id.layout_mask)     RelativeLayout layout_mask;
    @BindView(R.id.layout_panel)    LinearLayout    layout_panel;

    List<Story> stories = new ArrayList<>();
    Story story;
    User user;
    int nCounter = -1;
    int nMax = 0, nStory_cnt = 0;
    boolean bStop = true;
    int updateInterval = 50;

    Runnable myRunable = new Runnable() {

        @Override
        public void run() {
        // Run whatever background code you want here.
            if (!bStop)
                updateStatus();
    } };
    Handler myHandler = new Handler();

    int showmode = Constants.STORY_SHOW_SELF;

    StoryUserAdapter userAdapter;
    List<StoryView> storyviews = new ArrayList<>();
    StoryMsgAdapter  msgAdapter;
    List<StoryMsg>  storymessages = new ArrayList<>();

    StoryUserAdapter.EventListener clickListener = new StoryUserAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            User user = storyviews.get(index).user;
            if(storyviews.get(index).reply_count > 0){
                bStop = true;
                getStoryMsg(user);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story);
        user = getIntent().getParcelableExtra(Constants.USER);

        ButterKnife.bind(this);
        if(!user.id.equals(AppData.user.id)){
            showmode = Constants.STORY_SHOW_OTHER;
        }
        layout_mask.setVisibility(View.GONE);
        setActivity();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(myHandler != null){
            if(myRunable != null)
                myRunable = null;
            myHandler = null;
        }
    }

    private void setActivity(){
        if(user.photo_url != null){
            if(user.photo_url.contains("http")){
                Glide.with(this).load(user.photo_url).into(img_photo);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl+ user.photo_url).into(img_photo);
            }
        } else{
            img_photo.setImageResource(R.mipmap.profile);
        }
        txt_name.setText(user.username);

        edit_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    if (!edit_msg.getText().toString().trim().isEmpty()) {
                        sendReplyToStory(edit_msg.getText().toString().trim());
                        edit_msg.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        edit_msg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(showmode == Constants.STORY_SHOW_OTHER){
                    if (keyboardShown(edit_msg.getRootView())) {
                        bStop = true;
                    } else {
                        if (stories.size() > 0){
                            bStop = false;
                            updateStatus();
                        }
                    }
                }

            }
        });

        if(showmode == Constants.STORY_SHOW_SELF){
            layout_msg.setVisibility(View.GONE);
            edit_msg.setVisibility(View.GONE);
            userAdapter = new StoryUserAdapter(this, storyviews, clickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(userAdapter);

            msgAdapter = new StoryMsgAdapter(ShowStoryActivity.this, storymessages);
            recyclerMsg.setLayoutManager(new LinearLayoutManager(ShowStoryActivity.this, LinearLayoutManager.VERTICAL, true));
            recyclerMsg.setAdapter(msgAdapter);

        } else{
            layout_msg.setVisibility(View.GONE);
            layout_self.setVisibility(View.GONE);
        }

        getStories();
    }

    void getStories(){
        bStop = true;
        showProgress();
        API.getStoriesByUser(this, user.id, new APICallback<List<Story>>() {
            @Override
            public void onSuccess(List<Story> response) {
                dismissProgress();
                stories.addAll(response);
                nMax = stories.size();
                if (nMax > 0) {
                    nStory_cnt = 0;
                    nCounter = 0;
                    progressBar.setMax(nMax * 400);
                    updateStory();
                } else{
                    finish();
                }
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                finish();
            }
        });
    }

    void updateStory(){
        bStop = true;
        story = stories.get(nStory_cnt);
        Glide.with(ShowStoryActivity.this).load(API.baseUrl + API.storyDirUrl + story.image).into(img_story);
        txt_resttime.setText(getRestTime());

        if(showmode == Constants.STORY_SHOW_SELF){
            getStoryViews();
        } else {
            setViewStory();
        }

    }

    String getRestTime(){
        String rest_time = "";
        long diff = story.timeend - System.currentTimeMillis()/1000;
        if (diff < 60){
            rest_time = diff + "s";
        } else if(diff < 3600){
            rest_time = diff/60 + "m";
        } else{
            rest_time = diff/3600 + "h";
        }
        return rest_time;
    }

    private void getStoryMsg(final User other){
        showProgress();
        API.getStoryMsg(this, story.id, other.id, new APICallback<List<StoryMsg>>() {
            @Override
            public void onSuccess(List<StoryMsg> response) {
                dismissProgress();
                storymessages.clear();
                storymessages.addAll(response);
                msgAdapter.setDataList(storymessages);
                layout_msg.setVisibility(View.VISIBLE);
                layout_self.setVisibility(View.GONE);
                bStop = false;
                updateStatus();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
                bStop = false;
                updateStatus();
            }
        });
    }

    private void setViewStory(){
        showProgress();
        API.viewStory(this, story.id, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                dismissProgress();
                bStop = false;
                updateStatus();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
                bStop = false;
                updateStatus();
            }
        });
    }

    private void updateStatus(){
        nCounter++;
        if (nCounter >= nMax * 400){
            finish();
        }
        progressBar.setProgress(nCounter);
        if(myRunable != null)
            myHandler.postDelayed(myRunable, updateInterval);
        if (nStory_cnt < nCounter/400) {
            nStory_cnt = nCounter/400;
            if (nStory_cnt == nMax) {
                finish();
                return;
            }
            updateStory();
        }
    }

    private void sendReplyToStory(String reply){
        showProgress();
        API.replyStory(this, story.id, Constants.REPLY_TYPE_TEXT, reply, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                dismissProgress();
                MyApp.hideKeyboard(ShowStoryActivity.this);
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    private void getStoryViews(){
        showProgress();
        view_count.setText("");
        API.getStoryViews(this, story.id, new APICallback<Pair<List<StoryView>, Integer>>() {
            @Override
            public void onSuccess(Pair<List<StoryView>, Integer> response) {
                dismissProgress();
                storyviews.clear();
                storyviews.addAll(response.first);
                view_count.setText(response.second + " views");
                userAdapter.setDataList(storyviews);
//                if(response.second == 0)
//                    myHandler.postDelayed(myRunable, updateInterval);
                bStop = false;
                updateStatus();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
                bStop = false;
                updateStatus();
            }
        });
    }

    @OnClick(R.id.btn_delete) void onClickDelete(){
        if(layout_mask.getVisibility() == View.VISIBLE){
            layout_panel.animate()
                    .translationYBy(0)
                    .translationY(layout_panel.getHeight())
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_panel.setVisibility(View.GONE);
                            layout_mask.setVisibility(View.GONE);
                        }
                    });

            showProgress();
            DeleteDialog dialog = new DeleteDialog(this);
            dialog.setListener(() -> API.deleteStory(this, story.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    dismissProgress();
                    bStop = false;
                    nCounter = 400 * (nStory_cnt + 1);
                    updateStatus();
                }

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                    bStop = false;
                    updateStatus();
                }
            }));
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    }

    @OnClick(R.id.layout_mask) void onClickMask(){
        layout_panel.animate()
                .translationYBy(0)
                .translationY(layout_panel.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout_panel.setVisibility(View.GONE);
                        layout_mask.setVisibility(View.GONE);
                        bStop = false;
                        updateStatus();
                    }
                });
    }

    @OnClick(R.id.btn_menu) void onClickMenu(){
        bStop = true;
        if(showmode == Constants.STORY_SHOW_OTHER) {
            ReportDialog dialog = new ReportDialog(this, "Report", "");
            dialog.setListener(value -> {
                showProgress();
                API.reportStory(ShowStoryActivity.this, story.id, value, new APICallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        dismissProgress();
                        showToast("Your report was sent to manager successful");
                        bStop = false;
                        updateStatus();
                    }

                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        bStop = false;
                        updateStatus();
                    }
                });
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
            return;
        }
        layout_mask.setVisibility(View.VISIBLE);
        layout_panel.animate()
                .translationYBy(layout_panel.getHeight())
                .translationY(0).alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        layout_panel.setVisibility(View.VISIBLE);
                        layout_panel.setAlpha(0.0f);
                    }
                });
    }

    @OnClick(R.id.btn_down) void onClickDownMsg(){
        layout_msg.setVisibility(View.GONE);
        layout_self.setVisibility(View.VISIBLE);
    }

    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

}
