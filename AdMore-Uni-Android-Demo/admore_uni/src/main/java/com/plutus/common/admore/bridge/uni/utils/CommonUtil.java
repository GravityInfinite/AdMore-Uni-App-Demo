package com.plutus.common.admore.bridge.uni.utils;


import com.plutus.common.admore.api.AdError;

public class CommonUtil {
  public CommonUtil() {
  }

  public static String getErrorMsg(AdError adError) {
    try {
      return adError.getFullErrorInfo().replace("'s", "").replace("'", "");
    } catch (Throwable e) {
      e.printStackTrace();
      return "code:[ " + adError.getCode() + " ]desc:[ ]platformCode:[ " +
          adError.getPlatformCode() + " ]platformMSG:[ " + adError.getPlatformMSG() + " ]";
    }
  }
}
