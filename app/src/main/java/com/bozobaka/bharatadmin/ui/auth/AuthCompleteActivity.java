package com.bozobaka.bharatadmin.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import com.bozobaka.bharatadmin.R;
import com.bozobaka.bharatadmin.ui.base.BaseActivity;
import com.bozobaka.bharatadmin.ui.main.MainActivity;

public class AuthCompleteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(AuthCompleteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        };
        timer.start();
    }
}
