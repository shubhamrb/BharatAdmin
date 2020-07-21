package com.bozobaka.bharatadmin.viewmodels;

import com.bozobaka.bharatadmin.models.LiveModel;
import com.bozobaka.bharatadmin.repositories.LiveClassesRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LiveClassesViewModel extends ViewModel {

    private MutableLiveData<List<LiveModel>> liveClassList;
    private String classId;


    public LiveClassesViewModel(String classId) {
        this.classId = classId;
    }

    public LiveData<List<LiveModel>> getLiveClasses() {
        if (liveClassList == null) {
            liveClassList = new MutableLiveData<>();
            loadLiveClasses();
        }
        return liveClassList;
    }

    private void loadLiveClasses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            LiveClassesRepository liveClassesRepository = LiveClassesRepository.getInstance();
            liveClassList = liveClassesRepository.getLiveClasses(classId);
        }
    }

    public static class LiveClassViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        String classId;

        public LiveClassViewModelFactory(String classId) {
            this.classId = classId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new LiveClassesViewModel(classId);
        }
    }
}