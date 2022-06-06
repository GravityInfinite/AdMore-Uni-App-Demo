package com.plutus.common.admore.bridge.uni.utils;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.plutus.common.admore.beans.AdSource;

public class Helper {

    /**
     * 避免空指针问题
     * @param adSource
     * @return
     */
    public static JSONObject getAdSourceJsonObjSafely(@Nullable AdSource adSource) {
        if (adSource == null) {
            return new JSONObject();
        } else {
            return (JSONObject) JSONObject.toJSON(adSource);
        }
    }
}
