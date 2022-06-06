package com.plutus.common.admore.bridge.uni;

import com.plutus.common.admore.bridge.uni.helpers.BannerHelper;

import java.util.HashMap;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AdMoreBannerModule extends UniModule {
    private static final String TAG = "AdMoreBannerModule";
    private final HashMap<String, BannerHelper> sHelperMap = new HashMap<>();

    @UniJSMethod(uiThread = true)
    public void load(String placementId, String settings, UniJSCallback callback) {
        BannerHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.loadBanner(placementId, settings, callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void show(String placementId, UniJSCallback callback) {
        BannerHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.show("", callback);
        }
    }

    @UniJSMethod(uiThread = true)
    public void remove(String placementId) {
        BannerHelper helper = getHelper(placementId);
        if (helper != null) {
            helper.remove();
        }
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdReady(String placementId) {
        BannerHelper helper = getHelper(placementId);
        return helper != null && helper.isAdReady();
    }

    @UniJSMethod(uiThread = false)
    public boolean isAdExpired(String placementId) {
        BannerHelper helper = getHelper(placementId);
        return helper != null && helper.isAdExpired();
    }

    @UniJSMethod(uiThread = false)
    public String checkAdStatus(String placementId) {
        BannerHelper helper = getHelper(placementId);
        return helper != null ? helper.checkAdStatus() : "";
    }

    private BannerHelper getHelper(String placementId) {
        BannerHelper helper;
        if (!sHelperMap.containsKey(placementId)) {
            helper = new BannerHelper();
            sHelperMap.put(placementId, helper);
        } else {
            helper = sHelperMap.get(placementId);
        }

        return helper;
    }

}
