package com.heiguo.blackfruitvip.bean;

public class GoodBean {
    private int id;
    private String name;
    private String image;
    private int type;
    private double price;
    private double oPrice;
    private double vPrice;
    private String subtitle;
    private String anoTitle;
    private String anoImg;
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getoPrice() {
        return oPrice;
    }

    public void setoPrice(double oPrice) {
        this.oPrice = oPrice;
    }

    public double getvPrice() {
        return vPrice;
    }

    public void setvPrice(double vPrice) {
        this.vPrice = vPrice;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAnoTitle() {
        return anoTitle;
    }

    public void setAnoTitle(String anoTitle) {
        this.anoTitle = anoTitle;
    }

    public String getAnoImg() {
        return anoImg;
    }

    public void setAnoImg(String anoImg) {
        this.anoImg = anoImg;
    }

}
