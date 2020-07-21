package com.bozobaka.bharatadmin.models;

public class CheckUpdateModel {

    private String appLatestVersion;
    private String acceptPayment;

    public CheckUpdateModel() {
    }

    public CheckUpdateModel(String appLatestVersion, String acceptPayment) {
        this.appLatestVersion = appLatestVersion;
        this.acceptPayment = acceptPayment;
    }

    public String getAppLatestVersion() {
        return appLatestVersion;
    }

    public void setAppLatestVersion(String appLatestVersion) {
        this.appLatestVersion = appLatestVersion;
    }

    public String getAcceptPayment() {
        return acceptPayment;
    }

    public void setAcceptPayment(String acceptPayment) {
        this.acceptPayment = acceptPayment;
    }
}
