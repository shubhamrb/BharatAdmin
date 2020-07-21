package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import com.bozobaka.bharatadmin.models.NoteModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class NotesRepository {
    private static NotesRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<NoteModel> noteModels = new ArrayList<>();

    public static NotesRepository getInstance() {
        if (instance == null) {
            instance = new NotesRepository();
        }
        return instance;
    }

    public MutableLiveData<List<NoteModel>> getNotes(String classId) {
        final MutableLiveData<List<NoteModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .document(classId)
                .collection("notes")
                .whereEqualTo("deleted", false)
                //.orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            noteModels.clear();
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    NoteModel noteModel = document.toObject(NoteModel.class);
                                    noteModels.add(noteModel);
                                }
                                Collections.sort(noteModels, (n1, n2) -> n2.getDate().compareTo(n1.getDate()));
                                data.setValue(noteModels);
                            }
                        } else {
                            Log.d("Error Fetching notes: ", e.getMessage());
                        }

                    }
                });
        return data;
    }
}