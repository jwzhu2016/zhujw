package com.zhujw.projects.msghandle;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * author: zhujw
 * date: on 2018-12-21.
 */

public class HandleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }
}
