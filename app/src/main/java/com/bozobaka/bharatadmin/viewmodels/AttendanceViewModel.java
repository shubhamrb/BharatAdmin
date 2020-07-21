package com.bozobaka.bharatadmin.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bozobaka.bharatadmin.models.MarkAttendanceModel;
import com.bozobaka.bharatadmin.repositories.AttendanceRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AttendanceViewModel extends ViewModel {

    private LiveData<List<MarkAttendanceModel>> attendanceList;
    private MutableLiveData<String> date = new MutableLiveData<>();
    private String classId;

    private AttendanceViewModel(String classId, String date) {
        this.classId = classId;
//        this.date = date;
    }

    public LiveData<List<MarkAttendanceModel>> getAttendance() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AttendanceRepository attendanceRepository = AttendanceRepository.getInstance();
            attendanceList = Transformations.switchMap(date, id -> attendanceRepository.getAttendanceList(classId, id));
        }
        return attendanceList;
    }

    private void loadAttendance() {

    }

    public void setDateId(String dateId) {
        this.date.setValue(dateId);
    }

    public static class AttendanceViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private String classId;
        private String date;

        public AttendanceViewModelFactory(String classId, String date) {
            this.classId = classId;
            this.date = date;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new AttendanceViewModel(classId, date);
        }
    }
}