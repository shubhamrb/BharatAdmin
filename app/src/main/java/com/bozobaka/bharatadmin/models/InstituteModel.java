package com.bozobaka.bharatadmin.models;

import java.util.ArrayList;
import java.util.List;

public class InstituteModel {

    private String id;
    private String instituteName;
    private List<String> teachers = new ArrayList<>();
    private List<String> students = new ArrayList<>();

    public InstituteModel() {
    }

    public InstituteModel(String id, String instituteName, List<String> teachers, List<String> students) {
        this.id = id;
        this.instituteName = instituteName;
        this.teachers = teachers;
        this.students = students;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
