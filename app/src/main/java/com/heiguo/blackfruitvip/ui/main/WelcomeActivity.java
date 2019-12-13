package com.heiguo.blackfruitvip.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.heiguo.blackfruitvip.R;
import com.heiguo.blackfruitvip.base.BaseActivity;
import com.heiguo.blackfruitvip.ui.user.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;


@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    private int skipTimeCount = 3;
    private Timer timer;
    private TimerTask timerTask;

    @ViewInject(R.id.skip)
    private Button skipButton;

    @Event(R.id.skip)
    private void skip(View view) {
        skip();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSkipTime();
    }

    private void initSkipTime() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        skipButton.setText(skipTimeCount + " " + "跳过");
                        skipTimeCount--;
                        if (skipTimeCount == -1) {
                            timer.cancel();
                            skip();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void skip() {
        Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(newIntent);
        finish();
    }
}
