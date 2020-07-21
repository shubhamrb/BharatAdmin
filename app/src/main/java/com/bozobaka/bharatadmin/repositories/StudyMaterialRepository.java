package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class StudyMaterialRepository {
    private static StudyMaterialRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<StudyMaterialModel> studyMaterialList = new ArrayList<>();

    public static StudyMaterialRepository getInstance() {
        if (instance == null) {
            instance = new StudyMaterialRepository();
        }
        return instance;
    }

    public MutableLiveData<List<StudyMaterialModel>> getNotes(String classId) {
        final MutableLiveData<List<StudyMaterialModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .document(classId)
                .collection("studyMaterial")
                .whereEqualTo("deleted", false)
                //.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            studyMaterialList.clear();
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    StudyMaterialModel studyMaterialModel = document.toObject(StudyMaterialModel.class);
                                    studyMaterialList.add(studyMaterialModel);
                                }
                                Collections.sort(studyMaterialList, (n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()));
                                data.setValue(studyMaterialList);
                            }
                        } else {
                            Log.d("Error Fetching Study material: ", e.getMessage());
                        }

                    }
                });
        return data;
    }
}