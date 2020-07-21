package com.bozobaka.bharatadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ClassModel implements Parcelable {
    private String id;
    private String className;
    private String nextClassTiming;
    private int numberOfStudents;
    private String createdBy;
    private String createdAt;
    private String category;
    private String subCategory;
    private boolean deleted;
    private List<String> students = new ArrayList<>();
    private List<String> teachers = new ArrayList<>();

    public ClassModel() {
    }

    public ClassModel(String id, String className, String nextClassTiming,
                      int numberOfStudents, String createdBy, String createdAt,
                      String category, String subCategory,
                      boolean deleted, List<String> students, List<String> teachers) {
        this.id = id;
        this.className = className;
        this.nextClassTiming = nextClassTiming;
        this.numberOfStudents = numberOfStudents;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.category = category;
        this.subCategory = subCategory;
        this.deleted = deleted;
        this.students = students;
        this.teachers = teachers;
    }

    protected ClassModel(Parcel in) {
        id = in.readString();
        className = in.readString();
        nextClassTiming = in.readString();
        numberOfStudents = in.readInt();
        createdBy = in.readString();
        createdAt = in.readString();
        category = in.readString();
        subCategory = in.readString();
        deleted = in.readByte() != 0;
    }

    public static final Creator<ClassModel> CREATOR = new Creator<ClassModel>() {
        @Override
        public ClassModel createFromParcel(Parcel in) {
            return new ClassModel(in);
        }

        @Override
        public ClassModel[] newArray(int size) {
            return new ClassModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNextClassTiming() {
        return nextClassTiming;
    }

    public void setNextClassTiming(String nextClassTiming) {
        this.nextClassTiming = nextClassTiming;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(className);
        dest.writeString(nextClassTiming);
        dest.writeInt(numberOfStudents);
        dest.writeString(createdBy);
        dest.writeString(createdAt);
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeByte((byte) (deleted ? 1 : 0));
    }


}
