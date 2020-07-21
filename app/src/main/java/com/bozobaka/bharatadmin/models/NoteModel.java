package com.bozobaka.bharatadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteModel implements Parcelable {
    private String id;
    private String noteName;
    private String noteText;
    private String createdBy;
    private String createdAt;
    private Boolean deleted = false;
    private Date date;


    public NoteModel() {
    }


    protected NoteModel(Parcel in) {
        id = in.readString();
        noteName = in.readString();
        noteText = in.readString();
        createdBy = in.readString();
        createdAt = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<NoteModel> CREATOR = new Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel in) {
            return new NoteModel(in);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(noteName);
        parcel.writeString(noteText);
        parcel.writeString(createdBy);
        parcel.writeString(createdAt);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }


}
