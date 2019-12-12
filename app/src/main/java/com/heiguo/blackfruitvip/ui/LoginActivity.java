package com.heiguo.blackfruitvip.ui;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.bean.CommonResponse;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.phone)
    private EditText phoneEditText;

    @ViewInject(R.id.password)
    private EditText passwordEditText;

    @Event(R.id.login)
    private void login(View view){
        if (phoneEditText.getText().toString().length() != 11) {
            T.s("手机号码格式不对");
            return;
        }

        if (passwordEditText.getText().toString().length() == 0) {
            T.s("密码不能为空");
            return;
        }

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_LOGIN);
        params.addQueryStringParameter("phone", phoneEditText.getText().toString());
        params.addQueryStringParameter("password", passwordEditText.getText().toString());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS){
                    T.s("登录成功");
                }else {
                    T.s(response.getF_responseMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                T.s("请求出错，请检查网络");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Event(R.id.close)
    private void close(View view){
        finish();
    }

    @Event(R.id.forget)
    private void forget(View view){
        Intent intent=new Intent(this, ForgetActivity.class);
        startActivity(intent);
    }

    @Event(R.id.register)
    private void register(View view){
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
