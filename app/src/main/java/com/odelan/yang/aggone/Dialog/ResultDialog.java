package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.odelan.yang.aggone.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultDialog extends Dialog {
    @BindView(R.id.txt_type) TextView txt_type;
    @BindView(R.id.txt_value) TextView txt_value;

    Listener mListener;
    private String type;
    private int value;
    public ResultDialog(@NonNull Context context, String type, int value) {
        super(context);
        this.type = type;
        this.value = value;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_result);
        ButterKnife.bind(this);

        txt_type.setText(type);
        txt_value.setText(String.valueOf(value));
        setCancelable(false);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }

    public interface Listener {
    }
}
