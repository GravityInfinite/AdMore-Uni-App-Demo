package com.plutus.common.admore.bridge.uni.helpers;


import static com.plutus.common.admore.bridge.uni.utils.Helper.getAdSourceJsonObjSafely;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Pair;

import com.plutus.common.admore.api.AMAdStatusInfo;
import com.plutus.common.admore.api.AMRewardVideoAd;
import com.plutus.common.admore.api.AdError;
import com.plutus.common.admore.beans.AdSource;
import com.plutus.common.admore.bridge.uni.utils.CommonUtil;
import com.plutus.common.admore.bridge.uni.utils.LogWrapper;
import com.plutus.common.admore.bridge.uni.utils.MsgTools;
import com.plutus.common.admore.bridge.uni.utils.UniCallbackHelper;
import com.plutus.common.admore.listener.AMRewardVideoListener;
import com.plutus.common.core.utils.Utils;
import com.plutus.common.core.utils.activitycontext.ActivityContext;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class RewardVideoHelper extends BaseHelper {
    private static final String TAG = RewardVideoHelper.class.getSimpleName();
    AMRewardVideoAd mRewardVideoAd;
    String mPlacementId;
    Activity mActivity;
    boolean isReady = false;

    public RewardVideoHelper() {
        mActivity = ActivityContext.getInstance().getCurrentActivity();
    }

    private void initVideo(String placementId, UniJSCallback callback) {
        mPlacementId = placementId;
        LogWrapper.d(TAG, "initVideo placementId: " + mPlacementId);
        mRewardVideoAd = new AMRewardVideoAd(mActivity, placementId);

        // 回调回cocos
        mRewardVideoAd.setAdListener(new AMRewardVideoListener() {

            @Override
            public void onRewardedVideoAdLoaded() {
                MsgTools.printMsg("onRewardedVideoAdLoaded: " + mPlacementId);
                isReady = true;
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdLoaded", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onRewardedVideoAdFailed(AdError adError) {
                MsgTools.printMsg("onRewardedVideoAdFailed: " + mPlacementId + ", " + adError.getFullErrorInfo());
                isReady = false;
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdFailed", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", CommonUtil.getErrorMsg(adError)));
            }

            @Override
            public void onRewardedVideoAdPlayStart(AdSource adSource) {
                MsgTools.printMsg("onRewardedVideoAdPlayStart: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdPlayStart", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onRewardedVideoAdPlayEnd(AdSource adSource) {
                MsgTools.printMsg("onRewardedVideoAdPlayEnd: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdPlayEnd", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError adError, AdSource adSource) {
                MsgTools.printMsg("onRewardedVideoAdPlayFailed: " + mPlacementId + ", " + adError.getFullErrorInfo());
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdPlayFailed", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", CommonUtil.getErrorMsg(adError)), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onRewardedVideoAdClosed(AdSource adSource) {
                MsgTools.printMsg("onRewardedVideoAdClosed: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdClosed", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onRewardedVideoAdPlayClicked(AdSource adSource) {
                MsgTools.printMsg("onRewardedVideoAdPlayClicked: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdPlayClicked", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onReward(AdSource adSource) {
                MsgTools.printMsg("onReward: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onReward", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }
        });
    }

    public void loadRewardedVideo(final String placementId, final String settings, UniJSCallback callback) {
        MsgTools.printMsg("loadRewardedVideo: " + placementId + ", settings: " + settings);
        Utils.runOnUiThread(new Runnable() {
            public void run() {
                if (mRewardVideoAd == null) {
                    initVideo(placementId, callback);
                }

                if (!TextUtils.isEmpty(settings)) {
                    Map<String, Object> localExtra = new HashMap();
                    String userData = "";

                    try {
                        JSONObject jsonObject = new JSONObject(settings);
                        RewardVideoHelper.fillMapFromJsonObject(localExtra, jsonObject);

                        if (jsonObject.has("media_ext")) {
                            userData = jsonObject.optString("media_ext");
                        }
                        mRewardVideoAd.setLocalExtra(localExtra);
                        // s2s的信息单独伶出来设置
                        mRewardVideoAd.setS2SInfo(userData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mRewardVideoAd.load(mActivity);
            }
        });
    }

    public void showVideo(UniJSCallback callback) {
        MsgTools.printMsg("showVideo: " + mPlacementId);
        Utils.runOnUiThread(new Runnable() {
            public void run() {
                if (mRewardVideoAd != null) {
                    isReady = false;
                    mRewardVideoAd.show(mActivity);
                } else {
                    MsgTools.printMsg("showVideo error, you must call loadRewardVideo first " + mPlacementId);
                    UniCallbackHelper.callbackToUni(callback, "onRewardedVideoAdFailed", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", "showVideo error, you must call loadRewardVideo first"));
                }
            }
        });
    }

    public boolean isAdReady() {
        MsgTools.printMsg("video isAdReady: " + mPlacementId);
        try {
            if (mRewardVideoAd != null) {
                boolean isAdReady = mRewardVideoAd.isAdReady();
                MsgTools.printMsg("video isAdReady: " + mPlacementId + ", " + isAdReady);
                return isAdReady;
            }

            MsgTools.printMsg("video isAdReady error, you must call loadRewardedVideo first " + mPlacementId);
            MsgTools.printMsg("video isAdReady, end: " + mPlacementId);
        } catch (Throwable var2) {
            MsgTools.printMsg("video isAdReady, Throwable: " + var2.getMessage());
            return isReady;
        }

        return isReady;
    }

    public boolean isAdExpired() {
        MsgTools.printMsg("video isAdExpired: " + mPlacementId);
        try {
            if (mRewardVideoAd != null) {
                boolean isAdExpired = mRewardVideoAd.isAdExpired();
                MsgTools.printMsg("video isAdExpired: " + mPlacementId + ", " + isAdExpired);
                return isAdExpired;
            } else {
                return true;
            }
        } catch (Throwable e) {
            MsgTools.printMsg("video isAdExpired, Throwable: " + e.getMessage());
            return true;
        }
    }

    public String checkAdStatus() {
        MsgTools.printMsg("video checkAdStatus: " + mPlacementId);
        if (mRewardVideoAd != null) {
            AMAdStatusInfo atAdStatusInfo = mRewardVideoAd.checkAdStatus();
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