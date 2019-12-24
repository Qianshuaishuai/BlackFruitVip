package com.heiguo.blackfruitvip.response;

import com.heiguo.blackfruitvip.bean.MainBean;
import com.heiguo.blackfruitvip.bean.UserBean;

import java.util.List;

public class UserInfoResponse {
    private String F_responseMsg;
    private int F_responseNo;

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

    public UserBean getF_data() {
        return F_data;
    }

    public void setF_data(UserBean f_data) {
        F_data = f_data;
    }

    private UserBean F_data;
}
