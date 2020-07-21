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

public class AddClassMemberDialog extends DialogFragment {
    @BindView(R.id.et_user_name)
    EditText userName;
    @BindView(R.id.tv_cancel)
    TextView cancel;
    @BindView(R.id.et_mobile_no)
    EditText mobileNo;
    @BindView(R.id.tv_done)
    TextView done;

    private AddClassMemberDialogListener mCallback;

    public static AddClassMemberDialog NewInstance() {
        Bundle args = new Bundle();
        AddClassMemberDialog fragment = new AddClassMemberDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AddClassMemberDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddClassMemberDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_class_member, null);
        ButterKnife.bind(this, view);

        done.setOnClickListener(view12 -> {

            String name = userName.getText().toString().trim();
            String mobNo = "+91" + mobileNo.getText().toString().trim();

            if (name.length() <= 2){
                Toast.makeText(getContext(), getResources().getString(R.string.enter_valid_name),
                        Toast.LENGTH_SHORT).show();
                return;
            }else if (mobNo.length() != 13){
                Toast.makeText(getContext(), getResources().getString(R.string.enter_valid_mob_no),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mCallback.onAddMemberClicked(name, mobNo);
            AddClassMemberDialog.this.getDialog().dismiss();
        });

        cancel.setOnClickListener(view1 -> AddClassMemberDialog.this.getDialog().dismiss());

        builder.setView(view)
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public interface AddClassMemberDialogListener {
        void onAddMemberClicked(String userName, String mobileNo);
    }
}
