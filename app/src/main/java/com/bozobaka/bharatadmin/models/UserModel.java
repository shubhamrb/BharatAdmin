package com.bozobaka.bharatadmin.models;


public class UserModel {

    private String name;
    private String instituteName;
    private String langPref;
    private String msgToken;
    private String appVer;
    private String loginType;
    private String lastActiveDate;
    private boolean paidCustomer;
    private boolean premiumUser;
    private int amountPaid;
    private int noOfTimesPaid;
    private String paidStartDate;
    private String paidExpiryDate;
    private String referralCode;
    private int noOfReferrals;


    public UserModel() {
    }

    public UserModel(String name, String instituteName, String langPref, String msgToken,
                     String appVer, String loginType, String lastActiveDate,
                     boolean paidCustomer, boolean premiumUser,
                     int amountPaid, int noOfTimesPaid, String paidStartDate,
                     String paidExpiryDate, String referralCode, int noOfReferrals) {
        this.name = name;
        this.instituteName = instituteName;
        this.langPref = langPref;
        this.msgToken = msgToken;
        this.appVer = appVer;
        this.loginType = loginType;
        this.lastActiveDate = lastActiveDate;
        this.paidCustomer = paidCustomer;
        this.premiumUser = premiumUser;
        this.amountPaid = amountPaid;
        this.noOfTimesPaid = noOfTimesPaid;
        this.paidStartDate = paidStartDate;
        this.paidExpiryDate = paidExpiryDate;
        this.referralCode = referralCode;
        this.noOfReferrals = noOfReferrals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getLangPref() {
        return langPref;
    }

    public void setLangPref(String langPref) {
        this.langPref = langPref;
    }

    public String getMsgToken() {
        return msgToken;
    }

    public void setMsgToken(String msgToken) {
        this.msgToken = msgToken;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(String lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public boolean isPaidCustomer() {
        return paidCustomer;
    }

    public void setPaidCustomer(boolean paidCustomer) {
        this.paidCustomer = paidCustomer;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaidExpiryDate() {
        return paidExpiryDate;
    }

    public void setPaidExpiryDate(String paidExpiryDate) {
        this.paidExpiryDate = paidExpiryDate;
    }

    public boolean isPremiumUser() {
        return premiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        this.premiumUser = premiumUser;
    }

    public int getNoOfTimesPaid() {
        return noOfTimesPaid;
    }

    public void setNoOfTimesPaid(int noOfTimesPaid) {
        this.noOfTimesPaid = noOfTimesPaid;
    }

    public String getPaidStartDate() {
        return paidStartDate;
    }

    public void setPaidStartDate(String paidStartDate) {
        this.paidStartDate = paidStartDate;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public int getNoOfReferrals() {
        return noOfReferrals;
    }

    public void setNoOfReferrals(int noOfReferrals) {
        this.noOfReferrals = noOfReferrals;
    }
}
