package com.heiguo.blackfruitvip.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.phone)
    private TextView phoneTextView;

    @ViewInject(R.id.password)
    private TextView passwordTextView;

    @ViewInject(R.id.code)
    private TextView codeTextView;

    @ViewInject(R.id.agree)
    private CheckBox agreeCheckBox;

    @ViewInject(R.id.send_code)
    private Button sendCodeButton;

    @Event(R.id.login)
    private void login(View view){

    }

    @Event(R.id.register)
    private void register(View view){

    }

    @Event(R.id.send_code)
    private void sendCode(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
