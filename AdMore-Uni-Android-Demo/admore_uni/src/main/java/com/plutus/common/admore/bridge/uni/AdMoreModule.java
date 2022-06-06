package com.plutus.common.admore.bridge.uni;

import com.alibaba.fastjson.JSONObject;
import com.plutus.common.admore.AMSDK;
import com.plutus.common.admore.bridge.uni.utils.LogWrapper;
import com.plutus.common.admore.bridge.uni.utils.MsgTools;
import com.plutus.common.admore.listener.AMSDKInitListener;
import com.plutus.common.core.utils.ApkUtils;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AdMoreModule extends UniModule {
    private static final String TAG = "AdMoreModule";

    @UniJSMethod(uiThread = true)
    public void initSDK(String appId, String appKey, String userId, final UniJSCallback callback) {
        MsgTools.printMsg("initSDK:" + appId + ":" + appKey + ":" + userId);
        AMSDK.setClientVersion(ApkUtils.get().getVersionCode());
        AMSDK.setChannel(ApkUtils.get().getChannelName());
        AMSDK.setDebugAdnType(-2);
        AMSDK.init(appId, appKey, userId, new AMSDKInitListener() {
            @Override
            public void onSuccess() {
                LogWrapper.d(TAG, "init success");
                if (callback != null) {
                    JSONObject data = new JSONObject();
                    data.put("status", "success");
                    callback.invoke(data);
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogWrapper.d(TAG, "init fail");
                if (callback != null) {
                    JSONObject data = new JSONObject();
                    data.put("status", "failed");
                    callback.invoke(data);
                }
            }
        });
    }

    @UniJSMethod(uiThread = true)
    public static void setLogDebug(boolean isDebug) {
        MsgTools.setLogDebug(isDebug);
        MsgTools.printMsg("setLogDebug:" + isDebug);
        AMSDK.setLogDebug(isDebug);
    }
}
