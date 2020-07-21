package com.bozobaka.bharatadmin.viewmodels;

import android.app.Application;

import com.bozobaka.bharatadmin.models.UserModel;
import com.bozobaka.bharatadmin.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModel extends ViewModel {
    private Application application;
    private MutableLiveData<UserModel> userData;

    public UserViewModel(Application application) {
        this.application = application;
    }

    public LiveData<UserModel> getUserData() {
        if (userData == null) {
            userData = new MutableLiveData<>();
            loadUserData();
        }
        return userData;
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String document = user.getPhoneNumber();

        UserRepository userDataRepository = UserRepository.getInstance();
        userData = userDataRepository.getUserData(document);
    }

    public static class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        private Application application;

        public UserViewModelFactory(Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new UserViewModel(application);
        }
    }
}
