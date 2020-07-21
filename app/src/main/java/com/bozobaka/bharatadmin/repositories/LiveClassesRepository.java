package com.bozobaka.bharatadmin.repositories;

import android.util.Log;

import com.bozobaka.bharatadmin.models.LiveModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class LiveClassesRepository {
    private static LiveClassesRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<LiveModel> liveModels = new ArrayList<>();

    public static LiveClassesRepository getInstance() {
        if (instance == null) {
            instance = new LiveClassesRepository();
        }
        return instance;
    }

    public MutableLiveData<List<LiveModel>> getLiveClasses(String classId) {
        final MutableLiveData<List<LiveModel>> data = new MutableLiveData<>();
        db.collection("classes")
                .document(classId)
                .collection("liveClasses")
                .whereEqualTo("deleted", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e == null) {
                            liveModels.clear();
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    LiveModel liveModel = document.toObject(LiveModel.class);
                                    liveModels.add(liveModel);
                                }

                                data.setValue(sortLiveClasses(liveModels));
                            }
                        } else {
                            Log.d("Error Fetch liveclass: ", Objects.requireNonNull(e.getMessage()));
                        }

                    }
                });
        return data;
    }

    public List<LiveModel> sortLiveClasses(List<LiveModel> liveModels) {

        Collections.sort(liveModels, new Comparator<LiveModel>() {
            @Override
            public int compare(LiveModel l1, LiveModel l2) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_WEEK) - 1;
                boolean exist_l1 = l1.getScheduleDay().contains(day);
                boolean exist_l2 = l2.getScheduleDay().contains(day);
                if (exist_l1 && exist_l2) {
                    return l1.getScheduleRealTime().compareTo(l2.getScheduleRealTime());
                } else if (exist_l1) {
                    return -1;
                } else if (exist_l2) {
                    return 1;
                } else {
                    int i;
                    if (day == 6) {
                        i = 0;
                    } else {
                        i = day + 1;
                    }
                    while (i != day) {
                        if (l1.getScheduleDay().contains(i) && l2.getScheduleDay().contains(i)) {
                            return l1.getScheduleRealTime().compareTo(l2.getScheduleRealTime());
                        }
                        if (l1.getScheduleDay().contains(i)) {
                            return -1;
                        } else if (l2.getScheduleDay().contains(i)) {
                            return 1;
                        }
                        i++;
                        if (i == 7) {
                            i = 0;
                        }
                    }
                }
                return 0;
            }
        });


        return liveModels;
    }
}
