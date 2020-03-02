package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDialog extends Dialog {
    Listener mListener;

    @BindView(R.id.layout_add_history)    LinearLayout history_panel;
    @BindView(R.id.layout_add_news)    LinearLayout news_panel;
    @BindView(R.id.layout_add_description)    LinearLayout description_panel;
    @BindView(R.id.layout_carrier_roadmap)      LinearLayout carrier_panel;

    public AddDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add);
        ButterKnife.bind(this);
        setCancelable(false);

        if(AppData.user.group_id < Constants.TEAM_CLUB){
            description_panel.setVisibility(View.VISIBLE);
            carrier_panel.setVisibility(View.VISIBLE);
            history_panel.setVisibility(View.GONE);
            news_panel.setVisibility(View.GONE);
        } else{
            description_panel.setVisibility(View.GONE);
            carrier_panel.setVisibility(View.GONE);
            history_panel.setVisibility(View.VISIBLE);
            news_panel.setVisibility(View.VISIBLE);
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.layout_upload_video) void onClickUploadVideo() {
        if (mListener != null) mListener.onClickUploadVideo();
        dismiss();
    }
    @OnClick(R.id.layout_add_prize) void onClickAddPrize() {
        if (mListener != null) mListener.onClickAddPrize();
        dismiss();
    }
    @OnClick(R.id.layout_carrier_roadmap) void onClickCarrierRoadmap() {
        if (mListener != null) mListener.onClickCarrierRoadmap();
        dismiss();
    }
    @OnClick(R.id.layout_add_description) void onClickAddDescription() {
        if (mListener != null) mListener.onClickAddDescription();
        dismiss();
    }
    @OnClick(R.id.layout_add_news) void onClickAddNews() {
        if (mListener != null) mListener.onClickAddNews();
        dismiss();
    }
    @OnClick(R.id.layout_add_history) void onClickAddHistory() {
        if (mListener != null) mListener.onClickAddHistory();
        dismiss();
    }
    @OnClick(R.id.layout_remove) void onClickRemove() {
        if (mListener != null) mListener.onClickRemove();
        dismiss();
    }

    public interface Listener {
        void onClickUploadVideo();
        void onClickAddPrize();
        void onClickCarrierRoadmap();
        void onClickAddDescription();
        void onClickAddNews();
        void onClickAddHistory();
        void onClickRemove();
    }
}
