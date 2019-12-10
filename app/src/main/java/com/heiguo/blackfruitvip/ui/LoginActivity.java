package com.heiguo.blackfruitvip.ui;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.phone)
    private EditText phoneEditText;

    @ViewInject(R.id.password)
    private EditText passwordEditText;

    @Event(R.id.login)
    private void login(View view){

    }

    @Event(R.id.forget)
    private void forget(View view){

    }

    @Event(R.id.register)
    private void register(View view){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
