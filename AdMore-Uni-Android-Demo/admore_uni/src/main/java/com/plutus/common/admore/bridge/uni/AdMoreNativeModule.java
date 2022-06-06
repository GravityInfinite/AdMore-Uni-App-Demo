package com.plutus.common.admore.bridge.uni;

import com.plutus.common.admore.bridge.uni.helpers.NativeHelper;
import com.plutus.common.admore.bridge.uni.helpers.NativeHelper;

import java.util.HashMap;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AdMoreNativeModule extends UniModule {
    private static final String TAG = "AdMoreNativeModule";
    private final HashMap<String, NativeHelper> sHelperMap = new HashMap<>();

    @UniJSMethod(uiThread = true)
    public void load(String placementId, String settings, UniJSCallback callback) {
        NativeHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.loadNative(placementId, settings, callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void show(String placementId, UniJSCallback callback) {
        NativeHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.show("", callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void remove(String placementId) {
        NativeHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.remove();
        }
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdReady(String placementId) {
        NativeHelper helper = getHelper(placementId);
        return helper != null && helper.isAdReady();
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdExpired(String placementId) {
        NativeHelper helper = getHelper(placementId);
        return helper != null && helper.isAdExpired();
    }

    @UniJSMethod(uiThread = false)
    public String checkAdStatus(String placementId) {
        NativeHelper helper = getHelper(placementId);
        return helper != null ? helper.checkAdStatus() : "";
    }

    private NativeHelper getHelper(String placementId) {
        NativeHelper helper;
        if (!sHelperMap.containsKey(placementId)) {
            helper = new NativeHelper();
            sHelperMap.put(placementId, helper);
        } else {
            helper = sHelperMap.get(placementId);
        }

        return helper;
    }

}
