package com.bozobaka.bharatadmin.viewmodels;

import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.models.StudyMaterialModel;
import com.bozobaka.bharatadmin.repositories.NotesRepository;
import com.bozobaka.bharatadmin.repositories.StudyMaterialRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class StudyMaterialViewModel extends ViewModel {

    private MutableLiveData<List<StudyMaterialModel>> studyMaterialList;
    private String classId;

    public StudyMaterialViewModel(String classId) {
        this.classId = classId;
    }

    public LiveData<List<StudyMaterialModel>> getStudyMaterial() {
        if (studyMaterialList == null) {
            studyMaterialList = new MutableLiveData<>();
            loadClasses();
        }
        return studyMaterialList;
    }

    private void loadClasses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            StudyMaterialRepository studyMaterialRepository = StudyMaterialRepository.getInstance();
            studyMaterialList = studyMaterialRepository.getNotes(classId);
        }
    }

    public static class StudyMaterialViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        String classId;
        public StudyMaterialViewModelFactory(String classId) {
            this.classId = classId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new StudyMaterialViewModel(classId);
        }
    }
}