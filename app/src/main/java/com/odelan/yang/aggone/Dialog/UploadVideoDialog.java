package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.odelan.yang.aggone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadVideoDialog extends Dialog {
    Listener mListener;
    Context context;
    boolean isUploadedVideo;

    @BindView(R.id.edit_title) EditText edit_title;

    public UploadVideoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.isUploadedVideo = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_upload_video);
        ButterKnife.bind(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }
    public void setUploadedVideo(boolean value) {
        isUploadedVideo = value;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }

    @OnClick(R.id.btn_url) void onClickURL() {
        if (mListener != null) mListener.onClickURL();
        dismiss();
    }

    @OnClick(R.id.btn_submit) void onClickSubmit() {
        if (edit_title.getText().toString().isEmpty()) {
            if (mListener != null) mListener.onError("Please input title");
            return;
        }
        if (isUploadedVideo == false) {
            if (mListener != null) mListener.onError("Please add video");
            return;
        }
        if (mListener != null) mListener.onClickSubmit(edit_title.getText().toString());
        edit_title.setText("");
        isUploadedVideo = false;
        dismiss();
    }

    @OnClick(R.id.btn_plus) void onClickPlus() {
        if (mListener != null) mListener.onClickPlus();
    }
    @OnClick(R.id.btn_story) void onClickStory(){
        if (mListener != null) mListener.onAddStory();
        dismiss();
    }

    public interface Listener {
        void onClickURL();
        void onClickSubmit(String title);
        void onClickPlus();
        void onError(String error);
        void onAddStory();
    }
}
