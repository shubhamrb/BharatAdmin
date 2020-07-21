package com.bozobaka.bharatadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class StudyMaterialModel implements Parcelable {
    private String id;
    private String studyMaterialName;
    private String studyMaterialType;
    private String pdfUri;
    private String createdBy;
    private String createdAt;
    private Boolean deleted = false;


    public StudyMaterialModel() {
    }


    protected StudyMaterialModel(Parcel in) {
        id = in.readString();
        studyMaterialName = in.readString();
        studyMaterialType = in.readString();
        createdBy = in.readString();
        createdAt = in.readString();
        pdfUri=in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<StudyMaterialModel> CREATOR = new Creator<StudyMaterialModel>() {
        @Override
        public StudyMaterialModel createFromParcel(Parcel in) {
            return new StudyMaterialModel(in);
        }

        @Override
        public StudyMaterialModel[] newArray(int size) {
            return new StudyMaterialModel[size];
        }
    };


    public Boolean getDeleted() {
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

    public String getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(String pdfUri) {
        this.pdfUri = pdfUri;
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

    public String getStudyMaterialName() {
        return studyMaterialName;
    }

    public void setStudyMaterialName(String studyMaterialName) {
        this.studyMaterialName = studyMaterialName;
    }

    public String getStudyMaterialType() {
        return studyMaterialType;
    }

    public void setStudyMaterialType(String studyMaterialType) {
        this.studyMaterialType = studyMaterialType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(studyMaterialName);
        parcel.writeString(studyMaterialType);
        parcel.writeString(createdBy);
        parcel.writeString(createdAt);
        parcel.writeString(pdfUri);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }


}
