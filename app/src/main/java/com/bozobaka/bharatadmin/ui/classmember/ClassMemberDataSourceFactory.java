package com.bozobaka.bharatadmin.ui.classmember;

import androidx.paging.DataSource;

import com.bozobaka.bharatadmin.models.MemberModel;


public class ClassMemberDataSourceFactory extends DataSource.Factory<String, MemberModel>{

    private String classId;

    public ClassMemberDataSourceFactory(String classId) {
        this.classId = classId;
    }

    @Override
    public DataSource<String, MemberModel> create() {
        return new ClassMemberDataSource(classId);
    }
}
