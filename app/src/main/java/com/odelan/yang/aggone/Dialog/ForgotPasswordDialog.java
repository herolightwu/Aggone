package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.odelan.yang.aggone.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordDialog extends Dialog {
    @BindView(R.id.edit_email) EditText edit_email;

    Context context;
    Listener mListener;
    public ForgotPasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_forgot_password);
        ButterKnife.bind(this);

        setCancelable(false);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }

    @OnClick(R.id.btn_ok) void onClickOK() {
        if (edit_email.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) mListener.onClickOK(edit_email.getText().toString());
        dismiss();
    }

    @OnClick(R.id.btn_cancel) void onClickCancel() {
        dismiss();
    }

    public interface Listener {
        void onClickOK(String email);
    }
}
