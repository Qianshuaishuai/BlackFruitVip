package com.heiguo.blackfruitvip.bean;

public class UserBean {
    private String phone;
    private String username;
    private String icon;
    private String address;
    private float balance;
    private float saveCount;
    private int vipDay;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getSaveCount() {
        return saveCount;
    }

    public void setSaveCount(float saveCount) {
        this.saveCount = saveCount;
    }

    public int getVipDay() {
        return vipDay;
    }

    public void setVipDay(int vipDay) {
        this.vipDay = vipDay;
    }
}
