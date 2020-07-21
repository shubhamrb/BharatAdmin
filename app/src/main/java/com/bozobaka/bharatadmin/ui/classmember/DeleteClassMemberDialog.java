package com.bozobaka.bharatadmin.ui.classmember;

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

public class DeleteClassMemberDialog extends DialogFragment {
    @BindView(R.id.et_user_name)
    EditText userName;
    @BindView(R.id.tv_cancel)
    TextView cancel;
    @BindView(R.id.et_mobile_no)
    EditText mobileNo;
    @BindView(R.id.tv_delete)
    TextView delete;

    String name;
    String mobNo;

    private DeleteClassMemberDialogListener mCallback;

    public static DeleteClassMemberDialog NewInstance(String name, String mobileNo) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("mobileNo", mobileNo);
        DeleteClassMemberDialog fragment = new DeleteClassMemberDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (DeleteClassMemberDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DeleteClassMemberDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        name = args.getString("name");
        mobNo = args.getString("mobileNo");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_class_member, null);
        ButterKnife.bind(this, view);
        userName.setText(name);
        mobileNo.setText(mobNo);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDeleteMemberClicked(userName.getText().toString().trim(),
                        mobileNo.getText().toString().trim());
                DeleteClassMemberDialog.this.getDialog().dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteClassMemberDialog.this.getDialog().dismiss();
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

    public interface DeleteClassMemberDialogListener {
        void onDeleteMemberClicked(String userName, String mobileNo);
    }
}
