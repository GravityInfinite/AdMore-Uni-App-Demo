package com.plutus.common.admore.bridge.uni.helpers;

import static com.plutus.common.admore.bridge.uni.utils.Helper.getAdSourceJsonObjSafely;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Pair;

import com.plutus.common.admore.api.AMAdStatusInfo;
import com.plutus.common.admore.api.AMInterstitial;
import com.plutus.common.admore.api.AdError;
import com.plutus.common.admore.beans.AdSource;
import com.plutus.common.admore.bridge.uni.utils.CommonUtil;
import com.plutus.common.admore.bridge.uni.utils.LogWrapper;
import com.plutus.common.admore.bridge.uni.utils.MsgTools;
import com.plutus.common.admore.bridge.uni.utils.UniCallbackHelper;
import com.plutus.common.admore.listener.AMInterstitialListener;
import com.plutus.common.core.utils.Utils;
import com.plutus.common.core.utils.activitycontext.ActivityContext;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class InterstitialHelper extends BaseHelper {
    private static final String TAG = InterstitialHelper.class.getSimpleName();
    AMInterstitial mInterstitialAd;
    String mPlacementId;
    Activity mActivity;
    boolean isReady = false;

    public InterstitialHelper() {
        mActivity = ActivityContext.getInstance().getCurrentActivity();
    }

    private void initInterstitial(String placementId, UniJSCallback callback) {
        mPlacementId = placementId;
        LogWrapper.d(TAG, "initInterstitial placementId: " + mPlacementId);
        mInterstitialAd = new AMInterstitial(mActivity, placementId);

        // 回调回cocos
        mInterstitialAd.setAdListener(new AMInterstitialListener() {

            @Override
            public void onInterstitialAdLoaded() {
                MsgTools.printMsg("onInterstitialAdLoaded: " + mPlacementId);
                isReady = true;
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdLoaded", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onInterstitialAdClicked(AdSource adSource) {
                MsgTools.printMsg("onInterstitialAdClicked: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdClicked", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onInterstitialAdShow(AdSource adSource) {
                MsgTools.printMsg("onInterstitialAdShow: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdShow", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onInterstitialAdClose(AdSource adSource) {
                MsgTools.printMsg("onInterstitialAdClose: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdClose", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onInterstitialAdVideoStart(AdSource adSource) {
                MsgTools.printMsg("onInterstitialAdVideoStart: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdVideoStart", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onInterstitialAdVideoEnd(AdSource adSource) {
                MsgTools.printMsg("onInterstitialAdVideoEnd: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdVideoEnd", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onInterstitialAdVideoError(AdError adError) {
                MsgTools.printMsg("onInterstitialAdVideoError: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onInterstitialAdVideoError", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", CommonUtil.getErrorMsg(adError)));
            }

            @Override
            public void onReward(AdSource adSource) {
                MsgTools.printMsg("onReward: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onReward", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }
        });
    }

    public void loadInterstitial(final String placementId, final String settings, UniJSCallback callback) {
        MsgTools.printMsg("loadRewardedVideo: " + placementId + ", settings: " + settings);
        Utils.runOnUiThread(new Runnable() {
            public void run() {
                if (mInterstitialAd == null) {
                    initInterstitial(placementId, callback);
                }

                if (!TextUtils.isEmpty(settings)) {
                    Map<String, Object> localExtra = new HashMap();
                    try {
                        JSONObject jsonObject = new JSONObject(settings);
                        // 后续可以解析出settings的信息，放在localExtra中，传递到admore中去
                        InterstitialHelper.fillMapFromJsonObject(localExtra, jsonObject);
                        mInterstitialAd.setLocalExtra(localExtra);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mInterstitialAd.load(mActivity);
            }
        });
    }

    public void showInterstitial(UniJSCallback callback) {
        MsgTools.printMsg("showVideo: " + mPlacementId);
        Utils.runOnUiThread(new Runnable() {
            public void run() {
                if (mInterstitialAd != null) {
                    isReady = false;
                    mInterstitialAd.show(mActivity);
                } else {
                    MsgTools.printMsg("showInterstitial error, you must call loadInterstitial first " + mPlacementId);
                    UniCallbackHelper.callbackToUni(callback, "onInterstitialAdLoadFail", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", "you must call loadInterstitial first"));
                }
            }
        });
    }

    public boolean isAdReady() {
        MsgTools.printMsg("video isAdReady: " + mPlacementId);
        try {
            if (mInterstitialAd != null) {
                boolean isAdReady = mInterstitialAd.isAdReady();
                MsgTools.printMsg("video isAdReady: " + mPlacementId + ", " + isAdReady);
                return isAdReady;
            }

            MsgTools.printMsg("isAdReady error, you must call loadInterstitial first " + mPlacementId);
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
            if (mInterstitialAd != null) {
                boolean isAdExpired = mInterstitialAd.isAdExpired();
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

    public String checkAdStatus() {
        MsgTools.printMsg("checkAdStatus: " + mPlacementId);
        if (mInterstitialAd != null) {
            AMAdStatusInfo atAdStatusInfo = mInterstitialAd.checkAdStatus();
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