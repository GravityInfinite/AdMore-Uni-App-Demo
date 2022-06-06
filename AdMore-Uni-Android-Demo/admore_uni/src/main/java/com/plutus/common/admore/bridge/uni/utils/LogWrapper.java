package com.plutus.common.admore.bridge.uni.utils;


import com.elvishew.xlog.XLog;
import com.plutus.common.core.utils.ApkUtils;

public class LogWrapper {
    public static void d(String tag, String msg) {
        if (ApkUtils.isDebug()) {
            XLog.d(getPrefix(tag) + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ApkUtils.isDebug()) {
            XLog.e(getPrefix(tag) + msg);
        }
    }

    public static void json(String tag, String jsonMsg) {
        if (ApkUtils.isDebug()) {
            XLog.d("json " + getPrefix(tag));
            XLog.json(jsonMsg);
        }
    }

    /**
     * 不要超过20长度
     * @param tag
     * @return
     */
    private static String getPrefix(String tag) {
        String result = tag + "=======================";
        return result.substring(0, Math.min(result.length(), 25)) + " ";
    }
}
