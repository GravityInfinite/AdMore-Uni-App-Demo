package com.plutus.common.admore.uni.demo;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.plutus.common.core.GravitySDK;
import com.plutus.common.core.utils.SystemUtil;
import com.plutus.common.core.utils.activitycontext.ActivityContext;
import com.plutus.qmkfd.android.BuildConfig;

import java.util.concurrent.TimeUnit;

import io.dcloud.application.DCloudApplication;

public class PlutusApp extends DCloudApplication implements LifecycleObserver {

    private static final String TAG = "App";
    private long mAppStopTimeMillis = 0L;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        if (SystemUtil.isInMainProcess(base)) {
            GravitySDK.onAttachBaseContext(base, true, BuildConfig.BUILD_TYPE, "test");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (SystemUtil.isInMainProcess(this)) {
            GravitySDK.onCreate(this);
        }
        // 注册监听接口
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onForeground() {
        // 获取当前的activity
        Activity activity = ActivityContext.getInstance().getCurrentActivity();
        Log.d(TAG, "应用回到前台 " + activity);
        // 在这里做策略，控制广告展示的频率，比如这里的逻辑是：退出到后台之后，超过20秒就要展示开屏广告
        if ((SystemClock.elapsedRealtime() - mAppStopTimeMillis > TimeUnit.SECONDS.toMillis(20)) && !(activity instanceof SplashActivity)) {
            SplashActivity.showAdActivity(activity);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onBackground() {
        Activity activity = ActivityContext.getInstance().getCurrentActivity();
        Log.d(TAG, "应用退到后台 " + activity);
        mAppStopTimeMillis = SystemClock.elapsedRealtime();
        SplashHelper.preloadNextSplash();
    }

}