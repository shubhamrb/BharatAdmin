package com.bozobaka.bharatadmin.models;

import java.util.ArrayList;
import java.util.List;

public class AttendanceListModel {
    private String date;
    private String liveClassId;
    private String liveClassName;
    private List<String> studentNames = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();

    public AttendanceListModel() {
    }

    public AttendanceListModel(String date, String liveClassId, String liveClassName,
                               List<String> studentNames, List<String> studentIds) {
        this.date = date;
        this.liveClassId = liveClassId;
        this.liveClassName = liveClassName;
        this.studentNames = studentNames;
        this.studentIds = studentIds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLiveClassName() {
        return liveClassName;
    }

    public void setLiveClassName(String liveClassName) {
        this.liveClassName = liveClassName;
    }

    public String getLiveClassId() {
        return liveClassId;
    }

    public void setLiveClassId(String liveClassId) {
        this.liveClassId = liveClassId;
    }

    public List<String> getStudentNames() {
        return studentNames;
    }

    public void setStudentNames(List<String> studentNames) {
        this.studentNames = studentNames;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}
