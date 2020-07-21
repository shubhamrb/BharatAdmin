package com.bozobaka.bharatadmin.ui.classdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.LiveModel;
import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_name)
    TextView toolbarText;

    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.et_note_name)
    TextInputEditText etNoteName;
    @BindView(R.id.et_note_text)
    TextInputEditText etNoteText;

    @BindView(R.id.btn_create_note)
    Button btnCreateNote;

    String classId;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra("ClassId");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText.setText(getResources().getString(R.string.text_add_new_note));
        btnCreateNote.setOnClickListener(view -> saveNewNote());
    }

    private void saveNewNote() {
        String noteName = etNoteName.getText().toString();
        String noteText = etNoteText.getText().toString();


        if (noteName.trim().isEmpty() ||
                noteText.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all entries", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String currentFormattedDate = df.format(c);
        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            DocumentReference documentReference = db.collection("classes")
                    .document(classId).collection("notes").document();

            NoteModel noteModel = new NoteModel();
            noteModel.setId(documentReference.getId());
            noteModel.setNoteName(noteName);
            noteModel.setNoteText(noteText);
            noteModel.setDeleted(false);
            noteModel.setCreatedBy(auth.getCurrentUser().getUid());
            noteModel.setCreatedAt(currentFormattedDate);
            noteModel.setDate(c);

            batch.set(documentReference, noteModel);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        loadingView.setVisibility(View.GONE);
                        finish();
                    })
                    .addOnFailureListener(this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occurred. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
