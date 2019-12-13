package com.heiguo.blackfruitvip.ui.user;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_forget)
public class ForgetActivity extends BaseActivity {

    private int codeNotClickTime = 0;
    private Timer timer;
    private TimerTask timerTask;

    @ViewInject(R.id.phone)
    private TextView phoneTextView;

    @ViewInject(R.id.password)
    private TextView passwordTextView;

    @ViewInject(R.id.code)
    private TextView codeTextView;

    @ViewInject(R.id.send_code)
    private Button sendCodeButton;

    @Event(R.id.login)
    private void login(View view) {
        finish();
    }

    @Event(R.id.forget)
    private void forget(View view) {
        if (phoneTextView.getText().toString().length() != 11) {
            T.s("手机号码格式不对");
            return;
        }

        if (passwordTextView.getText().toString().length() == 0) {
            T.s("新密码不能为空");
            return;
        }

        if (codeTextView.getText().toString().length() == 0) {
            T.s("请填写验证码");
            return;
        }

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FORGET);
        params.addQueryStringParameter("phone", phoneTextView.getText().toString());
        params.addQueryStringParameter("password", passwordTextView.getText().toString());
        params.addQueryStringParameter("code", codeTextView.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS){
                    T.s("修改成功");
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
    private void close(View view) {
        finish();
    }

    @Event(R.id.send_code)
    private void sendCode(View view) {
        if (phoneTextView.getText().toString().length() != 11) {
            T.s("手机号码格式不对");
            return;
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_CODE);
        params.addQueryStringParameter("phone", phoneTextView.getText().toString());
        params.addQueryStringParameter("typeID", Constant.CODE_REGISTER);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                if (response.getF_responseNo() == Constant.REQUEST_SUCCESS) {
                    T.s("发送成功");
                } else {
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
        sendCodeButton.setClickable(false);
        codeNotClickTime = 60;
        sendCodeButton.setTextColor(Color.GRAY);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendCodeButton.setText(codeNotClickTime + "秒");
                        codeNotClickTime--;
                        if (codeNotClickTime == 0) {
                            timer.cancel();
                            sendCodeButton.setClickable(true);
                            sendCodeButton.setText("发送验证码");
                            sendCodeButton.setTextColor(Color.BLACK);
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
