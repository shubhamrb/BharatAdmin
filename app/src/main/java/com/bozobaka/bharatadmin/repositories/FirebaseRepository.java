package com.bozobaka.bharatadmin.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseRepository {
    private static FirebaseFirestore firestore;
    private static FirebaseAuth firebaseAuth;

    public static FirebaseFirestore getFirestoreInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
            return firestore;
        }
        return firestore;
    }

    public static FirebaseAuth getFirebaseAuthInstance() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            return firebaseAuth;
        }
        return firebaseAuth;
    }
}
