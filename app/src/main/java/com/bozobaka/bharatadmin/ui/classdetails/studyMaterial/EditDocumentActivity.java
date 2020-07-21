package com.bozobaka.bharatadmin.ui.classdetails.studyMaterial;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

public class EditDocumentActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_name)
    TextView toolbarText;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;
    @BindView(R.id.et_document_name)
    TextInputEditText etDocumentName;

    @BindView(R.id.btn_create_document)
    Button btnCreateDocument;
    String classId;
    private StudyMaterialModel studyMaterialModel;
    DocumentReference documentReference;
    FirebaseFirestore db;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_document);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra("ClassId");
        studyMaterialModel = getIntent().getParcelableExtra("STUDY_MATERIAL");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText.setText(getResources().getString(R.string.text_edit_document));
        etDocumentName.setText(studyMaterialModel.getStudyMaterialName());
        btnCreateDocument.setOnClickListener(view -> editDocument());

    }

    private void editDocument() {
        String documentName = etDocumentName.getText().toString();

        if (documentName.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter document name", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();

        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            documentReference = db.collection("classes")
                    .document(classId).collection("studyMaterial").document(studyMaterialModel.getId());

            batch.update(documentReference, "studyMaterialName", documentName);
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

    private void deleteNote(StudyMaterialModel studyMaterialModel) {
        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        studyMaterialModel.setDeleted(true);
        loadingView.setVisibility(View.VISIBLE);
        if (auth.getCurrentUser() != null) {
            db.collection("classes")
                    .document(classId).collection("studyMaterial")
                    .document(studyMaterialModel.getId())
                    .update("deleted", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Folder deleted successfully!");
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
                materialAlertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> deleteNote(studyMaterialModel));
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