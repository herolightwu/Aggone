package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.aggone.R;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteDialog extends Dialog {
    Context context;
    Listener mListener;
    public DeleteDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete);
        ButterKnife.bind(this);

        setCancelable(false);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_cancel) void onClickClose() {
        dismiss();
    }

    @OnClick(R.id.btn_delete) void onClickDelete() {

        if (mListener != null) mListener.onClickDelete();
        dismiss();
    }

    public interface Listener {
        void onClickDelete();
    }
}
