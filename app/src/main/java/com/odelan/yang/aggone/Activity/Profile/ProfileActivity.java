package com.odelan.yang.aggone.Activity.Profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jwang123.flagkit.FlagKit;
import com.odelan.yang.aggone.Activity.Auth.ChangePassActivity;
import com.odelan.yang.aggone.Activity.Auth.LoginActivity;
import com.odelan.yang.aggone.Activity.Auth.TermsActivity;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ChatActivity;
import com.odelan.yang.aggone.Activity.Publish.PublishVideoActivity;
import com.odelan.yang.aggone.Activity.Publish.PublishYoutubeActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Activity.Story.PublishStoryActivity;
import com.odelan.yang.aggone.Activity.Story.ShowStoryActivity;
import com.odelan.yang.aggone.Activity.VideoActivity;
import com.odelan.yang.aggone.Activity.YoutubeSearchActivity;
import com.odelan.yang.aggone.Adapter.PagerAdapter;
import com.odelan.yang.aggone.Adapter.ResultAdapter;
import com.odelan.yang.aggone.Dialog.AddDescriptionDlg;
import com.odelan.yang.aggone.Dialog.AddDialog;
import com.odelan.yang.aggone.Dialog.AddPrizeDialog;
import com.odelan.yang.aggone.Dialog.CareerDialog;
import com.odelan.yang.aggone.Dialog.NewsDialog;
import com.odelan.yang.aggone.Dialog.ReportDialog;
import com.odelan.yang.aggone.Dialog.ResultDialog;
import com.odelan.yang.aggone.Dialog.TextInputDialog;
import com.odelan.yang.aggone.Dialog.UploadURLDialog;
import com.odelan.yang.aggone.Dialog.UploadVideoDialog;
import com.odelan.yang.aggone.Fragment.Profile.CareerFragment;
import com.odelan.yang.aggone.Fragment.Profile.DescriptionFragment;
import com.odelan.yang.aggone.Fragment.Profile.HistoryFragment;
import com.odelan.yang.aggone.Fragment.Profile.PrizeFragment;
import com.odelan.yang.aggone.Fragment.Profile.TeamFragment;
import com.odelan.yang.aggone.Fragment.Profile.VideoFragment;
import com.odelan.yang.aggone.Model.Career;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Prize;
import com.odelan.yang.aggone.Model.Skill;
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
import com.odelan.yang.aggone.Utils.TinyDB;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity{//PopupMenu.OnMenuItemClickListener,

    static int EDIT_PROFILE = 95;
    static int YOUTUBE_SEARCH = 96;
    static int REQUEST_STORAGE_WRITE_PERMISSION = 111;

    @BindView(R.id.img_avata) CircleImageView img_avata;
    @BindView(R.id.btn_follow) Button btn_follow;
    @BindView(R.id.layout_button)    RelativeLayout layout_button;
    @BindView(R.id.btn_plus) ImageView btn_plus;
    @BindView(R.id.btn_sport) Button btn_sport;
    @BindView(R.id.btn_type) Button btn_type;
    @BindView(R.id.btn_specialty) Button btn_specialty;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_club) TextView txt_club;
    @BindView(R.id.txt_position) TextView txt_position;
    @BindView(R.id.txt_category) TextView txt_category;
    @BindView(R.id.base_view)   LinearLayout baseview;
    @BindView(R.id.img_flag)       ImageView img_flag;
    @BindView(R.id.txt_country)    TextView  txt_country;
    @BindView(R.id.txt_recommend)   TextView txt_recommend;

    /**
     * Navigation View
     * */
    @BindView(R.id.btn_video)           TextView btn_video;
    @BindView(R.id.btn_prize)           TextView btn_prize;
    @BindView(R.id.btn_carrier)         TextView btn_carrier;
    @BindView(R.id.btn_description)     TextView btn_description;
    @BindView(R.id.img_video)           ImageView img_video;
    @BindView(R.id.img_prize)           ImageView img_prize;
    @BindView(R.id.img_carrier)         ImageView img_carrier;
    @BindView(R.id.img_description)     ImageView img_description;
    @BindView(R.id.btn_menu)            ImageView btn_menu;

    /***
     * Result Summary*/
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    List<Skill> summary = new ArrayList<>();
    ResultAdapter resultAdapter;
    ResultAdapter.Listener resultListener = new ResultAdapter.Listener() {
        @Override
        public void onClickItem(int index) {
            ResultDialog dialog = new ResultDialog(ProfileActivity.this, summary.get(index).description, summary.get(index).value);
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    @BindView(R.id.view_pager) ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;

    VideoFragment.Listener videoListener = new VideoFragment.Listener() {
        @Override
        public void sendReport(Feed feed){
            ReportDialog dialog = new ReportDialog(ProfileActivity.this, "Report", "");
            dialog.setListener(value -> {
                showProgress();
                API.reportFeed(ProfileActivity.this, feed, value, new APICallback<Boolean>() {
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
    };
    PrizeFragment.Listener prizeListener = new PrizeFragment.Listener() {
    };
    CareerFragment.Listener carrierListener = new CareerFragment.Listener() {
    };
    TeamFragment.Listener teamListener = new TeamFragment.Listener() {
    };
    DescriptionFragment.Listener descriptionListener = new DescriptionFragment.Listener() {
        @Override
        public void onClickAccess() {
            Intent intent = new Intent(ProfileActivity.this, EditDescriptionActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
        @Override
        public void onClickStrength() {
            Intent intent = new Intent(ProfileActivity.this, AddStrengthActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    };
    HistoryFragment.Listener historyListener = new HistoryFragment.Listener() {
        @Override
        public void onClickPhone(String value) {
            TextInputDialog dialog = new TextInputDialog(ProfileActivity.this, getString(R.string.phone), value);
            dialog.setListener(new TextInputDialog.Listener() {
                @Override
                public void onClickOK(String value) {
                    if(!isValidPhone(value)){
                        Toast.makeText(ProfileActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress();
                    API.updateUserFields(ProfileActivity.this, "phone", value, new APICallback<User>() {
                        @Override
                        public void onSuccess(User response) {
                            //success
                            dismissProgress();
                            Fragment frag = fragments[3];
                            if (frag instanceof HistoryFragment){
                                AppData.user = response;
                                ((HistoryFragment)frag).setPhoneNumber(value);
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });

                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }

        @Override
        public void onClickWeb(String value) {
            TextInputDialog dialog = new TextInputDialog(ProfileActivity.this, getString(R.string.website), value);
            dialog.setListener(new TextInputDialog.Listener() {
                @Override
                public void onClickOK(String value) {
                    if(!isValidUrl(value)){
                        Toast.makeText(ProfileActivity.this, "Invalid Url Address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress();
                    API.updateUserFields(ProfileActivity.this, "web_url", value, new APICallback<User>() {
                        @Override
                        public void onSuccess(User response) {
                            //success
                            dismissProgress();
                            Fragment frag = fragments[3];
                            if (frag instanceof HistoryFragment){
                                AppData.user = response;
                                ((HistoryFragment)frag).setUrl(value);
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });

                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }

        @Override
        public void onClickText(String value){
            AddDescriptionDlg dlg = new AddDescriptionDlg(ProfileActivity.this, getString(R.string.prifile_history), value);
            dlg.setListener(new AddDescriptionDlg.Listener() {
                @Override
                public void onClickOK(String value) {
                    if(value.length() == 0){
                        Toast.makeText(ProfileActivity.this, "Invalid Description", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress();
                    API.updateUserFields(ProfileActivity.this, "desc_str", value, new APICallback<User>() {
                        @Override
                        public void onSuccess(User response) {
                            //success
                            dismissProgress();
                            Fragment frag = fragments[3];
                            if (frag instanceof HistoryFragment){
                                AppData.user = response;
                                ((HistoryFragment)frag).setDescription(value);
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });

                }
            });
            View decorView = dlg.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dlg.show();
        }

        @Override
        public void onClickEmail(String value){
            TextInputDialog dialog = new TextInputDialog(ProfileActivity.this, getString(R.string.email), value);
            dialog.setListener(new TextInputDialog.Listener() {
                @Override
                public void onClickOK(String value) {
                    if(!isValidEmail(value)){
                        Toast.makeText(ProfileActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress();
                    API.updateUserFields(ProfileActivity.this, "email", value, new APICallback<User>() {
                        @Override
                        public void onSuccess(User response) {
                            //success
                            dismissProgress();
                            Fragment frag = fragments[3];
                            if (frag instanceof HistoryFragment){
                                AppData.user = response;
                                ((HistoryFragment)frag).setEmailAddress(value);
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });

                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };


    private User user;
    private TinyDB tinyDB;

    @BindView(R.id.layout_menu_mask)    RelativeLayout layout_menu_maks;
    @BindView(R.id.layout_menu)         RelativeLayout layout_menu;
    @BindView(R.id.layout_menu_other)   RelativeLayout layout_menu_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        tinyDB = new TinyDB(this);
        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    @OnClick(R.id.btn_resume) void onClickResume(){
        Intent intent = new Intent(this, AddResumeActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    }

    FFmpeg ffmpeg;

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
                    thumbnail = FileUtil.fromBitmap(ProfileActivity.this, bitmap);
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
            dismissProgress();
            showToast(e.getLocalizedMessage());
        }
    }

    File video;
    File thumbnail;
    File careerLogo;

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
        if (requestCode == EDIT_PROFILE && resultCode == RESULT_OK) {
            user = AppData.user;
            setProfile();
        }
        if (requestCode == YOUTUBE_SEARCH && resultCode == RESULT_OK) {
            VideoItem youtube = data.getExtras().getParcelable(Constants.YOUTUBE_ITEM);
            urlDialog.setVideoItem(youtube);
        }

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            if (nPickImg_Mode == Constants.PICK_MODE_NEWS){
                String filenames = mPaths.get(0);
                newsDialog.setUploadedLogo(filenames);
            } else if (nPickImg_Mode == Constants.PICK_MODE_CAREER){
                careerLogo = new File(mPaths.get(0));
                careerDialog.setUploadedLogo(true);
            }
            nPickImg_Mode = Constants.PICK_MODE_STORY;
        }
    }

    void setProfile() {
        if (user.photo_url != null && !user.photo_url.isEmpty()) {
            if(user.photo_url.contains("http")){
                Glide.with(this).load(user.photo_url).into(img_avata);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + user.photo_url).into(img_avata);
            }
        } else {
            img_avata.setImageResource(R.mipmap.default_avata);
        }
        txt_name.setText(user.username);
        if (user.id.equals(AppData.user.id)) {
            btn_plus.setVisibility(View.VISIBLE);
            btn_menu.setVisibility(View.VISIBLE);
            layout_button.setVisibility(View.GONE);
        } else {
            btn_plus.setVisibility(View.GONE);
            btn_menu.setVisibility(View.VISIBLE);
            layout_button.setVisibility(View.VISIBLE);
            btn_follow.setVisibility(View.INVISIBLE);
            API.checkFollow(this, user.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    btn_follow.setVisibility(View.VISIBLE);
                    if (response) {
                        btn_follow.setText(getString(R.string.following));
                    } else {
                        btn_follow.setText(getString(R.string.follow));
                    }
                }
                @Override
                public void onFailure(String error) {
                }
            });
        }
        btn_sport.setText(getSportName(user.sport_id));
        btn_type.setText(getTypeName(user.group_id));
        btn_specialty.setText(user.contract);
        txt_club.setText(user.club);
        txt_recommend.setText(user.recommends + "");
        txt_category.setText(user.category);
        txt_position.setText(user.position);
        String c_code = MyApp.countries.get(user.country.trim());
        if (user.country.trim().equals("USA"))
            c_code = "US";
        if (c_code != null){
            Drawable flag = FlagKit.drawableWithFlag(this, c_code.toLowerCase());
            img_flag.setImageDrawable(flag);
        } else{
            img_flag.setImageDrawable(null);
        }
        if (user.city != null)
            txt_country.setText(user.city);
        else
            txt_country.setText("");
        // get sport summary
        if(user.group_id == Constants.COMPANY || user.group_id == Constants.AGENT || user.group_id == Constants.STAFF){
            recycler_view.setVisibility(View.GONE);
        } else{
            recycler_view.setVisibility(View.VISIBLE);
            API.getSportResultSummary(this, user, new APICallback<List<Pair<String, Integer>>>() {
                @Override
                public void onSuccess(List<Pair<String, Integer>> response) {
                    summary.clear();
                    summary.addAll(getSportSkills(user.group_id == Constants.PLAYER ? user.sport_id : Constants.STATS_COACH, response));
                    resultAdapter.setDataList(summary);
                }
                @Override
                public void onFailure(String error) {
                    //showToast(error);
                }
            });
        }


        API.viewProfile(this, user.id, new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer response) {
                txt_recommend.setText(prettyCount(response));
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    void setActivity() {
        layout_menu_maks.setVisibility(View.GONE);

        loadFFMpegBinary();
        setProfile();

        resultAdapter = new ResultAdapter(this, summary, resultListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(resultAdapter);

        // create dialog
        urlDialog = new UploadURLDialog(this);
        videoDialog = new UploadVideoDialog(this);
        addDialog = new AddDialog(this);
        prizeDialog = new AddPrizeDialog(this);
        careerDialog = new CareerDialog(this);
        newsDialog = new NewsDialog(this);

        //user type
        if (user.group_id < Constants.TEAM_CLUB){
            btn_video.setText(getString(R.string.videos));
            btn_prize.setText(getString(R.string.prizes));
            btn_carrier.setText(getString(R.string.career));
            btn_description.setText(getString(R.string.description));
            // create fragment
            fragments = new Fragment[] {
                    new VideoFragment(this, user, videoListener),
                    new PrizeFragment(this, user, prizeListener),
                    new CareerFragment(this, user, carrierListener),
                    new DescriptionFragment(this, user, descriptionListener),
            };
        } else {
            btn_video.setText(getString(R.string.profile_news));
            btn_prize.setText(getString(R.string.profile_trophies));
            btn_carrier.setText(getString(R.string.profile_team));
            btn_description.setText(getString(R.string.prifile_history));
                // create fragment
            fragments = new Fragment[] {
                    new VideoFragment(this, user, videoListener),
                    new PrizeFragment(this, user, prizeListener),
                    new TeamFragment(this, user, teamListener),
                    new HistoryFragment(this, user, historyListener),
            };
        }


        adapter = new PagerAdapter(this, fragments, getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}
            @Override
            public void onPageSelected(int i) { setTabTitle(i); }
            @Override
            public void onPageScrollStateChanged(int i) {}
        });
        view_pager.setCurrentItem(0);
    }

    void setTabTitle(int position) {
        btn_video.setTextColor(getResources().getColor(R.color.navigation_normal));
        btn_prize.setTextColor(getResources().getColor(R.color.navigation_normal));
        btn_carrier.setTextColor(getResources().getColor(R.color.navigation_normal));
        btn_description.setTextColor(getResources().getColor(R.color.navigation_normal));
        img_video.setVisibility(View.INVISIBLE);
        img_prize.setVisibility(View.INVISIBLE);
        img_carrier.setVisibility(View.INVISIBLE);
        img_description.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                btn_video.setTextColor(getResources().getColor(R.color.navigation_active));
                img_video.setVisibility(View.VISIBLE);
                break;
            case 1:
                btn_prize.setTextColor(getResources().getColor(R.color.navigation_active));
                img_prize.setVisibility(View.VISIBLE);
                break;
            case 2:
                btn_carrier.setTextColor(getResources().getColor(R.color.navigation_active));
                img_carrier.setVisibility(View.VISIBLE);
                break;
            case 3:
                btn_description.setTextColor(getResources().getColor(R.color.navigation_active));
                img_description.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.img_avata) void onClickAvatar(){
        Story st = AppData.last_stories.get(user.sport_id);
        if(st != null) {
            Intent intent = new Intent(ProfileActivity.this, ShowStoryActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_recommend) void onClickRecommend(){
        if(!user.id.equals(AppData.user.id)){
            showProgress();
            API.recommendUser(this, user.id, new APICallback<Integer>() {
                @Override
                public void onSuccess(Integer response) {
                    dismissProgress();
                    user.recommends = response;
                    setProfile();
                }

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() { finish(); }

    @OnClick(R.id.btn_message) void onClickMessage() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    }
    @OnClick(R.id.btn_stats) void onClickState() {
        if(user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF ){
            Intent intent = new Intent(this, ClubChartActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, StatsActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    }
    @OnClick(R.id.btn_video) void onClickVideo() {
        view_pager.setCurrentItem(0);
    }
    @OnClick(R.id.btn_prize) void onClickPrize() {
        view_pager.setCurrentItem(1);
    }
    @OnClick(R.id.btn_carrier) void onClickCarrier() {
        view_pager.setCurrentItem(2);
    }
    @OnClick(R.id.btn_description) void onClickDescription() {
        view_pager.setCurrentItem(3);
    }

    @OnClick(R.id.btn_follow) void onClickFollow() {
        btn_follow.setVisibility(View.INVISIBLE);
        if (btn_follow.getText().toString().equals(getString(R.string.follow))) {
            API.addFollow(this, user.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    if (response) {
                        btn_follow.setText(getString(R.string.following));
                    }
                    btn_follow.setVisibility(View.VISIBLE);
                }
                @Override
                public void onFailure(String error) {
                    btn_follow.setVisibility(View.VISIBLE);
                    showToast(error);
                }
            });
        } else {
            API.deleteFollow(this, user.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    if (response) {
                        btn_follow.setText(getString(R.string.follow));
                    }
                    btn_follow.setVisibility(View.VISIBLE);
                }
                @Override
                public void onFailure(String error) {
                    btn_follow.setVisibility(View.VISIBLE);
                    showToast(error);
                }
            });
        }
    }

    @OnClick(R.id.layout_menu_mask) void onClickMenuHide() {

        if (user.id.equals(AppData.user.id)){
            layout_menu.animate()
                    .translationXBy(0)
                    .translationX(layout_menu.getWidth())
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_menu.setVisibility(View.GONE);
                            layout_menu_maks.setVisibility(View.GONE);
                        }
                    });
        } else{
            layout_menu_other.animate()
                    .translationXBy(0)
                    .translationX(layout_menu_other.getWidth())
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_menu_other.setVisibility(View.GONE);
                            layout_menu_maks.setVisibility(View.GONE);
                        }
                    });
        }

    }

    @OnClick(R.id.btn_menu) void onClickMenuShow(){
        layout_menu_maks.setVisibility(View.VISIBLE);
        if (user.id.equals(AppData.user.id)){
            layout_menu_other.setVisibility(View.GONE);
            layout_menu.animate()
                    .translationXBy(layout_menu.getWidth())
                    .translationX(0).alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            layout_menu.setVisibility(View.VISIBLE);
                            layout_menu.setAlpha(0.0f);
                        }
                    });
        } else{
            layout_menu.setVisibility(View.GONE);
            layout_menu_other.animate()
                    .translationXBy(layout_menu_other.getWidth())
                    .translationX(0).alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            layout_menu_other.setVisibility(View.VISIBLE);
                            layout_menu_other.setAlpha(0.0f);
                        }
                    });
        }
    }

    /**
     * Dialog Event
     * */
    UploadURLDialog urlDialog;
    UploadVideoDialog videoDialog;
    AddDialog addDialog;
    AddPrizeDialog prizeDialog;
    CareerDialog careerDialog;
    NewsDialog newsDialog;
    int nPickImg_Mode = Constants.PICK_MODE_STORY;

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
            Intent intent = new Intent(ProfileActivity.this, PublishYoutubeActivity.class);
            intent.putExtra(Constants.YOUTUBE_ID, youtube.id);
            intent.putExtra(Constants.VIDEO_TITLE, title);
            intent.putExtra(Constants.THUMBNAIL_URL, youtube.thumbnailURL);
            startActivity(intent);
        }
        @Override
        public void onClickUrl() {
            Intent intent = new Intent(ProfileActivity.this, YoutubeSearchActivity.class);
            startActivityForResult(intent, YOUTUBE_SEARCH);
        }
    };

    UploadVideoDialog.Listener uploadVideoListener = new UploadVideoDialog.Listener() {
        @Override
        public void onAddStory(){
            startActivity(new Intent(ProfileActivity.this, PublishStoryActivity.class));
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
            Intent intent = new Intent(ProfileActivity.this, PublishVideoActivity.class);
            intent.putExtra(Constants.VIDEO_URL, video.getAbsolutePath());
            intent.putExtra(Constants.VIDEO_TITLE, title);
            intent.putExtra(Constants.THUMBNAIL_URL, thumbnail.getAbsolutePath());
            startActivity(intent);
        }
        @Override
        public void onClickPlus() {
            new VideoPicker.Builder(ProfileActivity.this)
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

    AddPrizeDialog.Listener addPrizeListener = new AddPrizeDialog.Listener() {
        @Override
        public void onClickSubmit(String title, String club, String year, int icon) {
            Prize prize = new Prize();
            prize.user_id = user.id;
            prize.title = title;
            prize.club = club;
            prize.year = year;
            prize.icon = icon;
            showProgress();
            API.savePrize(ProfileActivity.this, prize, new APICallback<Prize>() {
                @Override
                public void onSuccess(Prize response) {
                    dismissProgress();
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }
    };

    CareerDialog.Listener carrierDialogListener = new CareerDialog.Listener() {
        @Override
        public void onClickLogo() {
            nPickImg_Mode = Constants.PICK_MODE_CAREER;
            new ImagePicker.Builder(ProfileActivity.this)
                    .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                    .compressLevel(ImagePicker.ComperesLevel.HARD)
                    .directory(ImagePicker.Directory.DEFAULT)
                    .extension(ImagePicker.Extension.JPG)
                    .scale(500, 500)
                    .allowMultipleImages(false)
                    .enableDebuggingMode(true)
                    .build();
        }

        @Override
        public void onClickSubmit(final String club, final String sport, final int day, final int month, final int year, final int tday, final int tmonth, final int tyear, final String position, final String location, boolean has_image) {
            if (has_image) {
                String file_name = "career_" + AppData.user.id + "_" + System.currentTimeMillis()/1000;
                showProgress();
                API.uploadImage(file_name, careerLogo, new APICallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Career carrier = new Career();
                        carrier.user_id = user.id;
                        carrier.club = club;
                        carrier.sport_id = sport;
                        carrier.day = day;
                        carrier.month = month;
                        carrier.year = year;
                        carrier.tday = tday;
                        carrier.tmonth = tmonth;
                        carrier.tyear = tyear;
                        carrier.position = position;
                        carrier.location = location;
                        carrier.logo = response;
                        API.saveCareer(ProfileActivity.this, carrier, new APICallback<Career>() {
                            @Override
                            public void onSuccess(Career response) {
                                dismissProgress();
                            }
                            @Override
                            public void onFailure(String error) {
                                dismissProgress();
                                showToast(error);
                            }
                        });
                    }
                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                    }
                });
            } else {
                Career carrier = new Career();
                carrier.user_id = user.id;
                carrier.club = club;
                carrier.sport_id = sport;
                carrier.day = day;
                carrier.month = month;
                carrier.year = year;
                carrier.tday = tday;
                carrier.tmonth = tmonth;
                carrier.tyear = tyear;
                carrier.position = position;
                carrier.location = location;
                carrier.logo = "";
                showProgress();
                API.saveCareer(ProfileActivity.this, carrier, new APICallback<Career>() {
                    @Override
                    public void onSuccess(Career response) {
                        dismissProgress();
                    }
                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                    }
                });
            }
        }
    };

    AddDialog.Listener addDialogListener = new AddDialog.Listener() {
        @Override
        public void onClickUploadVideo() {
            videoDialog.setListener(uploadVideoListener);
            View decorView = videoDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            videoDialog.show();
        }
        @Override
        public void onClickAddPrize() {
            prizeDialog.setListener(addPrizeListener);
            View decorView = prizeDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            prizeDialog.show();
        }
        @Override
        public void onClickCarrierRoadmap() {
            careerDialog.setListener(carrierDialogListener);
            View decorView = careerDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            careerDialog.show();
        }
        @Override
        public void onClickAddDescription() {
            Intent intent = new Intent(ProfileActivity.this, EditDescriptionActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
        @Override
        public void onClickAddNews() {
            newsDialog.setListener(newsListener);
            View decorView = newsDialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            newsDialog.show();
            newsDialog.initialize();
        }
        @Override
        public void onClickAddHistory() {
            String desc_str = AppData.user.desc_str;
            if (desc_str.length() == 0) desc_str = "Write your Bio...";
            AddDescriptionDlg dlg = new AddDescriptionDlg(ProfileActivity.this, getString(R.string.prifile_history), AppData.user.desc_str);
            dlg.setListener(new AddDescriptionDlg.Listener() {
                @Override
                public void onClickOK(String value) {
                    if(value.length() == 0){
                        Toast.makeText(ProfileActivity.this, "Invalid Description", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress();
                    API.updateUserFields(ProfileActivity.this, "desc_str", value, new APICallback<User>() {
                        @Override
                        public void onSuccess(User response) {
                            //success
                            dismissProgress();
                            Fragment frag = fragments[3];
                            if (frag instanceof HistoryFragment){
                                AppData.user = response;
                                ((HistoryFragment)frag).setDescription(value);
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });
                }
            });
            View decorView = dlg.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dlg.show();
        }
        @Override
        public void onClickRemove() {
        }
    };

    NewsDialog.Listener newsListener = new NewsDialog.Listener() {
        @Override
        public void onSubmit(final String title, final String desc, String paths) {
            if(paths.length() > 0){
                String file_name = "video_" + AppData.user.id + "_" + System.currentTimeMillis()/1000;
                showProgress();
                API.uploadArticles(file_name, paths, new APICallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        Feed feed = new Feed();
                        feed.type = Constants.NEWS;
                        feed.user = AppData.user;
                        feed.title = title;
                        feed.video_url = "";
                        feed.thumbnail_url = "";
                        feed.sport_id = AppData.user.sport_id;
                        feed.like_count = 0;
                        feed.timestamp = System.currentTimeMillis() / 1000;
                        feed.shared = 1;
                        feed.mode = Constants.FEED_PUBLIC;
                        feed.tagged = "";
                        feed.articles = response;
                        feed.desc_str = desc;
                        API.saveFeed(ProfileActivity.this,  feed, new APICallback<Feed>() {
                            @Override
                            public void onSuccess(Feed response) {
                                dismissProgress();
                            }
                            @Override
                            public void onFailure(String error) {
                                dismissProgress();
                                showToast(error);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        }

        @Override
        public void onAddImages() {
            nPickImg_Mode = Constants.PICK_MODE_NEWS;
            new ImagePicker.Builder(ProfileActivity.this)
                    .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                    .compressLevel(ImagePicker.ComperesLevel.HARD)
                    .directory(ImagePicker.Directory.DEFAULT)
                    .extension(ImagePicker.Extension.JPG)
                    .scale(500, 500)
                    .allowMultipleImages(false)
                    .enableDebuggingMode(false)
                    .build();
        }
    };

    @OnClick(R.id.btn_plus) void onClickPlus() {
        addDialog.setListener(addDialogListener);
        View decorView = addDialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        addDialog.show();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void updateUserField(final String field, final String val){
        showProgress();
        API.updateUserFields(this, field, val, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                //success
                dismissProgress();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_change_password) void onClickChangePassword(){
        onClickMenuHide();
        startActivity(new Intent(this, ChangePassActivity.class));
    }

    @OnClick(R.id.btn_view) void onClickView(){
        onClickMenuHide();
        startActivity(new Intent(this, AudienceActivity.class));
    }

    @OnClick(R.id.btn_subscriptions) void onClickSubscriptions(){
        onClickMenuHide();
        startActivity(new Intent(this, SubscriptionActivity.class));
    }

    @OnClick(R.id.btn_subscriber) void onClickSubscribers(){
        onClickMenuHide();
        startActivity(new Intent(this, SubscriberActivity.class));
    }

    @OnClick(R.id.btn_blocked) void onClickBlocked(){
        onClickMenuHide();
        startActivity(new Intent(this, BlockActivity.class));
    }

    @OnClick(R.id.btn_recorded) void onClickRecorded(){
        onClickMenuHide();
        startActivity(new Intent(this, RecordedActivity.class));
    }

    @OnClick(R.id.btn_advertisment) void onClickAdvertisment(){
        onClickMenuHide();
        startActivity(new Intent(this, AdvertismentActivity.class));
    }

    @OnClick(R.id.btn_conditions) void onClickConditions(){
        onClickMenuHide();
        startActivity(new Intent(this, TermsActivity.class));
    }

    @OnClick(R.id.btn_contact) void onClickContact(){
        onClickMenuHide();
        startActivity(new Intent(this, ContactUsActivity.class));
    }

    @OnClick(R.id.btn_disconnect) void onClickDisconnect(){
        tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_modify_profile) void onClickModifyProfile(){
        startActivity(new Intent(this, EditProfileActivity.class));
        finish();
    }

    @OnClick(R.id.btn_block) void onClickBlock(){
        onClickMenuHide();
        API.addBlock(this, user.id, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                showToast("Blocked");
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });

    }

    @OnClick(R.id.btn_report) void onClickReport(){
        onClickMenuHide();
        ReportDialog dialog = new ReportDialog(this, "Report", "");
        dialog.setListener(new ReportDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                showProgress();
                API.reportUser(ProfileActivity.this, user.id, value, new APICallback<Boolean>() {
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

}
