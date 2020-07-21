package com.bozobaka.bharatadmin.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bozobaka.bharatadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstituteNameDialog extends DialogFragment {
    @BindView(R.id.et_institute_name)
    EditText instituteName;
    @BindView(R.id.tv_cancel)
    TextView cancel;
    @BindView(R.id.tv_done)
    TextView done;

    String name;

    private InstituteNameDialogListener mCallback;

    public static InstituteNameDialog NewInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        InstituteNameDialog fragment = new InstituteNameDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (InstituteNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement InstituteNameDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        name = args.getString("name");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_institute_name, null);
        ButterKnife.bind(this, view);
        instituteName.setText(name);
        instituteName.setSelection(instituteName.getText().length());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (instituteName.getText().toString().trim().length() > 2) {
                    mCallback.onInstituteNameDone(instituteName.getText().toString().trim());
                    InstituteNameDialog.this.getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_valid_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstituteNameDialog.this.getDialog().dismiss();
            }
        });

        builder.setView(view)
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public interface InstituteNameDialogListener {
        void onInstituteNameDone(String instituteName);
    }
}
