package com.dawn.androidlibrary.util;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * json工具类（需要依赖Gson2.0以上）
 */
@SuppressWarnings("unused")
public class LJsonUtil {
    private final static String TAG = LJsonUtil.class.getSimpleName();
    /**
     * 对象转json
     * @param obj 实体类或集合
     */
    public static String toJson(Object obj) {
        if(obj == null)
            return "";
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json转对象
     * @param str 转换的字符串
     * @param type 类
     */
    public static <T> T fromJson(String str, Class<T> type) {
        if(str == null)
            return null;
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * Map转JSONObject
     * @param data 转换的map数据
     */
    @SuppressWarnings("WeakerAccess")
    public static JSONObject map2Json(Map<?, ?> data) {
        JSONObject object = new JSONObject();

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            String key = (String) entry.getKey();
            if (key == null) {
                throw new NullPointerException("key == null");
            }
            try {
                object.put(key, wrap(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
                LLog.exception(TAG, e);
            }
        }

        return object;
    }

    /**
     * 集合转JSONArray
     * @param data 转换的集合
     */
    @SuppressWarnings("WeakerAccess")
    public static JSONArray collection2Json(Collection<?> data) {
        JSONArray jsonArray = new JSONArray();
        if (data != null) {
            for (Object aData : data) {
                jsonArray.put(wrap(aData));
            }
        }
        return jsonArray;
    }

    /**
     * 对象转换成JSONArray
     * @param data 转换的实体类
     */
    @SuppressWarnings("WeakerAccess")
    public static JSONArray object2Json(Object data) throws JSONException {
        if (!data.getClass().isArray()) {
            throw new JSONException("Not a primitive data: " + data.getClass());
        }
        final int length = Array.getLength(data);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < length; ++i) {
            jsonArray.put(wrap(Array.get(data, i)));
        }

        return jsonArray;
    }

    /**
     *
     */
    private static Object wrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return collection2Json((Collection<?>) o);
            } else if (o.getClass().isArray()) {
                return object2Json(o);
            }
            if (o instanceof Map) {
                return map2Json((Map<?, ?>) o);
            }

            if (o instanceof Boolean || o instanceof Byte || o instanceof Character || o instanceof Double || o instanceof Float || o instanceof Integer || o instanceof Long
                    || o instanceof Short || o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception e) {
            LLog.exception(TAG, e);
        }
        return null;
    }

    /**
     * json字符串生成JSONObject
     * @param json json字符串
     */
    public static JSONObject string2JSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            JSONTokener jsonParser = new JSONTokener(json);
            jsonObject = (JSONObject) jsonParser.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
        return jsonObject;
    }
}
