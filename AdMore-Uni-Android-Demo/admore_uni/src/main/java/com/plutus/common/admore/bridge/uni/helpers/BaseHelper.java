package com.plutus.common.admore.bridge.uni.helpers;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseHelper {
  private static final String TAG = BaseHelper.class.getSimpleName();

  public BaseHelper() {
  }

  protected Map<String, Object> getJsonMap(String json) {
    HashMap map = new HashMap();

    try {
      JSONObject jsonObject = new JSONObject(json);
      Iterator iterator = jsonObject.keys();

      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        map.put(key, jsonObject.get(key));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return map;
  }

  protected static void fillMapFromJsonObject(Map<String, Object> localExtra,
      JSONObject jsonObject) {
    try {
      Iterator keys = jsonObject.keys();

      while (keys.hasNext()) {
        String key = (String) keys.next();
        Object value = jsonObject.opt(key);
        localExtra.put(key, value);
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }

  }
}
