package com.bozobaka.bharatadmin.ui.classdetails.studyMaterial;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyMaterialOptionDialog extends DialogFragment {

    @BindView(R.id.tv_folder)
    TextView folder;
    @BindView(R.id.tv_video)
    TextView video;

    @BindView(R.id.tv_doc)
    TextView document;

    String classId;
    private static final int ADD_NOTE_REQUEST = 1;

    private StudyMaterialOptionDialogListener mCallback;

    public static StudyMaterialOptionDialog NewInstance(String classId) {
        Bundle args = new Bundle();
        args.putString("classId", classId);
        StudyMaterialOptionDialog fragment = new StudyMaterialOptionDialog();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StudyMaterialOptionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement InstituteNameDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        classId = args.getString("classId");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_study_material_option, null);
        ButterKnife.bind(this, view);

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyMaterialOptionDialog.this.getDialog().dismiss();
                Intent intent = new Intent(getActivity(), AddFolderActivity.class);
                intent.putExtra("ClassId", classId);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyMaterialOptionDialog.this.getDialog().dismiss();
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyMaterialOptionDialog.this.getDialog().dismiss();
                Intent intent = new Intent(getActivity(), AddDocumentActivity.class);
                intent.putExtra("ClassId", classId);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
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

    public interface StudyMaterialOptionDialogListener {
        void onDone(String instituteName);
    }
}
