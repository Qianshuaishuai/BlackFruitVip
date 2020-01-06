package com.heiguo.blackfruitvip.response;

import com.heiguo.blackfruitvip.bean.MainBean;

import java.util.List;

public class OrderBuildResponse {
    private String F_responseMsg;
    private int F_responseNo;
    private long F_data;

    public String getF_responseMsg() {
        return F_responseMsg;
    }

    public void setF_responseMsg(String f_responseMsg) {
        F_responseMsg = f_responseMsg;
    }

    public int getF_responseNo() {
        return F_responseNo;
    }

    public void setF_responseNo(int f_responseNo) {
        F_responseNo = f_responseNo;
    }

    public long getF_data() {
        return F_data;
    }

    public void setF_data(long f_data) {
        F_data = f_data;
    }
}
