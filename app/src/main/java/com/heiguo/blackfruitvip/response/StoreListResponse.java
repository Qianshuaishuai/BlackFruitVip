package com.heiguo.blackfruitvip.response;

import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.bean.StoreBean;

import java.util.List;

public class StoreListResponse {
    private String F_responseMsg;
    private int F_responseNo;
    private List<StoreBean> F_data;

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

    public List<StoreBean> getF_data() {
        return F_data;
    }

    public void setF_data(List<StoreBean> f_data) {
        F_data = f_data;
    }
}
