package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.odelan.yang.aggone.Model.VideoItem;
import com.odelan.yang.aggone.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadURLDialog extends Dialog {
    Listener mListener;
    Context context;

    @BindView(R.id.edit_title) EditText edit_title;
    @BindView(R.id.edit_url) EditText edit_url;

    private VideoItem youtube;

    public UploadURLDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_upload_url);
        ButterKnife.bind(this);
        edit_url.setKeyListener(null);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }
    public void setVideoItem(VideoItem item) {
        this.youtube = item;
        edit_url.setText(item.id);
    }

    @OnClick(R.id.edit_url) void onClickUrl() {
        if (mListener != null) mListener.onClickUrl();
    }
    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.btn_upload) void onClickUpload() {
        if (mListener != null) mListener.onClickUpload();
        dismiss();
    }
    @OnClick(R.id.btn_submit) void onClickSubmit() {
        if (edit_title.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_url.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input link", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) mListener.onClickSubmit(edit_title.getText().toString(), youtube);
        edit_title.setText("");
        edit_url.setText("");
        dismiss();
    }

    public interface Listener {
        void onClickUpload();
        void onClickSubmit(String title, VideoItem youtube);
        void onClickUrl();
    }
}
