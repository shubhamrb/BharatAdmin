package com.bozobaka.bharatadmin.repositories;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.bozobaka.bharatadmin.models.MemberModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ClassMembersRepositoryOptimized {

    private static ClassMembersRepositoryOptimized instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ClassMembersRepositoryOptimized getInstance() {
        if (instance == null) {
            instance = new ClassMembersRepositoryOptimized();
        }
        return instance;
    }

    public void getClassMembers(String classId, String memberId, final int size,
                                @NonNull final ItemKeyedDataSource.LoadCallback<MemberModel> callback) {
        if (memberId != null) {
            List<MemberModel> memberModels = new ArrayList<>();
            db.collection("classes")
                    .document(classId)
                    .collection("classMembers")
                    .orderBy("userMobNo")
                    .startAfter(memberId)
                    .limit(size)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            memberModels.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                MemberModel memberModel = document.toObject(MemberModel.class);
                                memberModels.add(memberModel);
                            }

                            if (memberModels.size() == 0) {
                                return;
                            }
                            if (callback instanceof ItemKeyedDataSource.LoadInitialCallback) {
                                //initial load
                                ((ItemKeyedDataSource.LoadInitialCallback) callback)
                                        .onResult(memberModels, 0, memberModels.size());
                            } else {
                                //next pages load
                                callback.onResult(memberModels);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        } else {
            List<MemberModel> memberModels = new ArrayList<>();
            db.collection("classes")
                    .document(classId)
                    .collection("classMembers")
                    .orderBy("userMobNo")
                    .limit(size)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            memberModels.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                MemberModel memberModel = document.toObject(MemberModel.class);
                                memberModels.add(memberModel);
                            }

                            if (memberModels.size() == 0) {
                                return;
                            }
                            if (callback instanceof ItemKeyedDataSource.LoadInitialCallback) {
                                //initial load
                                ((ItemKeyedDataSource.LoadInitialCallback) callback)
                                        .onResult(memberModels, 0, memberModels.size());
                            } else {
                                //next pages load
                                callback.onResult(memberModels);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }

    }


}
