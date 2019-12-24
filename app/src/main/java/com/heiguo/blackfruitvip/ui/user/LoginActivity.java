package com.heiguo.blackfruitvip.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.heiguo.blackfruitvip.BlackFruitVipApplication;
import com.heiguo.blackfruitvip.Constant;
import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.response.CommonResponse;
import com.heiguo.blackfruitvip.ui.home.HomeActivity;
import com.heiguo.blackfruitvip.util.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private Dialog userDialog;

    @ViewInject(R.id.phone)
    private EditText phoneEditText;

    @ViewInject(R.id.password)
    private EditText passwordEditText;

    @Event(R.id.login)
    private void login(View view) {
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
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("登录成功");
                    ((BlackFruitVipApplication) getApplication()).saveLoginPhone(phoneEditText.getText().toString());
                    startHomeActivity();

                } else {
                    userDialog.show();
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
    private void close(View view) {
        finish();
    }

    @Event(R.id.forget)
    private void forget(View view) {
        Intent intent = new Intent(this, ForgetActivity.class);
        intent.putExtra("forget-mode", "0");
        startActivity(intent);
    }

    @Event(R.id.register)
    private void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUserDialog();
        jdugeCanAutoLogin();
    }

    private void startHomeActivity() {
        Intent newIntent = new Intent(this, HomeActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void jdugeCanAutoLogin() {
        String phone = ((BlackFruitVipApplication) getApplication()).getLoginPhone();
        if (phone == "") {
            return;
        } else {
            phoneEditText.setText(phone);

            RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_AUTO);
            params.addQueryStringParameter("phone", phone);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    CommonResponse response = gson.fromJson(result, CommonResponse.class);
                    if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                        T.s("自动登录成功");
                        startHomeActivity();
                    } else {
                        System.out.println(response.getF_responseMsg());
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
    }

    private void initUserDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_user_common, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        userDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText("密码错误请重新输入或找回密码");
        Button leftButton = (Button) view.findViewById(R.id.left);
        Button rightButton = (Button) view.findViewById(R.id.right);

        leftButton.setText("找回密码");
        rightButton.setText("重新输入");

        leftButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                userDialog.cancel();
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });

        rightButton.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                userDialog.cancel();
                passwordEditText.setText("");
            }
        });

        userDialog.setCancelable(false);
    }
}
