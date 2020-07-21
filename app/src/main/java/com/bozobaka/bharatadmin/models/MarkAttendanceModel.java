package com.bozobaka.bharatadmin.models;

public class MarkAttendanceModel {
    private String id;
    private String studentName;
    private String studentId;
    private String attendedUnixTime;
    private String date;
    private String liveClassName;
    private String liveClassId;
    private String teacherName;

    public MarkAttendanceModel() {
    }


    public MarkAttendanceModel(String id, String studentName, String studentId, String attendedUnixTime,
                               String date, String liveClassName, String teacherName, String liveClassId) {
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.attendedUnixTime = attendedUnixTime;
        this.date = date;
        this.liveClassName = liveClassName;
        this.liveClassId = liveClassId;
        this.teacherName = teacherName;
        this.liveClassId = liveClassId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAttendedUnixTime() {
        return attendedUnixTime;
    }

    public void setAttendedUnixTime(String attendedUnixTime) {
        this.attendedUnixTime = attendedUnixTime;
    }

    public String getLiveClassId() {
        return liveClassId;
    }

    public void setLiveClassId(String liveClassId) {
        this.liveClassId = liveClassId;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
