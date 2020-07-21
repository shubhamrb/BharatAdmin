package com.bozobaka.bharatadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberModel implements Parcelable {
    private String userName;
    private String userMobNo;
    private String userPic;
    private String userType;

    public MemberModel() {
    }

    public MemberModel(String userName, String userMobNo, String userPic, String userType) {
        this.userName = userName;
        this.userMobNo = userMobNo;
        this.userPic = userPic;
        this.userType = userType;
    }

    protected MemberModel(Parcel in) {
        userName = in.readString();
        userMobNo = in.readString();
        userPic = in.readString();
        userType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userMobNo);
        dest.writeString(userPic);
        dest.writeString(userType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel in) {
            return new MemberModel(in);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobNo() {
        return userMobNo;
    }

    public void setUserMobNo(String userMobNo) {
        this.userMobNo = userMobNo;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
