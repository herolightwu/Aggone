package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.aggone.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportDialog extends Dialog {
    @BindView(R.id.txt_title) TextView txt_title;
    @BindView(R.id.edit_value) EditText edit_value;

    Context context;
    Listener mListener;
    private String title;
    private String value;
    public ReportDialog(@NonNull Context context, String title, String value) {
        super(context);
        this.context = context;
        this.title = title;
        this.value = value;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_report);
        ButterKnife.bind(this);

        txt_title.setText(title);
        edit_value.setText(value);
        setCancelable(false);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }

    @OnClick(R.id.btn_ok) void onClickOK() {
        if (edit_value.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input " + title, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) mListener.onClickOK(edit_value.getText().toString());
        dismiss();
    }

    public interface Listener {
        void onClickOK(String value);
    }
}
