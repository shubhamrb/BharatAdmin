package com.bozobaka.bharatadmin.ui.classmember;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import com.bozobaka.bharatadmin.models.MemberModel;
import com.bozobaka.bharatadmin.repositories.ClassMembersRepositoryOptimized;


public class ClassMemberDataSource extends ItemKeyedDataSource<String, MemberModel> {

    private ClassMembersRepositoryOptimized classMembersRepository;
    private String classId;

    public ClassMemberDataSource(String classId) {
        this.classId = classId;
        classMembersRepository = ClassMembersRepositoryOptimized.getInstance();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params,
                            @NonNull LoadInitialCallback<MemberModel> callback) {

        classMembersRepository.getClassMembers(classId, null, params.requestedLoadSize, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params,
                          @NonNull LoadCallback<MemberModel> callback) {
        classMembersRepository.getClassMembers(classId, params.key, params.requestedLoadSize, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params,
                           @NonNull LoadCallback<MemberModel> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull MemberModel memberModel) {
        return memberModel.getUserMobNo();
    }
}
