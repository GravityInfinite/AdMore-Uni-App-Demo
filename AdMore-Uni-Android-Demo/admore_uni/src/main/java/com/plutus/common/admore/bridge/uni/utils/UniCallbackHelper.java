package com.plutus.common.admore.bridge.uni.utils;

import android.util.Log;
import android.util.Pair;

import com.alibaba.fastjson.JSONObject;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class UniCallbackHelper {
    private static final String TAG = "UniCallbackHelper";

    @SafeVarargs
    public static void callbackToUni(UniJSCallback callback, String functionName, Pair<String, Object>... pairs) {
        if (callback != null) {
            JSONObject callbackData = new JSONObject();
            callbackData.put("functionName", functionName);
            JSONObject paramsObj = new JSONObject();
            for (Pair<String, Object> entry : pairs) {
                paramsObj.put(entry.first, entry.second);
            }
            callbackData.put("params", paramsObj);
            Log.d(TAG, "callback to uni " + callbackData.toString());
            callback.invokeAndKeepAlive(callbackData);
        }
    }
}
