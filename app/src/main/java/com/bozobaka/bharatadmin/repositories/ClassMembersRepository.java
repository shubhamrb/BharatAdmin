package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.bozobaka.bharatadmin.models.ClassMemberModel;
import com.bozobaka.bharatadmin.models.MemberModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClassMembersRepository {

    private static ClassMembersRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<MemberModel> memberModels = new ArrayList<>();

    public static ClassMembersRepository getInstance() {
        if (instance == null) {
            instance = new ClassMembersRepository();
        }
        return instance;
    }

    public MutableLiveData<List<MemberModel>> getClassMembers(String classId) {
        final MutableLiveData<List<MemberModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .document(classId)
                .collection("classMembers")
                .orderBy("userName")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            if (queryDocumentSnapshots != null) {
                                memberModels.clear();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    MemberModel memberModel = document.toObject(MemberModel.class);
                                    memberModels.add(memberModel);
                                }
                                data.setValue(memberModels);
                            }
                        } else {
                            Log.d("Error Fetch data: ", Objects.requireNonNull(e.getMessage()));
                        }

                    }
                });
        return data;
    }


}
