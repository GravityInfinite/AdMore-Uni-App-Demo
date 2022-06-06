package com.plutus.common.admore.bridge.uni.helpers;


import static com.plutus.common.admore.bridge.uni.utils.Helper.getAdSourceJsonObjSafely;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.plutus.common.admore.api.AMAdStatusInfo;
import com.plutus.common.admore.api.AMBanner;
import com.plutus.common.admore.api.AMNativeAdView;
import com.plutus.common.admore.beans.AdSource;
import com.plutus.common.admore.bridge.uni.utils.LogWrapper;
import com.plutus.common.admore.bridge.uni.utils.MsgTools;
import com.plutus.common.admore.bridge.uni.utils.UniCallbackHelper;
import com.plutus.common.admore.listener.AMBannerListener;
import com.plutus.common.admore.listener.ImpressionEventListener;
import com.plutus.common.core.utils.Utils;
import com.plutus.common.core.utils.activitycontext.ActivityContext;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class BannerHelper extends BaseHelper {
    private final String TAG = this.getClass().getSimpleName();
    Activity mActivity;
    String mPlacementId;
    AMBanner mAMBanner;
    AMNativeAdView mAMNativeAdView;
    boolean isReady = false;

    public BannerHelper() {
        this.mActivity = ActivityContext.getInstance().getCurrentActivity();
    }

    private void initBanner(String placementId, UniJSCallback callback) {
        mPlacementId = placementId;
        MsgTools.printMsg("initBanner: " + placementId);
        mAMBanner = new AMBanner(mActivity, placementId);
        mAMBanner.setAdListener(new AMBannerListener() {

            @Override
            public void onBannerAdLoaded() {
                MsgTools.printMsg("onBannerAdLoaded: " + mPlacementId);
                isReady = true;
                UniCallbackHelper.callbackToUni(callback, "onBannerAdLoaded", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onBannerRenderFail(int code, String msg) {
                MsgTools.printMsg("onBannerRenderFail: " + mPlacementId + ", " + code + " " + msg);
                UniCallbackHelper.callbackToUni(callback, "onBannerRenderFail", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", "code is " + code + " msg is " + msg));
            }

            @Override
            public void onBannerAdClicked(AdSource adSource) {
                MsgTools.printMsg("onBannerAdClicked: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onBannerAdClicked", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onBannerAdShow(AdSource adSource) {
                MsgTools.printMsg("onBannerAdShow: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onBannerAdShow", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)), Pair.create("width", (mAMNativeAdView == null ? 0 : mAMNativeAdView.getWidth())), Pair.create("height", (mAMNativeAdView == null ? 0 : mAMNativeAdView.getHeight())));
            }

            @Override
            public void onRenderSuccess(View view, float width, float height, int adnType) {
                MsgTools.printMsg("onRenderSuccess: " + mPlacementId);
                if (mAMNativeAdView != null) {
                    mAMNativeAdView.renderBannerAdToActivity(mActivity, adnType, view, new ImpressionEventListener() {
                        @Override
                        public void onImpression() {
                            LogWrapper.d(TAG, "on impression");
                        }
                    });
                }
            }

            @Override
            public void onDislikeRemoved() {
                MsgTools.printMsg("onDislikeRemoved: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onDislikeRemoved", Pair.create("placement_id", mPlacementId));
                if (mAMNativeAdView != null) {
                    mAMNativeAdView.removeSelfFromActivity();
                }
            }
        });
        this.mAMNativeAdView = new AMNativeAdView(this.mActivity);
    }

    public void loadBanner(final String placementId, final String settings, UniJSCallback callback) {
        MsgTools.printMsg("loadBanner: " + placementId + ", settings: " + settings);
        Utils.runOnUiThread(() -> {
            if (mAMBanner == null || !TextUtils.equals(mPlacementId, placementId)) {
                initBanner(placementId, callback);
            }

            if (!TextUtils.isEmpty(settings)) {
                try {
                    JSONObject jsonObject = new JSONObject(settings);
                    int width = 0;
                    int height = 0;
                    Map<String, Object> localExtra = new HashMap();
                    MsgTools.printMsg("Banner setLocalExtra >>>  width: " + width + ", height: " + height);
                    fillMapFromJsonObject(localExtra, jsonObject);
                    MsgTools.printMsg("extra is " + localExtra.toString());
                    mAMBanner.setLocalExtra(localExtra);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            mAMBanner.load(mActivity);
        });
    }

    public void show(final String showConfig, UniJSCallback callback) {
        MsgTools.printMsg("Banner show: " + mPlacementId + ", config: " + showConfig);
        if (mAMBanner == null) {
            MsgTools.printMsg("Banner show error: mAMBanner = null");
        } else {
            Utils.runOnUiThread(() -> {
                if (mAMBanner != null) {
                    isReady = false;
                    mAMBanner.show(mActivity);
                } else {
                    UniCallbackHelper.callbackToUni(callback, "onBannerRenderFail", Pair.create("placement_id", mPlacementId), Pair.create("code", -1), Pair.create("msg", "showBanner error, BannerAd = null"));
                }
            });
        }
    }

    public boolean isAdReady() {
        MsgTools.printMsg("video isAdReady: " + mPlacementId);
        try {
            if (mAMBanner != null) {
                boolean isAdReady = mAMBanner.isAdReady();
                MsgTools.printMsg("video isAdReady: " + mPlacementId + ", " + isAdReady);
                return isAdReady;
            }

            MsgTools.printMsg("isAdReady error, you must call loadBanner first " + mPlacementId);
            MsgTools.printMsg("isAdReady, end: " + mPlacementId);
        } catch (Throwable e) {
            MsgTools.printMsg("isAdReady, Throwable: " + e.getMessage());
            return isReady;
        }

        return isReady;
    }

    public boolean isAdExpired() {
        MsgTools.printMsg("isAdExpired: " + mPlacementId);
        try {
            if (mAMBanner != null) {
                boolean isAdExpired = mAMBanner.isAdExpired();
                MsgTools.printMsg("isAdExpired: " + mPlacementId + ", " + isAdExpired);
                return isAdExpired;
            } else {
                return true;
            }
        } catch (Throwable e) {
            MsgTools.printMsg("isAdExpired, Throwable: " + e.getMessage());
            return true;
        }
    }

    public void remove() {
        MsgTools.printMsg("Banner remove: " + this.mPlacementId);
        if (mAMNativeAdView != null) {
            mAMNativeAdView.removeSelfFromActivity();
        }
    }

    public String checkAdStatus() {
        MsgTools.printMsg("checkAdStatus: " + mPlacementId);
        if (mAMBanner != null) {
            AMAdStatusInfo atAdStatusInfo = mAMBanner.checkAdStatus();
            boolean loading = atAdStatusInfo.isLoading();
            boolean ready = atAdStatusInfo.isReady();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("isLoading", loading);
                jsonObject.put("isReady", ready);
                return jsonObject.toString();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
