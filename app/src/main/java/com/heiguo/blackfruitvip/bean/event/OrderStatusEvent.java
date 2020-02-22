package com.heiguo.blackfruitvip.bean.event;

public class OrderStatusEvent {
    private int status;

    public OrderStatusEvent(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
