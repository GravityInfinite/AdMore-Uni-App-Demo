package com.plutus.common.admore.bridge.uni;

import com.plutus.common.admore.bridge.uni.helpers.InterstitialHelper;

import java.util.HashMap;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AdMoreInterstitialModule extends UniModule {
    private static final String TAG = "AdMoreInterModule";
    private final HashMap<String, InterstitialHelper> sHelperMap = new HashMap<>();

    @UniJSMethod(uiThread = true)
    public void load(String placementId, String settings, UniJSCallback callback) {
        InterstitialHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.loadInterstitial(placementId, settings, callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void show(String placementId, UniJSCallback callback) {
        InterstitialHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.showInterstitial(callback);
        }
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdReady(String placementId) {
        InterstitialHelper helper = getHelper(placementId);
        return helper != null && helper.isAdReady();
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdExpired(String placementId) {
        InterstitialHelper helper = getHelper(placementId);
        return helper != null && helper.isAdExpired();
    }

    @UniJSMethod(uiThread = false)
    public String checkAdStatus(String placementId) {
        InterstitialHelper helper = getHelper(placementId);
        return helper != null ? helper.checkAdStatus() : "";
    }

    private InterstitialHelper getHelper(String placementId) {
        InterstitialHelper helper;
        if (!sHelperMap.containsKey(placementId)) {
            helper = new InterstitialHelper();
            sHelperMap.put(placementId, helper);
        } else {
            helper = sHelperMap.get(placementId);
        }

        return helper;
    }

}
