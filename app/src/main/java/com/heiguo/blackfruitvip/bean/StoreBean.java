package com.heiguo.blackfruitvip.bean;

public class StoreBean {
    private long id;
    private String name;
    private String image;
    private String tel;
    private double latitude;
    private double longitude;
    private String address;
    private String announ;
    private double maxDistance;
    private String time1Start;
    private String time1End;

    private int service1;
    private int service2;
    private int service3;

    public int getService1() {
        return service1;
    }

    public void setService1(int service1) {
        this.service1 = service1;
    }

    public int getService2() {
        return service2;
    }

    public void setService2(int service2) {
        this.service2 = service2;
    }

    public int getService3() {
        return service3;
    }

    public void setService3(int service3) {
        this.service3 = service3;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAnnoun() {
        return announ;
    }

    public void setAnnoun(String announ) {
        this.announ = announ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getTime1Start() {
        return time1Start;
    }

    public void setTime1Start(String time1Start) {
        this.time1Start = time1Start;
    }

    public String getTime1End() {
        return time1End;
    }

    public void setTime1End(String time1End) {
        this.time1End = time1End;
    }
}
