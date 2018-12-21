package com.zhujw.projects;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * author: zhujw
 * date: on 2018-12-20.
 */

public class ProjectsApplication extends Application {

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
