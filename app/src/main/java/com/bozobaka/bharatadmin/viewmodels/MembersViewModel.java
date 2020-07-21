package com.bozobaka.bharatadmin.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.repositories.ClassMembersRepository;
import com.bozobaka.bharatadmin.ui.classmember.ClassMemberDataSourceFactory;

import java.util.List;

public class MembersViewModel extends ViewModel {

    private MutableLiveData<List<MemberModel>> classMemberList;
    private String classId;
//    private ClassMemberDataSourceFactory dataSourceFactory;
//    private PagedList.Config config;

    private MembersViewModel(String classId) {
        this.classId = classId;
//        dataSourceFactory = new ClassMemberDataSourceFactory(classId);
//        config = (new PagedList.Config.Builder())
//                .setEnablePlaceholders(true)
//                .setPrefetchDistance(40)
//                .setPageSize(20).build();
    }

    public LiveData<List<MemberModel>> getClasses() {
        if (classMemberList == null) {
            classMemberList = new MutableLiveData<>();
            loadClassMembers();
        }
        return classMemberList;
    }

//    public LiveData<PagedList<MemberModel>> getClasses() {
//        return new LivePagedListBuilder<>(
//                dataSourceFactory, config)
//                .build();
//    }

    private void loadClassMembers() {
        ClassMembersRepository classMembersRepository = ClassMembersRepository.getInstance();
        classMemberList = classMembersRepository.getClassMembers(classId);
    }

    public static class ClassViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        String classId;
        public ClassViewModelFactory(String classId) {
            this.classId = classId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> viewModel) {
            return (T) new MembersViewModel(classId);
        }
    }
}