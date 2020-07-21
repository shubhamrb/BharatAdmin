package com.bozobaka.bharatadmin.ui.classmember;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectClassMemberTypeDialog extends DialogFragment {
    @BindView(R.id.tv_student)
    TextView student;
    @BindView(R.id.tv_teacher)
    TextView teacher;

    private ArrayList<String> membersList = new ArrayList<>();

    private SelectClassMemberTypeDialogListener mCallback;

    public static SelectClassMemberTypeDialog NewInstance(ArrayList<String> membersList) {
        Bundle args = new Bundle();
        args.putStringArrayList("membersList", membersList);
        SelectClassMemberTypeDialog fragment = new SelectClassMemberTypeDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (SelectClassMemberTypeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SelectClassMemberTypeDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        ArrayList<String> members = null;
        if (args != null) {
            members = args.getStringArrayList("membersList");
        }
        if (members != null) {
            membersList.addAll(members);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_class_member_type, null);
        ButterKnife.bind(this, view);

        student.setOnClickListener(view12 -> {
            mCallback.onMemberTypeClicked(true, membersList);
            SelectClassMemberTypeDialog.this.getDialog().dismiss();
        });

        teacher.setOnClickListener(view1 -> {
            mCallback.onMemberTypeClicked(false, membersList);
            SelectClassMemberTypeDialog.this.getDialog().dismiss();
        });

        builder.setView(view)
                .setCancelable(true);
        // Add action buttons
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public interface SelectClassMemberTypeDialogListener {
        void onMemberTypeClicked(boolean isTypeStudent, ArrayList<String> memberList);
    }
}
