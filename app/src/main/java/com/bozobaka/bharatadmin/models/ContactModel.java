package com.bozobaka.bharatadmin.models;

public class ContactModel {
    private String name;
    private String number;
    private String imgUrl;
    private boolean isSelected;

    public ContactModel() {
    }

    public ContactModel(String name, String number, String imgUrl, boolean isSelected) {
        this.name = name;
        this.number = number;
        this.imgUrl = imgUrl;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
