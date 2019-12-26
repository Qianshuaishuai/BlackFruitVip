package com.heiguo.blackfruitvip.bean;


import com.amap.api.services.core.PoiItem;

public class DistanceBean {
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PoiItem getItem() {
        return item;
    }

    public void setItem(PoiItem item) {
        this.item = item;
    }

    private PoiItem item;
}
