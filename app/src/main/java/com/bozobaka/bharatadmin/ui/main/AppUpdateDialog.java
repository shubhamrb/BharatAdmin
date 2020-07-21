package com.bozobaka.bharatadmin.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.bozobaka.bharatadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppUpdateDialog extends DialogFragment {
    @BindView(R.id.tv_cancel)
    TextView cancel;
    @BindView(R.id.tv_done)
    TextView done;

    String appVersion;

    private AppUpdateDialogListener mCallback;

    public static AppUpdateDialog NewInstance(String ver) {
        Bundle args = new Bundle();
        args.putString("ver", ver);
        AppUpdateDialog fragment = new AppUpdateDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AppUpdateDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AppUpdateDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        appVersion = args.getString("ver");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_app_update, null);
        ButterKnife.bind(this, view);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onUpdateAppClicked(true);
                AppUpdateDialog.this.getDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onUpdateAppClicked(false);
                AppUpdateDialog.this.getDialog().dismiss();
            }
        });

        builder.setView(view)
                .setCancelable(false);
        // Add action buttons
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public interface AppUpdateDialogListener {
        void onUpdateAppClicked(boolean doUpdate);
    }
}
