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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserNameDialog extends DialogFragment {
    @BindView(R.id.et_user_name)
    EditText userName;
    @BindView(R.id.tv_cancel)
    TextView cancel;
    @BindView(R.id.tv_done)
    TextView done;

    String name;

    private UserNameDialogListener mCallback;

    public static UserNameDialog NewInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        UserNameDialog fragment = new UserNameDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (UserNameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement UserNameDialogListener");
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
        View view = inflater.inflate(R.layout.dialog_user_name, null);
        ButterKnife.bind(this, view);
        userName.setText(name);
        userName.setSelection(userName.getText().length());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().trim().length() > 2) {
                    mCallback.onDoneClicked(userName.getText().toString().trim());
                    UserNameDialog.this.getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_valid_name), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserNameDialog.this.getDialog().dismiss();
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

    public interface UserNameDialogListener {
        void onDoneClicked(String userName);
    }
}
