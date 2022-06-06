package com.plutus.common.admore.bridge.uni.helpers;



import static com.plutus.common.admore.bridge.uni.utils.Helper.getAdSourceJsonObjSafely;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.plutus.common.admore.api.AMAdStatusInfo;
import com.plutus.common.admore.api.AMNative;
import com.plutus.common.admore.api.AMNativeAdView;
import com.plutus.common.admore.api.AdError;
import com.plutus.common.admore.beans.AdSource;
import com.plutus.common.admore.bridge.uni.utils.CommonUtil;
import com.plutus.common.admore.bridge.uni.utils.LogWrapper;
import com.plutus.common.admore.bridge.uni.utils.MsgTools;
import com.plutus.common.admore.bridge.uni.utils.UniCallbackHelper;
import com.plutus.common.admore.listener.AMNativeListener;
import com.plutus.common.admore.listener.ImpressionEventListener;
import com.plutus.common.core.utils.Utils;
import com.plutus.common.core.utils.activitycontext.ActivityContext;

import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.bridge.UniJSCallback;

public class NativeHelper extends BaseHelper {
    private final String TAG = this.getClass().getSimpleName();
    private final Activity mActivity;
    private String mPlacementId;
    private AMNative mAMNative;
    private final Deque<AMNativeAdView> nativeViewDeque = new ArrayDeque<>();
    boolean isReady = false;

    public NativeHelper() {
        this.mActivity = ActivityContext.getInstance().getCurrentActivity();
    }

    private void initNative(String placementId, UniJSCallback callback) {
        mPlacementId = placementId;
        MsgTools.printMsg("initNative: " + placementId);
        mAMNative = new AMNative(mActivity, placementId);
        mAMNative.setAdListener(new AMNativeListener() {

            @Override
            public void onNativeAdLoaded() {
                MsgTools.printMsg("onNativeAdLoaded: " + mPlacementId);
                isReady = true;
                UniCallbackHelper.callbackToUni(callback, "onNativeAdLoaded", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onNativeAdShow(AdSource adSource) {
                MsgTools.printMsg("onNativeAdShow: " + mPlacementId);
                AMNativeAdView nativeAdView = null;
                if (!nativeViewDeque.isEmpty()) {
                    nativeAdView = nativeViewDeque.peek();
                }
                UniCallbackHelper.callbackToUni(callback, "onNativeAdShow", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)), Pair.create("width", (nativeAdView == null ? 0 : nativeAdView.getWidth())), Pair.create("height", (nativeAdView == null ? 0 : nativeAdView.getHeight())));
            }

            @Override
            public void onNativeAdClicked(AdSource adSource) {
                MsgTools.printMsg("onNativeAdClicked: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onNativeAdClicked", Pair.create("placement_id", mPlacementId), Pair.create("ad_source", getAdSourceJsonObjSafely(adSource)));
            }

            @Override
            public void onNativeAdVideoStart(AdSource adSource) {
                MsgTools.printMsg("onNativeAdVideoStart: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onNativeAdVideoStart", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onNativeAdVideoEnd(AdSource adSource) {
                MsgTools.printMsg("onNativeAdVideoEnd: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onNativeAdVideoEnd", Pair.create("placement_id", mPlacementId));
            }

            @Override
            public void onNativeAdVideoProgress(int progress) {
                MsgTools.printMsg("onNativeAdVideoProgress: " + mPlacementId + " progress " + progress);
                UniCallbackHelper.callbackToUni(callback, "onNativeAdVideoProgress", Pair.create("placement_id", mPlacementId), Pair.create("progress", progress));
            }

            @Override
            public void onNativeAdVideoError(AdError adError) {
                MsgTools.printMsg("onNativeAdVideoError: " + mPlacementId + ", " + adError.getFullErrorInfo());
                UniCallbackHelper.callbackToUni(callback, "onNativeAdVideoError", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", CommonUtil.getErrorMsg(adError)));
            }

            @Override
            public void onRenderSuccess(View view, float width, float height, int adnType) {
                MsgTools.printMsg("onRenderSuccess: " + mPlacementId + " " + width + " " + height + " " + adnType);
                if (!nativeViewDeque.isEmpty()) {
                    AMNativeAdView nativeAdView = nativeViewDeque.peek();
                    if (nativeAdView != null) {
                        nativeAdView.renderNativeAdToActivity(mActivity, adnType, view, new ImpressionEventListener() {
                            @Override
                            public void onImpression() {
                                LogWrapper.d(TAG, "on impression");
                            }
                        });
                    }
                }
            }

            @Override
            public void onRenderFail(int code, String msg) {
                MsgTools.printMsg("onRenderFail: " + code + " " + msg);
                UniCallbackHelper.callbackToUni(callback, "onRenderFail", Pair.create("placement_id", mPlacementId), Pair.create("error_msg", "code is " + code + " msg is " + msg));
            }

            @Override
            public void onDislikeRemoved() {
                MsgTools.printMsg("onDislikeRemoved: " + mPlacementId);
                UniCallbackHelper.callbackToUni(callback, "onDislikeRemoved", Pair.create("placement_id", mPlacementId));
                remove();
            }
        });
    }

    public void loadNative(final String placementId, final String settings, UniJSCallback callback) {
        MsgTools.printMsg("loadNative: " + placementId + ", settings: " + settings);
        Utils.runOnUiThread(() -> {
            if (mAMNative == null || !TextUtils.equals(mPlacementId, placementId)) {
                initNative(placementId, callback);
            }

            if (!TextUtils.isEmpty(settings)) {
                try {
                    JSONObject jsonObject = new JSONObject(settings);
                    int width = 0;
                    int height = 0;
                    Map<String, Object> localExtra = new HashMap();
                    if (jsonObject.has("width")) {
                        width = jsonObject.optInt("width");
                        localExtra.put("key_width", width);
                        localExtra.put("tt_image_width", width);
                        localExtra.put("mintegral_auto_render_native_width", width);
                    }

                    if (jsonObject.has("height")) {
                        height = jsonObject.optInt("height");
                        localExtra.put("key_height", height);
                        localExtra.put("tt_image_height", height);
                        localExtra.put("mintegral_auto_render_native_height", height);
                    }

                    MsgTools.printMsg("native setLocalExtra >>>  width: " + width + ", height: " + height);
                    fillMapFromJsonObject(localExtra, jsonObject);
                    MsgTools.printMsg("extra is " + localExtra.toString());
                    mAMNative.setLocalExtra(localExtra);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            mAMNative.load(mActivity);
        });
    }

    public void show(final String showConfig, UniJSCallback callback) {
        MsgTools.printMsg("native show: " + mPlacementId + ", config: " + showConfig);
        if (mAMNative == null) {
            MsgTools.printMsg("native show error: mAMNative = null");
        } else {
            Utils.runOnUiThread(() -> {
                if (mAMNative != null) {
                    isReady = false;
                    // 保证每次show native ad，都是用一个全新的view container
                    nativeViewDeque.push(new AMNativeAdView(mActivity));
                    Log.d(TAG, "deque size is " + nativeViewDeque.size());
                    mAMNative.show(mActivity);
                } else {
                    UniCallbackHelper.callbackToUni(callback, "onRenderFail", Pair.create("placement_id", mPlacementId), Pair.create("code", -1), Pair.create("msg", "showNative error, nativeAd = null"));
                }
            });
        }
    }

    public boolean isAdReady() {
        MsgTools.printMsg("video isAdReady: " + mPlacementId);
        try {
            if (mAMNative != null) {
                boolean isAdReady = mAMNative.isAdReady();
                MsgTools.printMsg("video isAdReady: " + mPlacementId + ", " + isAdReady);
                return isAdReady;
            }

            MsgTools.printMsg("isAdReady error, you must call loadNative first " + mPlacementId);
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
            if (mAMNative != null) {
                boolean isAdExpired = mAMNative.isAdExpired();
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
        MsgTools.printMsg("native remove: " + this.mPlacementId);
        AMNativeAdView nativeAdView = nativeViewDeque.pop();
        if (nativeAdView != null) {
            nativeAdView.removeSelfFromActivity();
        }
    }

    public String checkAdStatus() {
        MsgTools.printMsg("checkAdStatus: " + mPlacementId);
        if (mAMNative != null) {
            AMAdStatusInfo atAdStatusInfo = mAMNative.checkAdStatus();
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
