package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bozobaka.bharatadmin.models.MarkAttendanceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRepository {
    private static AttendanceRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<MarkAttendanceModel> attendanceModels = new ArrayList<>();
//    private HashMap<String, MarkAttendanceModel> attendanceId = new HashMap<>();

    public static AttendanceRepository getInstance() {
        if (instance == null) {
            instance = new AttendanceRepository();
        }
        return instance;
    }

    public MutableLiveData<List<MarkAttendanceModel>> getAttendanceList(String classId, String date) {
        final MutableLiveData<List<MarkAttendanceModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .document(classId)
                .collection("attendance")
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            attendanceModels.clear();
                            if (task.getResult() != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    MarkAttendanceModel markAttendanceModel = document.toObject(MarkAttendanceModel.class);
                                    attendanceModels.add(markAttendanceModel);
                                }
                                data.setValue(attendanceModels);
                            }
                        } else {
                            if (task.getException() != null && task.getException().getMessage() != null)
                                Log.d("Error Fetching data: ", task.getException().getMessage());
                        }
                    }
                });
        return data;
    }
}
