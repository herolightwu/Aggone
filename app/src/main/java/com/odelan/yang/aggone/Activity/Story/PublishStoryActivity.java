package com.odelan.yang.aggone.Activity.Story;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.TagUserAdapter;
import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity;
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder;

public class PublishStoryActivity extends BaseActivity {

    public final static int PHOTO_EDITOR_REQUEST_CODE = 2345;
    public final static int REQUEST_STORAGE_WRITE_PERMISSION = 6789;

    @BindView(R.id.img_story)    ImageView img_story;
    @BindView(R.id.txt_name)    TextView txt_name;
    @BindView(R.id.img_photo)    CircleImageView img_photo;
    @BindView(R.id.input_layout)    RelativeLayout input_layout;
    @BindView(R.id.input_panel)    RelativeLayout input_panel;
    @BindView(R.id.tag_layout)     LinearLayout tag_layout;
    @BindView(R.id.edit_search)    EditText edit_search;
    @BindView(R.id.recycler_view)  RecyclerView recyclerView;
    @BindView(R.id.btn_search_clear)    ImageView btn_clear;

    TagUserAdapter adapter;
    List<User> all_users = new ArrayList<>();
    String newFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_story);

        ButterKnife.bind(this);
        setActivity();
        getAllUsers();
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseStoryImage();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_PERMISSION);
        } else {
            chooseStoryImage();
        }
    }

    @OnClick(R.id.btn_publish) void onClickPublish(){
        if(newFilePath.length() > 0){
            String file_name = "story_" + AppData.user.id + "_" + System.currentTimeMillis()/1000;
            showProgress();
            File file = new File(newFilePath);
            API.uploadStory(file_name, file, new APICallback<String>() {
                @Override
                public void onSuccess(String response) {
                    Story story = new Story();
                    story.user = AppData.user;
                    story.image = response;
                    story.tags = getTagged();
                    story.timebeg = System.currentTimeMillis() / 1000;
                    story.timeend = story.timebeg + 24*60*60;
                    API.saveStory(PublishStoryActivity.this, story, new APICallback<Story>() {
                        @Override
                        public void onSuccess(Story response) {
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

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }

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

    @Override
    public void onResume(){
        super.onResume();
        if(newFilePath.length() > 0){
            Glide.with(this).load(Uri.fromFile(new File(newFilePath))).into(img_story);
        }
    }

    @OnClick(R.id.btn_tag) void onClickTag(){
        input_panel.setVisibility(View.VISIBLE);
        tag_layout.setVisibility(View.VISIBLE);
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
                    }
                });
        MyApp.hideKeyboard(this);
    }
    @OnClick(R.id.btn_search_clear) void onClickClear(){
        edit_search.setText("");
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

        if (AppData.user.photo_url != null && !AppData.user.photo_url.isEmpty()) {
            if (AppData.user.photo_url.contains("http")){
                Glide.with(this).load(AppData.user.photo_url).into(img_photo);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + AppData.user.photo_url).into(img_photo);
            }
        } else {
            img_photo.setImageResource(R.mipmap.default_avata);
        }
        txt_name.setText(AppData.user.username);
        input_panel.setVisibility(View.GONE);
        tag_layout.setVisibility(View.GONE);
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    @OnClick(R.id.img_story) void onClickImage(){
        chooseStoryImage();
    }

    void chooseStoryImage(){
        new ImagePicker.Builder(this)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            String filenames = mPaths.get(0);

            Glide.with(this).load(Uri.fromFile(new File(filenames))).into(img_story);

            String foldername = "Aggone/Stories/";
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + foldername);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final String filepath = folder.getAbsolutePath() + "/edit_" + System.currentTimeMillis()/1000 + ".png";//"/edit_photo.png";
//            File file = new File(filepath);
//            if(file.exists()){
//                file.delete();
//            }

            try {
                Intent intent = new ImageEditorIntentBuilder(this, filenames, filepath)
                        .withAddText() // Add the features you need
                        .withPaintFeature()
                        .withFilterFeature()
                        .withRotateFeature()
                        .withCropFeature()
                        .withBrightnessFeature()
                        .withSaturationFeature()
                        .withBeautyFeature()
//                        .withStickerFeature()
                        .forcePortrait(true)  // Add this to force portrait mode (It's set to false by default)
                        .build();

                EditImageActivity.start(this, intent, PHOTO_EDITOR_REQUEST_CODE);
            } catch (Exception e) {
                Log.e("Aggone", e.getMessage()); // This could throw if either `sourcePath` or `outputPath` is blank or Null
            }
        }
        if (requestCode == PHOTO_EDITOR_REQUEST_CODE) { // same code you used while starting
            newFilePath = data.getStringExtra("output_path");//EditImageActivity.OUTPUT_PATH
            String sourcepath = data.getStringExtra("source_path");
            boolean isImageEdit = data.getBooleanExtra("is_image_edited", false);//EditImageActivity.IMAGE_IS_EDIT
            if (!isImageEdit) {
                newFilePath = sourcepath;
            }
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(PublishStoryActivity.this).load(Uri.fromFile(new File(newFilePath))).into(img_story);
//                    File file = new File(newFilePath);
//                    if(file.exists()){
//                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        img_story.setImageBitmap(myBitmap);
                    //}
                }
            }, 2000);

        }
    }
}
