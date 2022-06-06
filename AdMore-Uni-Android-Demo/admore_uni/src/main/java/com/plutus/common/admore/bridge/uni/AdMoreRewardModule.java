package com.plutus.common.admore.bridge.uni;

import com.plutus.common.admore.bridge.uni.helpers.RewardVideoHelper;

import java.util.HashMap;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AdMoreRewardModule extends UniModule {
    private static final String TAG = "AdMoreRewardModule";
    private final HashMap<String, RewardVideoHelper> sHelperMap = new HashMap<>();

    @UniJSMethod(uiThread = true)
    public void load(String placementId, String settings, UniJSCallback callback) {
        RewardVideoHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.loadRewardedVideo(placementId, settings, callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void show(String placementId, UniJSCallback callback) {
        RewardVideoHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.showVideo(callback);
        }
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdReady(String placementId) {
        RewardVideoHelper helper = getHelper(placementId);
        return helper != null && helper.isAdReady();
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdExpired(String placementId) {
        RewardVideoHelper helper = getHelper(placementId);
        return helper != null && helper.isAdExpired();
    }

    @UniJSMethod(uiThread = false)
    public String checkAdStatus(String placementId) {
        RewardVideoHelper helper = getHelper(placementId);
        return helper != null ? helper.checkAdStatus() : "";
    }

    private RewardVideoHelper getHelper(String placementId) {
        RewardVideoHelper helper;
        if (!sHelperMap.containsKey(placementId)) {
            helper = new RewardVideoHelper();
            sHelperMap.put(placementId, helper);
        } else {
            helper = sHelperMap.get(placementId);
        }

        return helper;
    }
}
