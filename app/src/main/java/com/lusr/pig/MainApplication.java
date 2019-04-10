package com.lusr.pig;


import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    /**
     * 全局的上下文.
     */
    private static Context mContext;
    public static Application	mApplication;
    public static boolean login = false;

    public static String getUserNmae() {
        return userNmae;
    }

    public static void setUserNmae(String userNmae) {
        MainApplication.userNmae = userNmae;
    }

    public static String userNmae;

    @Override
    public void onCreate() {

        super.onCreate();
        mApplication = this;
        mContext = getApplicationContext();
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean logins) {
        login = logins;
    }

    /**
     * 获取Context.
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}

