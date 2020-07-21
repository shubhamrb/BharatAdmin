package com.bozobaka.bharatadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LiveModel implements Parcelable {
    private String id;
    private String instituteName;
    private String className;
    private String liveClassName;
    private String meetingUrl;
    private String meetingId;
    private String createdBy;
    private String createdAt;
    private String teacherName;
    private String teacherId;
    private String classTime;
    private List<Integer> scheduleDay;
    private String scheduleTime;
    private String scheduleRealTime;
    private Boolean deleted = false;


    public LiveModel() {
    }


    protected LiveModel(Parcel in) {
        id = in.readString();
        instituteName = in.readString();
        className = in.readString();
        liveClassName = in.readString();
        meetingUrl = in.readString();
        meetingId = in.readString();
        createdBy = in.readString();
        createdAt = in.readString();
        teacherName = in.readString();
        teacherId = in.readString();
        classTime = in.readString();
        scheduleTime = in.readString();
        scheduleRealTime = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<LiveModel> CREATOR = new Creator<LiveModel>() {
        @Override
        public LiveModel createFromParcel(Parcel in) {
            return new LiveModel(in);
        }

        @Override
        public LiveModel[] newArray(int size) {
            return new LiveModel[size];
        }
    };

    public String getScheduleRealTime() {
        return scheduleRealTime;
    }

    public void setScheduleRealTime(String scheduleRealTime) {
        this.scheduleRealTime = scheduleRealTime;
    }



    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLiveClassName() {
        return liveClassName;
    }

    public void setLiveClassName(String liveClassName) {
        this.liveClassName = liveClassName;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public List<Integer> getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(List<Integer> scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(instituteName);
        parcel.writeString(className);
        parcel.writeString(liveClassName);
        parcel.writeString(meetingUrl);
        parcel.writeString(meetingId);
        parcel.writeString(createdBy);
        parcel.writeString(createdAt);
        parcel.writeString(teacherName);
        parcel.writeString(teacherId);
        parcel.writeString(classTime);
        parcel.writeString(scheduleTime);
        parcel.writeString(scheduleRealTime);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }


}
