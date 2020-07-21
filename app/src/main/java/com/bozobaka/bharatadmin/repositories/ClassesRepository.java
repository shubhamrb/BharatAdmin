package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import com.bozobaka.bharatadmin.models.ClassModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class ClassesRepository {
    private static ClassesRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ClassModel> classModels = new ArrayList<>();

    public static ClassesRepository getInstance() {
        if (instance == null) {
            instance = new ClassesRepository();
        }
        return instance;
    }

    public MutableLiveData<List<ClassModel>> getClasses(String uid) {
        final MutableLiveData<List<ClassModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .whereEqualTo("createdBy", uid)
                .whereEqualTo("deleted", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            classModels.clear();
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    ClassModel classModel = document.toObject(ClassModel.class);
                                    int noOfStudents = 0;
                                    if (classModel.getStudents() != null) {
                                        noOfStudents = classModel.getStudents().size();
                                    }
                                    classModel.setNumberOfStudents(noOfStudents);
                                    classModels.add(classModel);
                                }
                                if (classModels != null && classModels.size() > 0){
                                    Collections.sort(classModels, (c1, c2) -> c1.getClassName().toLowerCase().compareTo(c2.getClassName().toLowerCase()));
                                    data.setValue(classModels);
                                }
                            }
                        } else {
                            Log.d("Error Fetching data: ", e.getMessage());
                        }

                    }
                });
        return data;
    }
}
