package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import com.bozobaka.bharatadmin.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class UserRepository {
    private static UserRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public MutableLiveData<UserModel> getUserData(String mobileNo) {
        final MutableLiveData<UserModel> data = new MutableLiveData<>();
        db.collection("users")
                .document(mobileNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            UserModel userModel = task.getResult().toObject(UserModel.class);
                            if (userModel != null){
                                data.setValue(userModel);
                            }else {
                                data.setValue(null);
                            }
                        }else {
                            Log.d("Error Fetching user: ", task.getException().getMessage());
                            data.setValue(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error Fetching user: ", e.getMessage());
                    }
                });
        return data;
    }

}
