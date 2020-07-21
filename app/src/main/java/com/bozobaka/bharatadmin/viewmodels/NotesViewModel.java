package com.bozobaka.bharatadmin.viewmodels;

import com.bozobaka.bharatadmin.models.NoteModel;
import com.bozobaka.bharatadmin.repositories.NotesRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<List<NoteModel>> noteList;
    private String classId;

    public NotesViewModel(String classId) {
        this.classId = classId;
    }

    public LiveData<List<NoteModel>> getNotes() {
        if (noteList == null) {
            noteList = new MutableLiveData<>();
            loadClasses();
        }
        return noteList;
    }

    private void loadClasses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            NotesRepository notesRepository = NotesRepository.getInstance();
            noteList = notesRepository.getNotes(classId);
        }
    }

    public static class NotesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        String classId;
        public NotesViewModelFactory(String classId) {
            this.classId = classId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new NotesViewModel(classId);
        }
    }
}