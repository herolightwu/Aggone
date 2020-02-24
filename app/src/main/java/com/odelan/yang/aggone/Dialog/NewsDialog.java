package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDialog extends Dialog {

    Listener mListener;
    Context context;
    boolean isUploadedLogo;
    ImageView[] imageViews;

    @BindView(R.id.edit_description)    EditText edit_description;
    @BindView(R.id.edit_title)    EditText edit_title;
    @BindView(R.id.img_news0)    ImageView img0;
    @BindView(R.id.img_news1)    ImageView img1;
    @BindView(R.id.img_news2)    ImageView img2;
    @BindView(R.id.img_layout)    LinearLayout img_layout;

    public NewsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        isUploadedLogo = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_news);
        ButterKnife.bind(this);

        imageViews = new ImageView[] { img0, img1, img2};
        img_layout.setVisibility(View.GONE);
        setCancelable(false);
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.btn_submit) void onClickSubmit(){
        if (edit_title.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) {
            mListener.onSubmit(
                    edit_title.getText().toString(),
                    edit_description.getText().toString(),
                    img_paths);
        }
        dismiss();
    }
    @OnClick(R.id.btn_add_logo) void onClickLogo() {
        if (mListener != null) mListener.onAddImages();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public String img_paths="";

    public void initialize(){
        img_paths = "";
        isUploadedLogo = false;
        img_layout.setVisibility(View.GONE);
        edit_description.setText("");
        edit_title.setText("");
    }
    public void setUploadedLogo(String filenames) {
        img_layout.setVisibility(View.VISIBLE);
        if (filenames.length() > 0){
            isUploadedLogo = true;
            if (img_paths.length() > 0){
                String[] itemps = img_paths.split(",");
                if (itemps.length > 2){
                    img_paths = itemps[1] + "," + itemps[2] + "," + filenames;
                } else{
                    img_paths = img_paths + "," + filenames;
                }
            } else{
                img_paths = filenames;
            }
            String[] ifilenames = img_paths.split(",");
            for (int i = 0; i < ifilenames.length; i++){
                if(ifilenames[i].length() > 0){
                    Glide.with(context).load(new File(ifilenames[i])).into(imageViews[i]);
                }
            }
        } else{
            isUploadedLogo = false;
            img_layout.setVisibility(View.GONE);
        }

    }

    public interface Listener {
        void onSubmit(final String title, final String desc, String paths);
        void onAddImages();
    }
}
