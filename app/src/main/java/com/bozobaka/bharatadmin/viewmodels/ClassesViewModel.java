package com.bozobaka.bharatadmin.viewmodels;

import android.app.Application;

import com.bozobaka.bharatadmin.models.ClassModel;
import com.bozobaka.bharatadmin.repositories.ClassesRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ClassesViewModel extends ViewModel {

    private MutableLiveData<List<ClassModel>> classList;

    public ClassesViewModel() {
    }

    public LiveData<List<ClassModel>> getClasses() {
        if (classList == null) {
            classList = new MutableLiveData<>();
            loadClasses();
        }
        return classList;
    }

    private void loadClasses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            ClassesRepository classesRepository = ClassesRepository.getInstance();
            classList = classesRepository.getClasses(user.getUid());
        }
    }

    public static class ClassViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        public ClassViewModelFactory() {
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new ClassesViewModel();
        }
    }
}