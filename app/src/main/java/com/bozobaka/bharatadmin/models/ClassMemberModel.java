package com.bozobaka.bharatadmin.models;

import java.util.ArrayList;
import java.util.List;

public class ClassMemberModel {
    private List<String> names = new ArrayList<>();
    private List<String> mobileNoList = new ArrayList<>();
    private List<String> userTypes = new ArrayList<>();


    public ClassMemberModel() {
    }

    public ClassMemberModel(List<String> names, List<String> mobileNoList, List<String> userTypes) {
        this.names = names;
        this.mobileNoList = mobileNoList;
        this.userTypes = userTypes;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getMobileNoList() {
        return mobileNoList;
    }

    public void setMobileNoList(List<String> mobileNoList) {
        this.mobileNoList = mobileNoList;
    }

    public List<String> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }
}
