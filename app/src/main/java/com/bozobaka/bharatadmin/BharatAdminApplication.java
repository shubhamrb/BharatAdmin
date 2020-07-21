package com.bozobaka.bharatadmin;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.bozobaka.bharatadmin.common.LocaleManager;
import com.bozobaka.bharatadmin.repositories.FirebaseRepository;

public class BharatAdminApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context2) {
        super.attachBaseContext(LocaleManager.setLocale(context2));
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        LocaleManager.setLocale(this);
    }
}
