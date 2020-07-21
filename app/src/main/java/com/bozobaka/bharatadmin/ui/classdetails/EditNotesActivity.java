package com.bozobaka.bharatadmin.ui.classdetails;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

public class EditNotesActivity extends BaseActivity {
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
    private NoteModel noteModel;
    DocumentReference documentReference;
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra("ClassId");
        noteModel = getIntent().getParcelableExtra("NOTE");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText.setText(getResources().getString(R.string.text_edit_note));
        etNoteName.setText(noteModel.getNoteName());
        etNoteText.setText(noteModel.getNoteText());
        btnCreateNote.setOnClickListener(view -> editNote());

    }

    private void editNote() {
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

        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            documentReference = db.collection("classes")
                    .document(classId).collection("notes").document(noteModel.getId());


            batch.update(documentReference, "noteName", noteName);
            batch.update(documentReference, "noteText", noteText);
            batch.commit()
                    .addOnSuccessListener(this, aVoid -> {
                        loadingView.setVisibility(View.GONE);
                        finish();
                    })
                    .addOnFailureListener(this, e -> {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Some error occured. Please retry!", Toast.LENGTH_SHORT).show();
                    });
        }

    }

    private void deleteNote(NoteModel noteModel) {
        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        noteModel.setDeleted(true);
        loadingView.setVisibility(View.VISIBLE);
        if (auth.getCurrentUser() != null) {
            db.collection("classes")
                    .document(classId).collection("notes")
                    .document(noteModel.getId())
                    .update("deleted", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Note successfully Deleted!");
                            loadingView.setVisibility(View.GONE);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                            loadingView.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Some error occurred! Try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
                materialAlertDialogBuilder.setTitle("Are you sure you want to delete? ");
                materialAlertDialogBuilder.setCancelable(true);
                materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> deleteNote(noteModel));
                materialAlertDialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                materialAlertDialogBuilder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}