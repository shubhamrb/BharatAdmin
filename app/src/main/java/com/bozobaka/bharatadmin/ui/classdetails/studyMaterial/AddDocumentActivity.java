package com.bozobaka.bharatadmin.ui.classdetails.studyMaterial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDocumentActivity extends BaseActivity {
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
    @BindView(R.id.btn_add_file)
    Button btnAddFile;

    Uri docUri,downloadUri;
    String classId;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra("ClassId");

        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarText.setText(getResources().getString(R.string.text_add_new_document));
        btnCreateDocument.setOnClickListener(view -> saveNewDocument());
        btnAddFile.setOnClickListener(view -> uploadNewDocument());
    }

    private void uploadNewDocument() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission required",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {
                Intent intent=new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Choose document to add"),2);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode!=2||resultCode!=RESULT_OK){
            return;
        }
        if (data!=null){
            docUri=data.getData();
            Toast.makeText(getApplicationContext(), "Document added "+docUri, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Something went wrong! Try again...",Toast.LENGTH_SHORT).show();

        }

    }

    private void saveNewDocument() {
        String documentName = etDocumentName.getText().toString();

        if (documentName.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter the document name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (docUri==null) {
            Toast.makeText(getApplicationContext(), "Please select the document", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        StorageReference file_path= FirebaseStorage.getInstance().
                getReference().child("doc_file").child(System.currentTimeMillis()+".pdf");

        file_path.putFile(docUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri=uri;
                        storeOtherInfo(documentName);
                    }
                });
            }
        });



    }

    private void storeOtherInfo(String documentName) {

        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String currentFormattedDate = df.format(c);
        if (auth.getCurrentUser() != null) {
            WriteBatch batch = db.batch();
            DocumentReference documentReference = db.collection("classes")
                    .document(classId).collection("studyMaterial").document();

            StudyMaterialModel studyMaterialModel = new StudyMaterialModel();
            studyMaterialModel.setId(documentReference.getId());
            studyMaterialModel.setStudyMaterialName(documentName);
            studyMaterialModel.setStudyMaterialType("Document");
            studyMaterialModel.setPdfUri(downloadUri.toString());
            studyMaterialModel.setDeleted(false);
            studyMaterialModel.setCreatedBy(auth.getCurrentUser().getUid());
            studyMaterialModel.setCreatedAt(currentFormattedDate);

            batch.set(documentReference, studyMaterialModel);
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
