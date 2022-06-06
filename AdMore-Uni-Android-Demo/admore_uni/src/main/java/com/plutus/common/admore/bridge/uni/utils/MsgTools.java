package com.plutus.common.admore.bridge.uni.utils;

import android.util.Log;

public class MsgTools {
  private static final String TAG = "AMJSBridge";
  static boolean isDebug = true;

  public MsgTools() {
  }

  public static void printMsg(String msg) {
    if (isDebug) {
      Log.e("AMJSBridge", msg);
    }
  }

  public static void setLogDebug(boolean debug) {
    isDebug = debug;
  }
}