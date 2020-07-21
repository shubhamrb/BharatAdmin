package com.bozobaka.bharatadmin.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;


import com.bozobaka.bharatadmin.utils.AppUtil;

import java.util.Locale;

public class LocaleManager {

    public static Context setLocale(Context context) {
        return setNewLocale(context, AppUtil.getLocaleString(context));
    }

    public static Context setNewLocale(Context context, String str) {
        return updateResources(context, str);
    }

    private static Context updateResources(Context context, String str) {
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        if (Build.VERSION.SDK_INT >= 19) {
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        }
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

}
