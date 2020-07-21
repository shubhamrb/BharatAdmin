package com.bozobaka.bharatadmin.ui.classdetails.studyMaterial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewerActivity extends AppCompatActivity {
    String classId;
    private StudyMaterialModel studyMaterialModel;
    private PDFView pdfView;
    DocumentReference docRef;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String url;
    @BindView(R.id.progress_bar_layout)
    RelativeLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        classId = getIntent().getStringExtra("ClassId");
        studyMaterialModel = getIntent().getParcelableExtra("STUDY_MATERIAL");
        pdfView=(PDFView)findViewById(R.id.pdfView);
        db = FirebaseRepository.getFirestoreInstance();
        auth = FirebaseRepository.getFirebaseAuthInstance();
        loadingView.setVisibility(View.VISIBLE);
        getUrl();
    }

    private void getUrl() {

        if (auth.getCurrentUser() != null) {
            docRef=db.collection("classes")
                    .document(classId).collection("studyMaterial").document(studyMaterialModel.getId());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();
                        if (documentSnapshot!=null && documentSnapshot.exists()){
                            url=documentSnapshot.getString("pdfUri");
                            loadingView.setVisibility(View.GONE);
                            new RetrivePdfStream().execute(url);
                        }
                    }else {
                        Log.e("ERROR : ","Something went wrong");
                    }
                }
            });


        }

    }
    class RetrivePdfStream extends AsyncTask<String,Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                if (urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream).load();
        }
    }

}
