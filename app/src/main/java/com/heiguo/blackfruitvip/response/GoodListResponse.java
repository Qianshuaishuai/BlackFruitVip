package com.heiguo.blackfruitvip.response;

import com.heiguo.blackfruitvip.bean.StoreDetailBean;

public class GoodListResponse {
    private String F_responseMsg;
    private int F_responseNo;
    private StoreDetailBean F_data;

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

    public StoreDetailBean getF_data() {
        return F_data;
    }

    public void setF_data(StoreDetailBean f_data) {
        F_data = f_data;
    }
}
